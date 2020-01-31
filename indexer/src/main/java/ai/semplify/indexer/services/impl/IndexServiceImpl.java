package ai.semplify.indexer.services.impl;

import ai.semplify.indexer.mappers.DocMapper;
import ai.semplify.indexer.models.Doc;
import ai.semplify.indexer.services.IndexService;
import lombok.var;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class IndexServiceImpl implements IndexService {

    private ElasticsearchOperations elasticsearchOperations;
    private final IndexCoordinates indexCoordinates;

    private DocMapper mapper;

    public IndexServiceImpl(ElasticsearchOperations elasticsearchOperations, IndexCoordinates indexCoordinates, DocMapper mapper) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.indexCoordinates = indexCoordinates;
        this.mapper = mapper;
    }

    public Mono<String> indexFile(Doc doc, String fileContent) {
        var entity = mapper.toEntity(doc);
        entity.setContent(fileContent);
        var indexQuery = new IndexQueryBuilder().withId(doc.getUri()).withObject(entity).build();
        return Mono.just(elasticsearchOperations.index(indexQuery, indexCoordinates));
    }

    @Override
    public Mono<String> indexFile(Doc doc, FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    try {
                        var inputStream = dataBuffer.asInputStream();
                        var handler = new BodyContentHandler(-1);
                        var metadata = new Metadata();
                        var parser = new AutoDetectParser();
                        parser.parse(inputStream, handler, metadata);
                        inputStream.close();
                        var content = handler.toString();
                        return indexFile(doc, content);
                    } catch (IOException | SAXException | TikaException e) {
                        e.printStackTrace();
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }
}
