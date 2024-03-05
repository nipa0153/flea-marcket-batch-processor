package com.example.CategoryBatch.Tasklet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class CategoryTasklet implements Tasklet {

    private TransactionTemplate transactionTemplate;

    private final JdbcTemplate template;

    public CategoryTasklet(JdbcTemplate template, TransactionTemplate transactionTemplate) {
        this.template = template;
        this.transactionTemplate = transactionTemplate;
    }

    private static final String INSERT_INTO_CATEGORIES = """
            -- 1階層目（親）のカテゴリを挿入
            INSERT INTO categories (name)
            SELECT DISTINCT parent
            FROM temp_categories
            WHERE parent IS NOT NULL;
            -- 2階層目（子）のカテゴリを挿入
            -- 親カテゴリが存在するものだけを選択
            INSERT INTO categories (name, parent_id)
            SELECT DISTINCT child,
                c.id
            FROM temp_categories AS tc
                JOIN categories AS c ON tc.parent = c.name
            WHERE child IS NOT NULL;
            -- 3階層目（孫）のカテゴリを挿入
            -- 子カテゴリと親カテゴリが存在するものだけを選択
            INSERT INTO categories (name, parent_id, name_all)
            SELECT DISTINCT tc.grandchild,
                c1.id,
                tc.parent || '/' || tc.child || '/' || tc.grandchild
            FROM temp_categories AS tc
                JOIN categories AS c1 ON tc.child = c1.name
                JOIN categories AS c2 ON tc.parent = c2.name
                AND c1.parent_id = c2.id
            WHERE tc.grandchild IS NOT NULL;

                        """;

    private static final Logger log = LogManager.getLogger(CategoryTasklet.class);

    @Override
    public RepeatStatus execute(@SuppressWarnings("null") StepContribution contribution,
            @SuppressWarnings("null") ChunkContext chunkContext) {
        return transactionTemplate.execute(status -> {
            try {
                // 処理開始時間
                Long start = System.currentTimeMillis();

                log.info("categoriesテーブルへの挿入を開始します");
                int updateCount = template.update(INSERT_INTO_CATEGORIES);
                log.info("categoriesテーブルへの挿入が完了しました　更新件数：" + updateCount);

                // 処理終了時間
                Long end = System.currentTimeMillis();

                Long jobTime = end - start;

                Double jobTimeInSecond = jobTime / 1000.0;

                log.info("処理時間は{}秒です", jobTimeInSecond);

                return RepeatStatus.FINISHED;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("CategoryTasklet の実行中にエラーが発生しました", e);
                throw new RuntimeException(e);
            }
        });
    }
}