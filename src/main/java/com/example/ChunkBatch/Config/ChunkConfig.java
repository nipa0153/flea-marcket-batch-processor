// package com.example.ChunkBatch.Config;

// import javax.sql.DataSource;

// import org.springframework.batch.item.database.JdbcCursorItemReader;
// import
// org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.core.RowMapper;

// import com.example.Domain.Category;

// @Configuration
// public class ChunkConfig {

// private DataSource dataSource;

// public ChunkConfig(DataSource dataSource) {
// this.dataSource = dataSource;
// }

// private static final String SELECT_CATEGORIES = """
// SELECT
// category_name
// FROM
// originals
// ORDER BY
// id ASC;
// """;

// private static final RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, i) -> {
// Category category = new Category();
// category.set
// }

// public JdbcCursorItemReader<Category> itemReader() {
// return new JdbcCursorItemReaderBuilder<Category>()
// .name("importCategory")
// .sql(SELECT_CATEGORIES)
// .dataSource(dataSource)
// .rowMapper()
// .build();

// }
// }
