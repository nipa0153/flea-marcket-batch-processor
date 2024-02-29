// package com.example.CategoryBatch;

// import
// org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
// import org.springframework.batch.item.database.JdbcBatchItemWriter;
// import
// org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import javax.sql.DataSource;
// import com.example.Domain.Category;

// @Configuration
// public class CategoryWriter {

// private static final String INSERT_INTO_PARENT_CATEGORIES = """
// INSERT INTO
// categories (
// name
// )
// SELECT DISTINCT
// parent
// FROM
// temp_categories
// WHERE
// parent IS NOT NULL;
// """;

// private static final String INSERT_INTO_CHILD_CATEGORIES = """
// INSERT INTO
// categories (
// name
// , parent_id
// )
// SELECT DISTINCT
// child
// , c.id
// FROM
// temp_categories AS tc
// JOIN
// categories AS c ON tc.parent = c.name
// WHERE
// child IS NOT NULL;
// """;
// private static final String INSERT_INTO_GRAND_CHILD_CATEGORIES = """
// INSERT INTO
// categories (
// name
// , parent_id
// , name_all
// )
// SELECT DISTINCT
// tc.grandchild
// , c1.id
// , tc.parent || '/' || tc.child || '/' || tc.grandchild
// FROM
// temp_categories AS tc
// JOIN
// categories AS c1 ON tc.child = c1.name
// JOIN
// categories AS c2 ON tc.parent = c2.name
// AND
// c1.parent_id = c2.id
// WHERE
// tc.grandchild IS NOT NULL;
// """;

// private final DataSource dataSource;

// public CategoryWriter(DataSource dataSource) {
// this.dataSource = dataSource;
// }

// /**
// * 親カテゴリの挿入
// *
// * @param dataSource
// * @return
// */
// @Bean
// public JdbcBatchItemWriter<Category> parentCategoryWriter() {
// return new JdbcBatchItemWriterBuilder<Category>()
// .itemSqlParameterSourceProvider(new
// BeanPropertyItemSqlParameterSourceProvider<>())
// .sql(INSERT_INTO_PARENT_CATEGORIES)
// .dataSource(dataSource)
// .build();
// }

// /**
// * 子カテゴリの挿入
// *
// * @param dataSource
// * @return
// */
// @Bean
// public JdbcBatchItemWriter<Category> childCategoryWriter() {
// return new JdbcBatchItemWriterBuilder<Category>()
// .itemSqlParameterSourceProvider(new
// BeanPropertyItemSqlParameterSourceProvider<>())
// .sql(INSERT_INTO_CHILD_CATEGORIES)
// .dataSource(dataSource)
// .build();
// }

// /**
// * 孫カテゴリの挿入
// *
// * @param dataSource
// * @return
// */
// @Bean
// public JdbcBatchItemWriter<Category> grandChildCategoryWriter() {
// return new JdbcBatchItemWriterBuilder<Category>()
// .itemSqlParameterSourceProvider(new
// BeanPropertyItemSqlParameterSourceProvider<>())
// .sql(INSERT_INTO_GRAND_CHILD_CATEGORIES)
// .dataSource(dataSource)
// .build();
// }

// }
