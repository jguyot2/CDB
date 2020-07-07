package com.excilys.serviceconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.excilys.service", "com.excilys.adapters", "com.excilys.persistence" })
public class ServiceConfig {

}
