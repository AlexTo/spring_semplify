package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.mappers.AnnotationMapper;
import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.AnnotationResource;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import ai.semplify.entityhub.services.NERService;
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
import java.util.stream.Collectors;

@Service
public class NERServiceImpl implements NERService {

    private AnnotationMapper mapper;
    private DBPediaSpotlightFeignClient spotlightFeignClient;
    private PoolPartyExtractorFeignClient poolPartyExtractorFeignClient;

    @Value("${poolparty.projectId}")
    private String projectId;

    public NERServiceImpl(DBPediaSpotlightFeignClient spotlightFeignClient,
                          PoolPartyExtractorFeignClient poolPartyExtractorFeignClient,
                          AnnotationMapper mapper) {
        this.spotlightFeignClient = spotlightFeignClient;
        this.poolPartyExtractorFeignClient = poolPartyExtractorFeignClient;
        this.mapper = mapper;
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

    private Annotation annotateTextFromDBPedia(TextAnnotationRequest textAnnotationRequest) {
        Map<String, String> params = new HashMap<>();

        params.put("text", textAnnotationRequest.getText());
        params.put("confidence", "0.8");

        var dbPediaAnnotation = spotlightFeignClient.annotate(params);

        var annotation = mapper.toAnnotation(dbPediaAnnotation);
        var text = annotation.getText();

        var resources = annotation.getResources();
        if (resources == null) {
            annotation.setResources(new ArrayList<>());
        } else {
            var resourcesWithoutDuplicate = resources.stream()
                    .distinct()
                    .peek(resource -> {
                        var contextStart = Math.max(resource.getOffset() - 50, 0);
                        var contextEnd = Math.min(resource.getOffset() + 50, text.length());
                        resource.setSource("dbpedia");
                        resource.setContext(text.substring(contextStart, contextEnd));
                    })
                    .collect(Collectors.toList());
            annotation.setResources(resourcesWithoutDuplicate);
        }
        return annotation;
    }

    private Annotation annotateTextFromPoolParty(TextAnnotationRequest textAnnotationRequest) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("text", textAnnotationRequest.getText());
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
            resource.setSource("poolparty");
            resources.add(resource);
        }

        annotation.setResources(resources);
        return annotation;
    }
}
