package com.excilys.persistence.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.DefaultJpaDialect;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "com.excilys.persistence")
public class PersistenceConfig {
    private DataSource ds;

    @Bean(destroyMethod = "")
    public DataSource dataSource() {
        return getDataSource();
    }

    private DataSource getDataSource() {
        if (this.ds == null) {
            HikariConfig config = new HikariConfig("/hikaricp.properties");
            this.ds = new HikariDataSource(config);
        }
        return this.ds;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    @Bean
    public JdbcTemplate template() {
        return new JdbcTemplate(getDataSource());
    }

    @Bean(destroyMethod = "")
    public EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    @Bean(destroyMethod = "")
    public EntityManagerFactory getEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean fb = new LocalContainerEntityManagerFactoryBean();

        fb.setDataSource(dataSource());
        fb.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        fb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        fb.setPackagesToScan("com.excilys.model");
        fb.setJpaDialect(new DefaultJpaDialect());
        fb.afterPropertiesSet();
        return fb.getNativeEntityManagerFactory();
    }
}
