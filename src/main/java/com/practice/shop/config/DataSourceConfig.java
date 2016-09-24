package com.practice.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;

    /**
     * Embedded datasource is used for both 'production' and test for convenience.
     * @return embedded database
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript(env.getRequiredProperty("jdbc.initLocation"))
                .addScript(env.getRequiredProperty("jdbc.dataLocation"))
                .ignoreFailedDrops(true)
                .build();
    }
}
