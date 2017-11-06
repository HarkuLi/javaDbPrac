package com.harku.test.config;

import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class ViewResolverTest {
	static public ViewResolver genResolver() {
		InternalResourceViewResolver resolver
			= new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
}
