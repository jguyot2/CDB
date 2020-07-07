package com.excilys.ui.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.persistence.config.PersistenceConfig;
import com.excilys.serviceconfig.ServiceConfig;

@Configuration
@ComponentScan(basePackages = "com.excilys.ui")
public class UiConfig {

    private static ApplicationContext ctx = new AnnotationConfigApplicationContext(UiConfig.class, ServiceConfig.class,
            PersistenceConfig.class);

    public static ApplicationContext getContext() {
        return ctx;
    }
}
