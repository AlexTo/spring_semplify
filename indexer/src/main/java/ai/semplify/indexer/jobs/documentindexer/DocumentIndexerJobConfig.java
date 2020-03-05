package ai.semplify.indexer.jobs.documentindexer;

import ai.semplify.indexer.entities.postgresql.Document;
import ai.semplify.indexer.repositories.postgresql.DocumentJpaRepository;
import ai.semplify.indexer.services.IndexService;
import lombok.var;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
public class DocumentIndexerJobConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private DocumentJpaRepository documentJpaRepository;
    private JobLauncher jobLauncher;
    private IndexService indexService;

    public DocumentIndexerJobConfig(JobBuilderFactory jobBuilderFactory,
                                    StepBuilderFactory stepBuilderFactory,
                                    DocumentJpaRepository documentJpaRepository,
                                    JobLauncher jobLauncher,
                                    IndexService indexService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.documentJpaRepository = documentJpaRepository;
        this.jobLauncher = jobLauncher;
        this.indexService = indexService;
    }

    @Scheduled(fixedRate = 5000)
    public void jobScheduler() throws
            JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {

        var documents = documentJpaRepository
                .findAllByStatusIsNullOrderByLastModifiedDateAsc(PageRequest.of(0, 1));

        if (documents.isEmpty()) {
            return;
        }

        var jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(documentIndexJob(), jobParameters);
    }

    @Bean
    @StepScope
    public DocumentItemReader documentItemReader() {
        return new DocumentItemReader(documentJpaRepository);
    }

    @Bean
    @StepScope
    public DocumentItemProcessor documentItemProcessor() {
        return new DocumentItemProcessor(documentJpaRepository);
    }

    @Bean
    @StepScope
    public DocumentItemWriter documentItemWriter() {
        return new DocumentItemWriter(indexService, documentJpaRepository);
    }

    @Bean
    public Step documentIndexJobStep() {
        return stepBuilderFactory.get("documentIndexJobStep")
                .<List<Document>, List<Document>>chunk(1)
                .reader(documentItemReader())
                .processor(documentItemProcessor())
                .writer(documentItemWriter())
                .build();
    }


    @Bean(name = "documentIndexJob")
    public Job documentIndexJob() {
        return jobBuilderFactory.get("documentIndexJob")
                .incrementer(new RunIdIncrementer())
                .flow(documentIndexJobStep())
                .end()
                .build();
    }

}
