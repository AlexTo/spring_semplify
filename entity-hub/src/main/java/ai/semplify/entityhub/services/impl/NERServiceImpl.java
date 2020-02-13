package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.mappers.AnnotationMapper;
import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import ai.semplify.entityhub.services.NERService;
import ai.semplify.feignclients.clients.spotlight.DBPediaSpotlightFeignClient;
import lombok.var;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NERServiceImpl implements NERService {

    private AnnotationMapper mapper;
    private DBPediaSpotlightFeignClient spotlightFeignClient;

    public NERServiceImpl(DBPediaSpotlightFeignClient spotlightFeignClient,
                          AnnotationMapper mapper) {
        this.spotlightFeignClient = spotlightFeignClient;
        this.mapper = mapper;
    }


    @Override
    public Annotation annotateText(TextAnnotationRequest textAnnotationRequest) {
        return annotationTextFromDBPedia(textAnnotationRequest);
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

    private Annotation annotationTextFromDBPedia(TextAnnotationRequest textAnnotationRequest) {
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
}
