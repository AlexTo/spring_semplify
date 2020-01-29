package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.mappers.AnnotationMapper;
import ai.semplify.entityhub.models.Annotation;
import ai.semplify.entityhub.models.TextAnnotationRequest;
import ai.semplify.entityhub.models.spotlight.DBPediaAnnotation;
import ai.semplify.entityhub.services.NERService;
import lombok.var;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.xml.sax.SAXException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class NERServiceImpl implements NERService {

    private WebClient spotlightWebClient;
    private AnnotationMapper mapper;

    public NERServiceImpl(@Qualifier("spotlightWebClient") WebClient spotlightWebClient,
                          AnnotationMapper mapper) {
        this.spotlightWebClient = spotlightWebClient;
        this.mapper = mapper;
    }


    @Override
    public Mono<Annotation> annotateText(TextAnnotationRequest textAnnotationRequest) {
        return annotationTextFromDBPedia(textAnnotationRequest);
    }

    @Override
    public Mono<Annotation> annotateFile(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    try {
                        var inputStream = dataBuffer.asInputStream();
                        var handler = new BodyContentHandler(-1);
                        var metadata = new Metadata();
                        var parser = new AutoDetectParser();
                        parser.parse(inputStream, handler, metadata);
                        inputStream.close();
                        var request = new TextAnnotationRequest();
                        request.setText(handler.toString());
                        return annotateText(request);
                    } catch (IOException | SAXException | TikaException e) {
                        e.printStackTrace();
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }

    private Mono<Annotation> annotationTextFromDBPedia(TextAnnotationRequest textAnnotationRequest) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("text", textAnnotationRequest.getText());
        params.add("confidence", "0.8");

        return spotlightWebClient.post().uri("/rest/annotate")
                .bodyValue(params)
                .retrieve()
                .bodyToMono(DBPediaAnnotation.class)
                .map(dbPediaAnnotation -> {
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
                });
    }
}
