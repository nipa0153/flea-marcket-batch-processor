package com.example.ChunkBatch.junk;
// package com.example.ChunkBatch;
// package com.example.CategoryBatch.Config.Listener;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.springframework.batch.core.ExitStatus;
// import org.springframework.batch.core.StepExecution;
// import org.springframework.batch.core.StepExecutionListener;
// import org.springframework.dao.DataAccessException;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Component;

// @Component
// public class CategoryMappingStepListener implements StepExecutionListener {

// private static final Logger log =
// LogManager.getLogger(CategoryMappingStepListener.class);

// private final JdbcTemplate template;

// public CategoryMappingStepListener(JdbcTemplate template) {
// this.template = template;
// }

// /**
// * カテゴリマッピングテーブル作成・削除のクエリ
// */
// private static final String CREATE_CATEGORY_MAPPING = """
// CREATE TEMP TABLE IF NOT EXISTS category_mapping AS
// SELECT
// o.id AS o_id
// , c.id AS c_id
// FROM
// temp_categories AS tc
// JOIN
// originals AS o ON tc.id = o.id
// JOIN
// categories AS c ON (
// tc.parent || '/' || tc.child || '/' || tc.grandchild = c.name_all
// OR
// o.category_name IS NULL
// );
// """;

// /**
// * カテゴリマッピングテーブル作成
// *
// * @param jobExecution
// */
// @Override
// public void beforeStep(@SuppressWarnings("null") StepExecution stepExecution)
// {
// try {
// template.execute(CREATE_CATEGORY_MAPPING);
// log.info("カテゴリマッピングテーブルを作成します");
// } catch (DataAccessException e) {
// log.error("itemsテーブルへの移行作業中にエラーが発生しました", e);
// }
// }

// /**
// * カテゴリマッピングテーブル削除
// */
// @Override
// public ExitStatus afterStep(@SuppressWarnings("null") StepExecution
// stepExecution) {
// template.execute(DROP_CATEGORY_MAPPING);
// log.info("カテゴリマッピングテーブルを削除しました");

// return stepExecution.getExitStatus();
// }

// }
