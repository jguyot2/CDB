package com.excilys.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet permettant l'initialisation de log4j
 *
 * @author jguyot2
 *
 */
public class Log4jInitListener implements ServletContextListener {
	@Override
	public void contextInitialized(final ServletContextEvent cte) {
		try (InputStream os = cte.getServletContext().getResourceAsStream("/WEB-INF/classes/log4j.properties");) {
			Properties properties = new Properties();
			properties.load(os);
			PropertyConfigurator.configure(properties);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
