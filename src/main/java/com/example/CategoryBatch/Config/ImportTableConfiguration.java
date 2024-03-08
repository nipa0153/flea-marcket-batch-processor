package com.example.CategoryBatch.Config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.example.CategoryBatch.Config.Listener.ChunkCountingListener;
import com.example.CategoryBatch.Config.Listener.JobCompletionNotificationListener;
import com.example.CategoryBatch.Config.Listener.StepCompletionNotificationListener;
import com.example.CategoryBatch.Config.Processor.ItemCategoryCheckProcessor;
import com.example.CategoryBatch.Tasklet.InsertChildTasklet;
import com.example.CategoryBatch.Tasklet.InsertGrandChildTasklet;
import com.example.CategoryBatch.Tasklet.InsertParentTasklet;
import com.example.DTO.ItemsDto;
import com.example.Domain.Items;

@Configuration
public class ImportTableConfiguration {

        @Autowired
        private JdbcCursorItemReader<ItemsDto> itemReader;

        @Autowired
        private JdbcBatchItemWriter<Items> itemWriter;

        @Bean
        public ItemCategoryCheckProcessor processor(ItemsDto itemsDto) {
                return new ItemCategoryCheckProcessor();
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
        @Lazy
        public Job moveToOriginalsJob(JobRepository jobRepository,
                        Step step1, Step step2, Step step3, Step step4,
                        JobCompletionNotificationListener notificationListener) {
                return new JobBuilder("moveToOriginalsJob", jobRepository)
                                .listener(notificationListener)
                                .start(step1)
                                .next(step2)
                                .next(step3)
                                .next(step4)
                                .build();
        }

        /**
         * parentをcategoryテーブルに挿入
         * 
         * @param jobRepository
         * @param transactionManager
         * @param template
         * @param tasklet1
         * @return
         */
        @SuppressWarnings("null")
        @Bean
        @Lazy
        public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                        JdbcTemplate template, InsertParentTasklet tasklet1) {
                return new StepBuilder("step1", jobRepository)
                                .tasklet(tasklet1, transactionManager)
                                .build();
        }

        /**
         * childをcategoryテーブルに挿入
         * 
         * @param jobRepository
         * @param transactionManager
         * @param template
         * @param tasklet2
         * @return
         */
        @SuppressWarnings("null")
        @Bean
        @Lazy
        public Step step2(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                        JdbcTemplate template, InsertChildTasklet tasklet2) {
                return new StepBuilder("step1", jobRepository)
                                .tasklet(tasklet2, transactionManager)
                                .build();
        }

        /**
         * grandchildをcategoryテーブルに挿入
         * 
         * @param jobRepository
         * @param transactionManager
         * @param template
         * @param tasklet3
         * @return
         */
        @SuppressWarnings("null")
        @Bean
        @Lazy
        public Step step3(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                        JdbcTemplate template, InsertGrandChildTasklet tasklet3) {
                return new StepBuilder("step1", jobRepository)
                                .tasklet(tasklet3, transactionManager)
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
        @Lazy
        public Step step4(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                        StepCompletionNotificationListener stepListener, ItemCategoryCheckProcessor processor,
                        ChunkCountingListener listener) {
                return new StepBuilder("Step2", jobRepository)
                                .<ItemsDto, Items>chunk(10000, transactionManager)
                                .listener(stepListener)
                                .listener(listener)
                                .reader(itemReader)
                                .processor(processor)
                                .writer(itemWriter)
                                .build();
        }

}