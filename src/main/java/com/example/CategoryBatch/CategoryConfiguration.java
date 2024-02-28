package com.example.CategoryBatch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.example.DTO.CategoryDto;
import com.example.Domain.Category;
import com.example.Domain.Original;

import java.util.Arrays;

// @EnableBatchProcessing
@Configuration
public class CategoryConfiguration {

    private static final String INSERT_INTO_PARENT_CATEGORIES = """
            INSERT INTO
                categories (
                    name
                    )
            SELECT DISTINCT
                parent
            FROM
                temp_categories
            WHERE
                parent IS NOT NULL;
            """;

    private static final String INSERT_INTO_CHILD_CATEGORIES = """
            INSERT INTO
                categories (
                    name
                    , parent_id
                    )
            SELECT DISTINCT
                child
                , c.id
            FROM
                temp_categories AS tc
            JOIN
                categories AS c ON tc.parent = c.name
            WHERE
                child IS NOT NULL;
            """;
    private static final String INSERT_INTO_GRAND_CHILD_CATEGORIES = """
            INSERT INTO
                categories (
                    name
                    , parent_id
                    , name_all
                    )
            SELECT DISTINCT
                tc.grandchild
                , c1.id
                , tc.parent || '/' || tc.child || '/' || tc.grandchild
            FROM
                temp_categories AS tc
            JOIN
                categories AS c1 ON tc.child = c1.name
            JOIN
                categories AS c2 ON tc.parent = c2.name
            AND
                c1.parent_id = c2.id
            WHERE
                tc.grandchild IS NOT NULL;
            """;
    // @Autowired
    // public DataSource dataSource;

    @Autowired
    private JdbcTemplate template;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/gs-batch-processing");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        return dataSourceBuilder.build();
    }

    @Bean
    public JdbcCursorItemReader<CategoryDto> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<CategoryDto>()
                .name("originalReader")
                .dataSource(dataSource)
                .sql("SELECT id, parent, child, grandChild FROM temp_categories ORDER BY id ASC;")
                .rowMapper(new BeanPropertyRowMapper<>(CategoryDto.class))
                .build();
    }

    // @Bean
    // public Tasklet parentInsertTasklet() {
    // return (contribution, chunkContext) -> {
    // template.execute(INSERT_INTO_PARENT_CATEGORIES);
    // return RepeatStatus.FINISHED;
    // };
    // }

    // @Bean
    // public Tasklet childInsertTasklet() {
    // return (contribution, chunkContext) -> {
    // template.execute(INSERT_INTO_CHILD_CATEGORIES);
    // return RepeatStatus.FINISHED;
    // };
    // }

    // @Bean
    // public Tasklet grandChiidInsertTasklet() {
    // return (contribution, chunkContext) -> {
    // template.execute(INSERT_INTO_GRAND_CHILD_CATEGORIES);
    // return RepeatStatus.FINISHED;
    // };
    // }
    // @Bean
    // public CategoryItemProcessor processor(JdbcTemplate template) {
    // return new CategoryItemProcessor(template);
    // }

    /**
     * 親カテゴリの挿入
     * 
     * @param dataSource
     * @return
     */
    // @Bean
    // public ItemWriter<CategoryDto> categoryWriter() {
    // return items -> {
    // JdbcTemplate template = new JdbcTemplate();

    // template.update(INSERT_INTO_PARENT_CATEGORIES);
    // template.update(INSERT_INTO_CHILD_CATEGORIES);
    // template.update(INSERT_INTO_GRAND_CHILD_CATEGORIES);
    // };
    // }

    /**
     * 親カテゴリの挿入
     *
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Category> parentCategoryWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Category>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_INTO_PARENT_CATEGORIES)
                .dataSource(dataSource)
                .build();
    }

    /**
     * 子カテゴリの挿入
     *
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Category> childCategoryWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Category>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_INTO_CHILD_CATEGORIES)
                .dataSource(dataSource)
                .build();
    }

    /**
     * 孫カテゴリの挿入
     *
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<Category> grandChildCategoryWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Category>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_INTO_GRAND_CHILD_CATEGORIES)
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job moveToCategoryJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("moveToCategoryJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    // @Bean
    // public Step step1(JobRepository jobRepository, DataSourceTransactionManager
    // transactionManager,
    // JdbcCursorItemReader<CategoryDto> reader, ItemWriter<CategoryDto> writer,
    // CategoryStepExecutionListener listener) {
    // return new StepBuilder("step1", jobRepository)
    // .listener(listener)
    // .<CategoryDto, Category>chunk(1500000, transactionManager)
    // .reader(reader)
    // .writer()
    // .build();
    // }

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
            JdbcCursorItemReader<CategoryDto> reader,
            CategoryStepExecutionListener listener,
            JdbcBatchItemWriter<Category> parentWriter,
            JdbcBatchItemWriter<Category> childWriter,
            JdbcBatchItemWriter<Category> grandChildWriter,
            DataSource dataSource) {

        CompositeItemWriter<Category> compositeItemWriter = compositeItemWriter(dataSource);

        return new StepBuilder("step1", jobRepository)
                .listener(listener)
                .<CategoryDto, Category>chunk(1500, transactionManager)
                .reader(reader)
                .writer(compositeItemWriter)
                .build();
    }

    @Bean
    public CompositeItemWriter<Category> compositeItemWriter(DataSource dataSource) {
        CompositeItemWriter<Category> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(parentCategoryWriter(dataSource), childCategoryWriter(dataSource),
                grandChildCategoryWriter(dataSource)));
        return writer;
    }

}
