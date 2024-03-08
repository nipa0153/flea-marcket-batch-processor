
package com.example.CategoryBatch.Config.Writer;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.example.Domain.Items;

@Configuration
public class WriteConfig {

    private DataSource dataSource;

    public WriteConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * items移行用クエリ
     */

    private static final String INSERT_INTO_ITEMS = """
            INSERT INTO items (
                name
                , condition_id
                , category
                , brand
                , price
                , shipping
                , description
                )
            VALUES (
                :name
                , :conditionId
                , :category
                , :brand
                , :price
                , :shipping
                , :description
            );
                        """;

    /**
     * itemsライター
     *
     * @param dataSource
     * @return
     */
    @SuppressWarnings("null")
    @Bean
    @Lazy
    public JdbcBatchItemWriter<Items> itemWriter() {
        return new JdbcBatchItemWriterBuilder<Items>()
                .sql(INSERT_INTO_ITEMS)
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

}
