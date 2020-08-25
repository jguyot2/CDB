package com.excilys.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.excilys.persistence.config.PersistenceConfig;
import com.excilys.serviceconfig.ServiceConfig;

public class MvcInit implements WebApplicationInitializer {

	private static AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

	public static AnnotationConfigWebApplicationContext getContext() {
		return MvcInit.ctx;
	}

	@Override
	public void onStartup(final ServletContext container) throws ServletException {
		MvcInit.ctx.register(ControllerConfig.class);
		MvcInit.ctx.register(ServiceConfig.class);
		MvcInit.ctx.register(PersistenceConfig.class);
		MvcInit.ctx.register(SecurityConfig.class);
		MvcInit.ctx.setServletContext(container);
		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(MvcInit.ctx));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
	}

}
