package com.excilys.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.excilys.persistence.config.PersistenceConfig;
import com.excilys.serviceconfig.ServiceConfig;

public class MvcInit implements WebApplicationInitializer {

	@Override
	public void onStartup(final ServletContext container) throws ServletException {
		AnnotationConfigWebApplicationContext ctx = getContext();
		ctx.register(ServiceConfig.class);
		ctx.register(PersistenceConfig.class);
		ctx.register(SecurityConfig.class);
		ctx.setServletContext(container);
		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		
	}

	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("com.excilys.config");
		return context;
	}

}
