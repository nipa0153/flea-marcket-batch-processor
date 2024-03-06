package com.example.NotUsed.junk;
// package com.example.ChunkBatch;
// package com.example.CategoryBatch.Config.Writer;

// import javax.sql.DataSource;

// import org.springframework.batch.item.Chunk;
// import org.springframework.batch.item.ItemWriter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Lazy;
// import org.springframework.jdbc.core.JdbcTemplate;

// import com.example.Domain.Items;

// @Configuration
// public class CustomItemWriter implements ItemWriter<Items> {

// private DataSource dataSource;
// private JdbcTemplate template;

// public CustomItemWriter(@Lazy DataSource dataSource) {
// this.dataSource = dataSource;
// this.template = new JdbcTemplate(dataSource);
// }

// /**
// * items移行用クエリ
// */

// private static final String INSERT_INTO_ITEMS = """
// INSERT INTO items (
// name,
// condition,
// category,
// brand,
// price,
// shipping,
// description
// )
// SELECT o.name,
// o.condition_id,
// cm.category_id,
// o.brand,
// o.price,
// o.shipping,
// o.description
// FROM originals AS o
// LEFT JOIN category_mapping AS cm ON o.id = cm.originals_id;
// """;

// @Override
// public void write(Chunk<? extends Items> items) throws Exception {
// template.execute(INSERT_INTO_ITEMS);
// }
// }
