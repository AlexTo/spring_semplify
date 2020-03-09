package ai.semplify.indexer.jobs.subjectindexer;

import ai.semplify.commons.feignclients.entityhub.EntityHubFeignClient;
import ai.semplify.indexer.entities.elasticsearch.Subject;
import org.eclipse.rdf4j.repository.Repository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.List;

@Configuration
public class SubjectIndexerJobConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private Repository sparqlEndpoint;
    private EntityHubFeignClient entityHubFeignClient;
    private IndexCoordinates subjectsIndex;
    private ElasticsearchOperations elasticsearchOperations;

    public SubjectIndexerJobConfig(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory,
                                   Repository sparqlEndpoint,
                                   EntityHubFeignClient entityHubFeignClient,
                                   @Qualifier("subjects_index") IndexCoordinates subjectsIndex,
                                   ElasticsearchOperations elasticsearchOperations) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.sparqlEndpoint = sparqlEndpoint;
        this.entityHubFeignClient = entityHubFeignClient;
        this.subjectsIndex = subjectsIndex;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Bean
    @StepScope
    public SubjectItemReader subjectItemReader() {
        return new SubjectItemReader(sparqlEndpoint);
    }

    @Bean
    @StepScope
    public SubjectItemProcessor subjectItemProcessor() {
        return new SubjectItemProcessor(entityHubFeignClient);
    }

    @Bean
    @StepScope
    public SubjectItemWriter subjectItemWriter() {
        return new SubjectItemWriter(subjectsIndex, elasticsearchOperations);
    }

    @Bean
    public Step subjectIndexJobStep() {
        return stepBuilderFactory.get("subjectIndexJobStep")
                .<List<Subject>, List<Subject>>chunk(1)
                .reader(subjectItemReader())
                .processor(subjectItemProcessor())
                .writer(subjectItemWriter())
                .build();
    }


    @Bean(name = "subjectIndexJob")
    public Job subjectIndexJob() {
        return jobBuilderFactory.get("subjectIndexJob")
                .incrementer(new RunIdIncrementer())
                .flow(subjectIndexJobStep())
                .end()
                .build();
    }
}
