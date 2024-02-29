package com.example.CategoryBatch;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class CategoryTasklet implements Tasklet {

    @Autowired
    private TransactionTemplate transactionTemplate;

    private final JdbcTemplate template;

    public CategoryTasklet(JdbcTemplate template) {
        this.template = template;
    }

    private static final String INSERT_INTO_PARENT_CATEGORIES = """
            INSERT INTO categories (name)
            SELECT DISTINCT parent
            FROM temp_categories
            WHERE parent IS NOT NULL;
            """;

    private static final String INSERT_INTO_CHILD_CATEGORIES = """
            INSERT INTO categories (name, parent_id)
            SELECT DISTINCT child,
                c.id
            FROM temp_categories AS tc
                JOIN categories AS c ON tc.parent = c.name
            WHERE child IS NOT NULL;
            """;
    private static final String INSERT_INTO_GRAND_CHILD_CATEGORIES = """
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
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        return transactionTemplate.execute(status -> {
            try {
                // 処理開始時間
                Long start = System.currentTimeMillis();

                log.info("INSERT_INTO_CHILD_CATEGORIES　を実行します。");
                int parentUpdateCount = template.update(INSERT_INTO_PARENT_CATEGORIES);
                log.info("INSERT_INTO_CHILD_CATEGORIES　が完了しました。　更新件数：" + parentUpdateCount);

                log.info("INSERT_INTO_CHILD_CATEGORIES　を実行します。");
                int childUpdateCount = template.update(INSERT_INTO_CHILD_CATEGORIES);
                log.info("INSERT_INTO_CHILD_CATEGORIES　が完了しました。　更新件数：" + childUpdateCount);

                log.info("INSERT_INTO_GRAND_CHILD_CATEGORIES　を実行します。");
                int grandChildUpdateCount = template.update(INSERT_INTO_GRAND_CHILD_CATEGORIES);
                log.info("INSERT_INTO_GRAND_CHILD_CATEGORIES　が完了しました。　更新件数：" + grandChildUpdateCount);

                // 処理終了時間
                Long end = System.currentTimeMillis();

                Long jobTime = end - start;

                Double jobTimeInSecond = jobTime / 1000.0;

                log.info("処理時間は{}でした。", jobTimeInSecond);

                log.info("categoriesの確認。");
                List<String> insertedParents = template.queryForList(
                        "SELECT * FROM categories", String.class);
                insertedParents.forEach(parentName -> log.info("挿入されたcategory: {}", parentName));

                return RepeatStatus.FINISHED;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("CategoryTasklet の実行中にエラーが発生しました", e);
                throw new RuntimeException(e);
            }
        });
    }
}
