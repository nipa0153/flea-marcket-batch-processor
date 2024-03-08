package com.example.CategoryBatch.Config.Reader;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;

import com.example.DTO.ItemsDto;

@Configuration
public class ReaderConfig {

    private DataSource dataSource;

    public ReaderConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String SELECT_ORIGINALS = """
                SELECT
                o.name AS o_name,
                o.condition_id AS o_condition_id,
                c.id AS category,
                o.brand AS o_brand,
                o.price AS o_price,
                o.shipping AS o_shipping,
                o.description AS o_description
            FROM
                originals AS o
            JOIN (
                SELECT
                    id,
                    split_part(category_name, '/', 1) || '/' ||
                    split_part(category_name, '/', 2) || '/' ||
                    split_part(category_name, '/', 3) AS category_name_modified
                FROM
                    originals
                ) AS modified_category_names ON o.id = modified_category_names.id
            LEFT JOIN
                categories AS c ON c.name_all = modified_category_names.category_name_modified;
                    """;

    @SuppressWarnings("unused")
    private static final RowMapper<ItemsDto> ITEMS_ROW_MAPPER = (rs, i) -> {
        ItemsDto itemsDto = new ItemsDto();

        itemsDto.setName(rs.getString("o_name"));
        itemsDto.setConditionId(rs.getInt("o_condition_id"));
        itemsDto.setCategory(rs.getInt("category"));
        itemsDto.setBrand(rs.getString("o_brand"));
        itemsDto.setPrice(rs.getDouble("o_price"));
        itemsDto.setShipping(rs.getInt("o_shipping"));
        itemsDto.setDescription(rs.getString("o_description"));

        return itemsDto;
    };

    /**
     * itemsリーダー
     * 
     * @return
     */
    @SuppressWarnings("null")
    @Bean
    @Lazy
    public JdbcCursorItemReader<ItemsDto> itemReader() {
        return new JdbcCursorItemReaderBuilder<ItemsDto>()
                .dataSource(dataSource)
                .name("importData")
                .sql(SELECT_ORIGINALS)
                .rowMapper(ITEMS_ROW_MAPPER)
                .build();
    }

}
