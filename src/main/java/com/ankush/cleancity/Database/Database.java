package com.ankush.cleancity.Database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class Database {
    @Autowired
    DataSource source;

    @Bean
    public JdbcTemplate template() {
        return new JdbcTemplate(source);
    }
}