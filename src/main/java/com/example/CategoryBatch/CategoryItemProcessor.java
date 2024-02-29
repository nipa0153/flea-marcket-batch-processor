// package com.example.CategoryBatch;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.springframework.batch.item.ItemProcessor;
// import org.springframework.dao.EmptyResultDataAccessException;
// import org.springframework.jdbc.core.JdbcTemplate;

// import com.example.DTO.CategoryDto;
// import com.example.Domain.Category;
// import com.example.Domain.Original;

// public class CategoryItemProcessor implements ItemProcessor<Original,
// CategoryDto> {

// private final Logger log = LogManager.getLogger(CategoryItemProcessor.class);
// private JdbcTemplate template;

// public CategoryItemProcessor(JdbcTemplate template) {
// this.template = template;
// }

// private static final String SQL = """
// SELECT id FROM originals WHERE name = ?;
// """;

// @Override
// public CategoryDto process(Original original) throws Exception {

// log.debug("処理開始 Originalテーブル", original);
// if (original.getCategoryName() == null) {
// return null;
// }

// String[] parts = original.getCategoryName().split("/");
// CategoryDto categoryDto = new CategoryDto();

// String parent = parts.length > 0 ? parts[0] : null; // 三項演算子 条件式 ? trueの時に返る値
// : falseの時に返る値 if文？
// String child = parts.length > 0 ? parts[1] : null;
// String grandChild = parts.length > 0 ? parts[2] : null;

// categoryDto.setName(grandChild != null ? grandChild : (child != null ? child
// : parent));

// categoryDto.setNameAll(String.join("/", parts));

// Integer parentId = findCategoryIdByName(parent);
// categoryDto.setParentId(parentId);

// log.debug("処理終了 Category分別終了", categoryDto);
// return categoryDto;
// }

// /**
// * 上位カテゴリの検索
// *
// * @param categoryName
// * @return
// */
// private Integer findCategoryIdByName(String categoryName) {
// try {
// Integer categoryId = template.queryForObject(SQL, Integer.class,
// categoryName);
// return categoryId;
// } catch (EmptyResultDataAccessException e) {
// return null;
// }
// }

// }
