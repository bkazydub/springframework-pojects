package com.shop.springframework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;

    /*@Bean
    @Profile("default")
    public DataSource devDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        return dataSource;
    }*/

    @Bean
    @Profile("default")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(HSQL)
                .addScript(env.getRequiredProperty("jdbc.initLocation"))
                .addScript(env.getRequiredProperty("jdbc.dataLocation"))
                .ignoreFailedDrops(true)
                .build();
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
        			 .addScript(env.getRequiredProperty("jdbc.initLocation"))
                .setType(EmbeddedDatabaseType.HSQL)
                .ignoreFailedDrops(true)
                .build();
    }
}
