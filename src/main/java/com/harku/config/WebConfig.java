package com.harku.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.harku.aspect.AspectLogging;
import com.harku.interceptor.SignInInterceptor;

@Configuration
@Import(AppConfig.class)
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.harku.controller", "com.harku.aspect", "com.harku.interceptor", "com.harku.validator"})
public class WebConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private SignInInterceptor signInInterceptor;
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver
			= new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
	//aspect for logging
	@Bean
	public AspectLogging aspectLogging() {
		return new AspectLogging();
	}
	
	//for messages.properties (i18n)
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("messages");
		return source;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver clr = new CookieLocaleResolver();
		clr.setDefaultLocale(ConstantConfig.DEFAULT_LOCALE);
		clr.setCookieName(ConstantConfig.LOCALE_COOKIE_NAME);
		return clr;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(signInInterceptor)
				.addPathPatterns("/user/*", "/occupation/*", "/interest/*");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registry.addResourceHandler("/js/*").addResourceLocations("/js/");
		registry.addResourceHandler("/css/*").addResourceLocations("/css/");
		registry.addResourceHandler("/*.png").addResourceLocations("/image/");
		registry.addResourceHandler("/resources/*.properties").addResourceLocations("classpath:/");
	}
	
}
