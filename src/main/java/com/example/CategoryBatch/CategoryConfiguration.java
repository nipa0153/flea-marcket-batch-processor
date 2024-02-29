package com.example.CategoryBatch;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class CategoryConfiguration {

    // @Autowired
    // private PlatformTransactionManager transactionManager;

    // @Bean
    // public TransactionTemplate transactionTemplate() {
    // return new TransactionTemplate(transactionManager);
    // }

    private final DataSource dataSource;

    public CategoryConfiguration(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    private static final Logger log = LogManager.getLogger(CategoryConfiguration.class);

    /**
     * ジョブ
     * 
     * @param jobRepository
     * @param step1
     * @param listener
     * @return
     */
    @Bean
    public Job moveToCategoryJob(JobRepository jobRepository, Step step1,
            JobCompletionNotificationListener notificationListener) {
        return new JobBuilder("moveToCategoryJob", jobRepository)
                .listener(notificationListener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
            JdbcTemplate template, CategoryTasklet categoryTasklet,
            TaskletStepExecution execution) {

        return new StepBuilder("step1", jobRepository)
                .listener(execution)
                .tasklet(categoryTasklet, transactionManager)
                .build();
    }

}