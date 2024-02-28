package com.example.CategoryBatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LogManager.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate template;

    public JobCompletionNotificationListener(JdbcTemplate template) {
        this.template = template;
    }

    private static final String CREATE_TEMP_TABLE = """
            CREATE TEMP TABLE IF NOT EXISTS temp_categories AS
            SELECT NULLIF(split_part(category_name, '/', 1), '') AS parent,
                NULLIF(split_part(category_name, '/', 2), '') AS child,
                NULLIF(split_part(category_name, '/', 3), '') AS grandchild
            FROM originals
            WHERE category_name IS NOT NULL;
            """;

    private static final String DROP_TEMP_TABLE = """
            DROP TABLE temp_categories;
            """;

    /**
     * 一時テーブル作成・挿入
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        template.execute(CREATE_TEMP_TABLE);
        log.info("一時テーブルを作成し、データを加工して挿入しました。");
    }

    /**
     * 一時テーブル処理完了
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("処理が完了しました！");

            template.execute(DROP_TEMP_TABLE);
            log.info("一時テーブルを削除します。");
        }
    }
}