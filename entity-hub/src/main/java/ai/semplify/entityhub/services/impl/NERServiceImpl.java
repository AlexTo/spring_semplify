package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.mappers.AnnotationMapper;
import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.AnnotationResource;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import ai.semplify.entityhub.services.EntityService;
import ai.semplify.entityhub.services.NERService;
import ai.semplify.feignclients.clients.fileserver.FileServerFeignClient;
import ai.semplify.feignclients.clients.poolparty.PoolPartyExtractorFeignClient;
import ai.semplify.feignclients.clients.spotlight.DBPediaSpotlightFeignClient;
import lombok.var;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NERServiceImpl implements NERService {

    private AnnotationMapper mapper;
    private DBPediaSpotlightFeignClient spotlightFeignClient;
    private PoolPartyExtractorFeignClient poolPartyExtractorFeignClient;
    private FileServerFeignClient fileServerFeignClient;

    private EntityService entityService;


    @Value("${poolparty.projectId}")
    private String projectId;

    public NERServiceImpl(DBPediaSpotlightFeignClient spotlightFeignClient,
                          PoolPartyExtractorFeignClient poolPartyExtractorFeignClient,
                          AnnotationMapper mapper, FileServerFeignClient fileServerFeignClient, EntityService entityService) {
        this.spotlightFeignClient = spotlightFeignClient;
        this.poolPartyExtractorFeignClient = poolPartyExtractorFeignClient;
        this.mapper = mapper;
        this.fileServerFeignClient = fileServerFeignClient;
        this.entityService = entityService;
    }


    @Override
    public Annotation annotateText(TextAnnotationRequest textAnnotationRequest) throws IOException {
        var dbpediaAnnotation = annotateTextFromDBPedia(textAnnotationRequest);
        var poolpartyAnnotation = annotateTextFromPoolParty(textAnnotationRequest);
        var mergedResources = new ArrayList<AnnotationResource>();
        mergedResources.addAll(dbpediaAnnotation.getResources());
        mergedResources.addAll(poolpartyAnnotation.getResources());
        dbpediaAnnotation.setResources(mergedResources);
        return dbpediaAnnotation;
    }

    @Override
    public Annotation annotateFile(MultipartFile filePart) throws IOException, TikaException, SAXException {

        var inputStream = filePart.getInputStream();
        var handler = new BodyContentHandler(-1);
        var metadata = new Metadata();
        var parser = new AutoDetectParser();
        parser.parse(inputStream, handler, metadata);
        inputStream.close();
        var request = new TextAnnotationRequest();
        request.setText(handler.toString());
        return annotateText(request);

    }

    @Override
    public Annotation annotateServerFile(Long fileId) throws IOException, TikaException, SAXException {
        var file = fileServerFeignClient.download(fileId);
        return annotateFile(file);
    }

    private Annotation annotateTextFromDBPedia(TextAnnotationRequest textAnnotationRequest) {
        var text = textAnnotationRequest.getText();

        Map<String, String> params = new HashMap<>();

        params.put("text", text);
        params.put("confidence", "0.8");

        var dbPediaAnnotation = spotlightFeignClient.annotate(params);

        var annotation = mapper.toAnnotation(dbPediaAnnotation);

        var resources = annotation.getResources();
        if (resources == null) {
            annotation.setResources(new ArrayList<>());
        } else {
            var resourcesWithoutDuplicate = resources.stream()
                    .distinct()
                    .sorted((r1, r2) -> {
                        if (r1.getSimilarityScore() == null) {
                            return 1;
                        }
                        if (r2.getSimilarityScore() == null) {
                            return -1;
                        }
                        return r2.getSimilarityScore().compareTo(r1.getSimilarityScore());
                    })
                    .limit(10)
                    .peek(resource -> {

                        var contextStart = Math.max(resource.getOffset() - 100, 0);
                        var contextEnd = Math.min(resource.getOffset() + 100, text.length());
                        resource.setSource("DBPedia");
                        var prefLabel = entityService.getPrefLabel(resource.getUri());
                        resource.setPrefLabel(prefLabel.getPrefLabel());
                        resource.setContext(text.substring(contextStart, contextEnd));
                    })
                    .collect(Collectors.toList());
            annotation.setResources(resourcesWithoutDuplicate);
        }
        return annotation;
    }

    private Annotation annotateTextFromPoolParty(TextAnnotationRequest textAnnotationRequest) throws IOException {
        var text = textAnnotationRequest.getText();
        var lowerCaseText = text.toLowerCase();
        Map<String, String> params = new HashMap<>();
        params.put("text", text);
        params.put("language", "en");
        params.put("documentUri", "http://localhost/file/");
        params.put("projectId", projectId);
        String response = poolPartyExtractorFeignClient.annotate(params);
        var rdfParser = Rio.createParser(RDFFormat.RDFXML);
        Model model = new LinkedHashModel();
        rdfParser.setRDFHandler(new StatementCollector(model));
        rdfParser.parse(new ByteArrayInputStream(response.getBytes()), "");

        var annotation = new Annotation();
        var resources = new ArrayList<AnnotationResource>();
        for (var statement : model.filter(null, RDF.TYPE, SKOS.CONCEPT)) {
            var resource = new AnnotationResource();
            var subj = statement.getSubject();
            resource.setUri(subj.stringValue());
            var prefLabel = Models.getProperty(model, subj, SKOS.PREF_LABEL)
                    .map(org.eclipse.rdf4j.model.Value::stringValue)
                    .orElse(null);

            resource.setPrefLabel(prefLabel);
            resource.setSurfaceForm(prefLabel);

            var offset = lowerCaseText.indexOf(Objects.requireNonNull(prefLabel).toLowerCase());
            resource.setOffset(offset);

            var contextStart = Math.max(offset - 100, 0);
            var contextEnd = Math.min(offset + 100, text.length());
            resource.setContext(text.substring(contextStart, contextEnd));

            resource.setSource("PoolParty");
            resources.add(resource);
        }

        annotation.setResources(resources);
        return annotation;
    }
}
