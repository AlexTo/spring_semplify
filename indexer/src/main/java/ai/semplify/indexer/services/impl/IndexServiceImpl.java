package ai.semplify.indexer.services.impl;

import ai.semplify.indexer.mappers.DocMapper;
import ai.semplify.indexer.models.Doc;
import ai.semplify.indexer.repositories.DocRepository;
import ai.semplify.indexer.services.IndexService;
import lombok.var;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class IndexServiceImpl implements IndexService {

    private DocRepository repo;
    private DocMapper mapper;

    public IndexServiceImpl(DocRepository repo, DocMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Mono<Doc> indexFile(Doc doc, String fileContent) {
        var entity = mapper.toEntity(doc);
        entity.setContent(fileContent);
        var result = repo.save(entity);
        return result.map(e -> mapper.toModel(e));
    }

    @Override
    public Mono<Doc> indexFile(Doc doc, FilePart filePart) {
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

    @Override
    public Flux<Doc> findAll() {
        return repo.findAll().map(m -> mapper.toModel(m));
    }
}
