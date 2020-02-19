package ai.semplify.indexer.services.impl;

import ai.semplify.feignclients.clients.entityhub.EntityHubFeignClient;
import ai.semplify.feignclients.clients.entityhub.models.PrefLabelRequest;
import ai.semplify.feignclients.clients.entityhub.models.TypeCheckRequest;
import ai.semplify.indexer.entities.elasticsearch.Annotation;
import ai.semplify.indexer.entities.elasticsearch.AnnotationClass;
import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.entities.postgresql.DocumentType;
import ai.semplify.indexer.mappers.DocumentMapper;
import ai.semplify.indexer.models.DocumentMetadata;
import ai.semplify.indexer.repositories.postgresql.DocumentRepository;
import ai.semplify.indexer.services.IndexService;
import lombok.var;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    private ElasticsearchOperations elasticsearchOperations;
    private final IndexCoordinates indexCoordinates;
    private DocumentRepository documentRepository;
    private EntityHubFeignClient entityHubFeignClient;

    private DocumentMapper mapper;

    public IndexServiceImpl(ElasticsearchOperations elasticsearchOperations,
                            IndexCoordinates indexCoordinates,
                            DocumentRepository documentRepository,
                            EntityHubFeignClient entityHubFeignClient,
                            DocumentMapper mapper) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.indexCoordinates = indexCoordinates;
        this.documentRepository = documentRepository;
        this.entityHubFeignClient = entityHubFeignClient;
        this.mapper = mapper;

    }

    @Override
    public void submitFile(DocumentMetadata documentMetadata, MultipartFile filePart) throws IOException {
        var document = mapper.toEntity(documentMetadata);
        document.setContent(filePart.getBytes());
        document.setContentType(filePart.getContentType());
        document.setType(DocumentType.FILE.getValue());
        documentRepository.save(document);
    }

    @Override
    public void indexFile(Document document) throws TikaException, SAXException, IOException {
        var inputStream = new ByteArrayInputStream(document.getContent());

        var handler = new BodyContentHandler(-1);
        var metadata = new Metadata();
        var parser = new AutoDetectParser();
        parser.parse(inputStream, handler, metadata);
        inputStream.close();
        var content = handler.toString();
        var indexedDocument = mapper.toEntity(document);
        indexedDocument.setContent(content);

        // Build facets //
        var facets = new HashSet<String>();
        facets.add("http://dbpedia.org/ontology/Place");
        facets.add("http://dbpedia.org/ontology/Person");
        facets.add("http://dbpedia.org/ontology/Organisation");
        facets.add("https://localhost/DoENSW/1102");
        facets.add("https://localhost/DoENSW/1729");

        if (document.getAnnotations() != null) {
            var indexedAnnotations = new HashSet<Annotation>();
            for (var uri : document.getAnnotations()) {
                var annotation = new Annotation();
                annotation.setUri(uri);
                annotation.setLabel(getPrefLabel(uri));
                var annotationClasses = new HashSet<AnnotationClass>();
                for (var facet : facets) {
                    var typeCheckRequest = new TypeCheckRequest();
                    typeCheckRequest.setEntity(uri);
                    typeCheckRequest.setType(facet);
                    var typeCheck = entityHubFeignClient.isSubClassOf(typeCheckRequest);
                    if (typeCheck.getAns()) {
                        var annotationClass = new AnnotationClass();
                        annotationClass.setUri(facet);
                        annotationClass.setLabel(getPrefLabel(facet));
                        annotationClasses.add(annotationClass);
                    }
                    typeCheck = entityHubFeignClient.isNarrowerConceptOf(typeCheckRequest);
                    if (typeCheck.getAns()) {
                        var annotationClass = new AnnotationClass();
                        annotationClass.setUri(facet);
                        annotationClass.setLabel(getPrefLabel(facet));
                        annotationClasses.add(annotationClass);
                    }
                }
                annotation.setClasses(annotationClasses);
                indexedAnnotations.add(annotation);
            }
            indexedDocument.setAnnotations(indexedAnnotations);
        }

        var indexQuery = new IndexQueryBuilder()
                .withId(indexedDocument.getUri()).withObject(indexedDocument)
                .build();
        elasticsearchOperations.index(indexQuery, indexCoordinates);
    }

    private String getPrefLabel(String uri) {
        var prefLabelRequest = new PrefLabelRequest();
        prefLabelRequest.setUri(uri);
        var response = entityHubFeignClient.getPrefLabel(prefLabelRequest);
        return response.getPrefLabel();
    }
}
