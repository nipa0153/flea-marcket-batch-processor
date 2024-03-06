package com.example.NotUsed.Config;
// package com.example.ChunkBatch.Config;

// import javax.sql.DataSource;

// import
// org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
// import org.springframework.batch.item.database.JdbcCursorItemReader;
// import
// org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.core.RowMapper;

// import com.example.ChunkBatch.Processor.CategoryItemProcessor;
// import com.example.DTO.CategoryDto;
// import com.example.Domain.Category;

// @EnableBatchProcessing
// @Configuration
// public class ChunkConfig {

// private DataSource dataSource;

// public ChunkConfig(DataSource dataSource) {
// this.dataSource = dataSource;
// }

// // カテゴリテーブルへ挿入する過程
// private static final String SELECT_CATEGORIES = """
// SELECT
// NULLIF(split_part(category_name, '/', 1), '') AS parent,
// NULLIF(split_part(category_name, '/', 2), '') AS child,
// NULLIF(split_part(category_name, '/', 3), '') AS grand_child
// FROM
// originals
// """;

// private static final RowMapper<CategoryDto> CATEGORY_ROW_MAPPER = (rs, i) ->
// {
// CategoryDto categoryDto = new CategoryDto();
// categoryDto.setParent(rs.getString("parent"));
// categoryDto.setChild(rs.getString("child"));
// categoryDto.setGrandChild(rs.getString("grand_child"));
// return categoryDto;
// };

// @SuppressWarnings("null")
// @Bean
// public JdbcCursorItemReader<CategoryDto> itemReader() {
// return new JdbcCursorItemReaderBuilder<CategoryDto>()
// .name("importCategory")
// .sql(SELECT_CATEGORIES)
// .dataSource(dataSource)
// .rowMapper(CATEGORY_ROW_MAPPER)
// .build();

// }

// @Bean
// public CategoryItemProcessor<> itemProcessor() {
// return new CategoryItemProcessor();
// }
// }
