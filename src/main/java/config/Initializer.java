package config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import controller.filter.SignInFilter;

public class Initializer implements WebApplicationInitializer{

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		
		//register filters
		final FilterRegistration filterReg = servletContext.addFilter("signInFilter", SignInFilter.class);
		filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/user/*", "/occ/*", "/interest/*");
		
		//initialize the ServletConfig
		final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(ServletConfig.class);
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		//register the DispatcherServlet
		final ServletRegistration.Dynamic appServlet =
			servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
		appServlet.setAsyncSupported(true);
		appServlet.setLoadOnStartup(1);
		appServlet.addMapping("/");
	}

}
