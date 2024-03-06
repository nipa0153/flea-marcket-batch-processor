package com.example.CategoryBatch.Config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class DataSourceConfig {

    @Bean
    @Lazy
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/postgres");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("postgres");
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        return dataSourceBuilder.build();
    }

}
