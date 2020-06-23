package com.excilys.springconfig;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "com.excilys")
public class AppConfig {
    private static DataSource ds;

    private DataSource getDataSource() {
        if (ds == null) {
            HikariConfig config = new HikariConfig("/hikaricp.properties");
            ds = new HikariDataSource(config);
        }
        return ds;
    }

    @Bean(destroyMethod = "")
    public DataSource dataSource() {
        return getDataSource();
    }

    @Bean
    public JdbcTemplate template() {
        return new JdbcTemplate(getDataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    private static ApplicationContext pouet = new AnnotationConfigApplicationContext(AppConfig.class);

    public static ApplicationContext getContext() {
        return pouet;
    }

}
