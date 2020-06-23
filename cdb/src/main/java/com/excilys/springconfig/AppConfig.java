package com.excilys.springconfig;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "com.excilys")
public class AppConfig {
    @Bean(destroyMethod = "")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig("/hikaricp.properties");
        return new HikariDataSource(config);
    }

    private static ApplicationContext pouet = new AnnotationConfigApplicationContext(AppConfig.class);

    public static ApplicationContext getContext() {
        return pouet;
    }

}
