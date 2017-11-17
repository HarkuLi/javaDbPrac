package com.harku.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = {"com.harku.service", "com.harku.dao"})
public class AppConfig {
	@Bean
	//@Scope("singleton") //default
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(ConstantConfig.DB_DRIVER_NAME);
		dataSource.setUrl(ConstantConfig.DB_URL);
		dataSource.setUsername(ConstantConfig.DB_USERNAME);
		dataSource.setPassword(ConstantConfig.DB_PASSWORD);
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
