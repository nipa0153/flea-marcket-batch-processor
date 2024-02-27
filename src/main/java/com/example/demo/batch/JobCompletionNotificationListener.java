package com.example.demo.batch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Category;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LogManager.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate template;

    public JobCompletionNotificationListener(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("処理が完了しました！");

            template.query("SELECT name FROM categories", new DataClassRowMapper<>(Category.class))
                    .forEach(category -> log.info("カテゴリ名"));
        }
    }
}
