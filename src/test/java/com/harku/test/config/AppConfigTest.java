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
		prepareTables(jdbcObj);
		
		return jdbcObj;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	private void prepareTables(JdbcTemplate jdbcObj) {
		//"user" table
		String sqlStr = "create table user(";
		sqlStr += "id varchar(40) not null, ";
		sqlStr += "name varchar(40) not null, ";
		sqlStr += "age int not null, ";
		sqlStr += "birth date not null, ";
		sqlStr += "photo_name varchar(100), ";
		sqlStr += "occupation varchar(40) not null, ";
		sqlStr += "primary key (id));";
		jdbcObj.execute(sqlStr);
		
		//"userInterest" table
		sqlStr  = "create table userInterest(";
		sqlStr += "id varchar(40), ";
		sqlStr += "interest varchar(40));";
		jdbcObj.execute(sqlStr);
		
		//"userAccount" table
		sqlStr  = "create table userAccount(";
		sqlStr += "id varchar(40) not null, ";
		sqlStr += "account varchar(32), ";
		sqlStr += "password varchar(60), ";
		sqlStr += "state varchar(2), ";
		sqlStr += "sign_in_time bigint, ";
		sqlStr += "token varchar(100), ";
		sqlStr += "unique (account), ";
		sqlStr += "primary key (id));";
		jdbcObj.execute(sqlStr);
		
		//"occupation" table
		sqlStr  = "create table occupation(";
		sqlStr += "id varchar(40) not null, ";
		sqlStr += "name varchar(40) not null, ";
		sqlStr += "state varchar(2) not null, ";
		sqlStr += "primary key (id));";
		jdbcObj.execute(sqlStr);
		
		//"interest" table
		sqlStr  = "create table interest(";
		sqlStr += "id varchar(40) not null, ";
		sqlStr += "name varchar(40) not null, ";
		sqlStr += "state boolean not null, ";
		sqlStr += "primary key (id));";
		jdbcObj.execute(sqlStr);
	}
}
