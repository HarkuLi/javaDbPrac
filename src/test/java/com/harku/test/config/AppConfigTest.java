package com.harku.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.harku.config.AppConfig;

@Configuration
@Import(AppConfig.class)
public class AppConfigTest {
	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		dataSource.setUrl("jdbc:hsqldb:mem:myDb");
		dataSource.setUsername("sa");
		dataSource.setPassword("sa");
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbcObj = new JdbcTemplate(dataSource());
		
		//prepare the table to be tested
		String sqlStr = "create table users(";
		sqlStr += "id varchar(40) not null, ";
		sqlStr += "name varchar(40) not null, ";
		sqlStr += "age int not null, ";
		sqlStr += "birth date not null, ";
		sqlStr += "photoName varchar(100), ";
		sqlStr += "occupation varchar(40) not null, ";
		sqlStr += "primary key (id));";
		jdbcObj.execute(sqlStr);
		
		return jdbcObj;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
