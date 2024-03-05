package com.example.CategoryBatch.Config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.example.CategoryBatch.Config.Listener.JobCompletionNotificationListener;
import com.example.CategoryBatch.Tasklet.CategoryTasklet;
import com.example.DTO.ItemsDto;
import com.example.Domain.Items;

@Configuration
public class ImportTableConfiguration {

    @Autowired
    private JdbcCursorItemReader<ItemsDto> itemReader;

    @Autowired
    private JdbcBatchItemWriter<Items> itemWriter;

    /**
     * データソース設定
     * 
     * @return
     */
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/postgres");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("postgres");
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        return dataSourceBuilder.build();
    }

    /**
     * ジョブ
     * 
     * @param jobRepository
     * @param step1
     * @param listener
     * @return
     */
    @SuppressWarnings("null")
    @Bean
    public Job moveToOriginalsJob(JobRepository jobRepository, Step step1, Step step2,
            JobCompletionNotificationListener notificationListener) {
        return new JobBuilder("moveToOriginalsJob", jobRepository)
                .listener(notificationListener)
                .start(step1)
                .next(step2)
                .build();
    }

    /**
     * Categoryステップ
     * 
     * @param jobRepository
     * @param transactionManager
     * @param template
     * @param categoryTasklet
     * @param execution
     * @return
     */
    @SuppressWarnings("null")
    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
            JdbcTemplate template, CategoryTasklet categoryTasklet) {

        return new StepBuilder("step1", jobRepository)
                .tasklet(categoryTasklet, transactionManager)
                .build();
    }

    /**
     * itemsステップ
     * 
     * @param jobRepository
     * @param transactionManager
     * @param itemReader
     * @param itemWriter
     * @return
     */
    @SuppressWarnings("null")
    @Bean
    public Step step2(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("Step2", jobRepository)
                .<ItemsDto, Items>chunk(100000, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

}