package com.savely.socksapp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
public class TestConfiguration {


    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:15.3");
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        dataSource.setUsername(postgreSQLContainer().getUsername());
        return dataSource;
    }
}
