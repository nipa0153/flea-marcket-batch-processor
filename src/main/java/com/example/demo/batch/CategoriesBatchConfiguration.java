package com.example.demo.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.example.demo.domain.Category;
import com.example.demo.domain.Original;
import com.example.demo.dto.OriginalsDto;

import io.micrometer.common.lang.NonNull;

import java.util.Arrays;

@EnableBatchProcessing
@Configuration
public class CategoriesBatchConfiguration {

    private static final String INSERT_INTO_PARENT_CATEGORIES = """
            INSERT INTO category (
                name)
            VALUES
                (:name)
            """;

    private static final String INSERT_INTO_CHILD_CATEGORIES = """
            INSERT INTO category (
                name
                , parent_id
                )
            VALUES
                (:name
                , :parent_id
                )
            """;
    private static final String INSERT_INTO_GRAND_CHILD_CATEGORIES = """
            INSERT INTO category (
                name
                , parent_id
                , name_all
                )
            VALUES
                (:name
                , :parent_id
                , :name_all
                )
            """;

    @Bean
    public JdbcCursorItemReader<Original> reader(@NonNull DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Original>()
                .name("originalReader")
                .dataSource(dataSource)
                .sql("SELECT id, category_name FROM originals ORDER BY id ASC LIMIT 100;")
                .rowMapper((rs, i) -> {
                    Original original = new Original();
                    original.setId(rs.getInt("id"));
                    original.setCategoryName(rs.getString("category_name"));
                    return original;
                })
                .build();
    }

    @Bean
    public CategoryItemProcessor processor(JdbcTemplate template) {
        return new CategoryItemProcessor(template);
    }

    /**
     * 親カテゴリの挿入
     * 
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Category> writerForParentCategories(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Category>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_INTO_PARENT_CATEGORIES)
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    /**
     * 子カテゴリの挿入
     * 
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Category> writerForChildCategories(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Category>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_INTO_CHILD_CATEGORIES)
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    /**
     * 孫カテゴリの挿入
     * 
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Category> writerForGrandChildCategories(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Category>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_INTO_GRAND_CHILD_CATEGORIES)
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job moveToCategoryJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("moveToCategoryJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    /**
     * 
     * @param jobRepository
     * @param transactionManager
     * @param reader
     * @param writerForParentCategories
     * @param writerForChildCategories
     * @param writerForGrandChildCategories
     * @return
     */
    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
            JdbcCursorItemReader<Original> reader, CategoryItemProcessor processor,
            CategoryStepExecutionListener listener,
            JdbcBatchItemWriter<Category> writerForParentCategories,
            JdbcBatchItemWriter<Category> writerForChildCategories,
            JdbcBatchItemWriter<Category> writerForGrandChildCategories) {

        CompositeItemWriter<Category> compositeItemWriter = compositeItemWriter(
                writerForParentCategories, writerForChildCategories, writerForGrandChildCategories);

        return new StepBuilder("step1", jobRepository)
                .listener(new CategoryStepExecutionListener())
                .<Original, Category>chunk(50000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(compositeItemWriter)
                .build();
    }

    @Bean
    public CompositeItemWriter<Category> compositeItemWriter(
            JdbcBatchItemWriter<Category> writerForParentCategories,
            JdbcBatchItemWriter<Category> writerForChildCategories,
            JdbcBatchItemWriter<Category> writerForGrandChildCategories) {
        CompositeItemWriter<Category> writer = new CompositeItemWriter<>();
        writer.setDelegates(
                Arrays.asList(writerForParentCategories, writerForChildCategories, writerForGrandChildCategories));
        return writer;
    }

}
