package ai.semplify.indexer.jobs.subjectindexer;

import ai.semplify.commons.feignclients.entityhub.EntityHubFeignClient;
import ai.semplify.commons.models.entityhub.PrefLabelRequest;
import ai.semplify.indexer.entities.elasticsearch.Subject;
import lombok.var;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.elasticsearch.core.completion.Completion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectItemProcessor implements ItemProcessor<List<Subject>, List<Subject>> {

    private EntityHubFeignClient entityHubFeignClient;

    public SubjectItemProcessor(EntityHubFeignClient entityHubFeignClient) {
        this.entityHubFeignClient = entityHubFeignClient;
    }

    @Override
    public List<Subject> process(List<Subject> subjects) throws Exception {
        for (var subject : subjects) {
            var prefLabelRequest = new PrefLabelRequest();
            prefLabelRequest.setUri(subject.getUri());
            var prefLabel = entityHubFeignClient.getPrefLabel(prefLabelRequest);
            subject.setPrefLabel(prefLabel.getPrefLabel());
            var tokens = Arrays.stream(prefLabel.getPrefLabel().split(" "))
                    .map(String::trim)
                    .collect(Collectors.toList());

            var completion = new Completion(tokens);
            subject.setCompletion(completion);
        }
        return subjects;
    }
}
