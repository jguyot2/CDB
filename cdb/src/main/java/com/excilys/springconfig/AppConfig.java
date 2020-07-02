package com.excilys.springconfig;

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
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.excilys")
public class AppConfig implements WebMvcConfigurer {
    private DataSource ds;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    private static AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

    public static AnnotationConfigWebApplicationContext getContext() {
        return ctx;
    }

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

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
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
