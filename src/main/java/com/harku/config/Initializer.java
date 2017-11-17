package com.harku.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Initializer implements WebApplicationInitializer{
	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		
		//initialize the WebConfig
		final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(WebConfig.class);
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		//multipart config
		MultipartConfigElement multipartConfigElement
			= new MultipartConfigElement(
					null,
					ConstantConfig.MAX_UPLOAD_SIZE,
					ConstantConfig.MAX_UPLOAD_SIZE * 2,
					ConstantConfig.MAX_UPLOAD_SIZE * 2
			);
		
		//register the DispatcherServlet
		final ServletRegistration.Dynamic appServlet =
			servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
		appServlet.setAsyncSupported(true);
		appServlet.setMultipartConfig(multipartConfigElement);
		appServlet.setLoadOnStartup(1);
		appServlet.addMapping("/");
	}

}
