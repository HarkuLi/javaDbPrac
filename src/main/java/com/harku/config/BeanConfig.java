package com.harku.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.harku.dao.interest.IntDao;
import com.harku.dao.occ.OccDao;
import com.harku.dao.user.UserAccDao;
import com.harku.dao.user.UserIntDao;
import com.harku.dao.user.UsersDao;
import com.harku.service.interest.IntService;
import com.harku.service.occ.OccService;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UserIntService;
import com.harku.service.user.UsersService;

@Configuration
public class BeanConfig {
	@Bean
	//@Scope("singleton") //default
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://127.0.0.1/db1?useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("1234,Qwer");
		return dataSource;
	}
	
	/////////
	// dao //
	/////////
	
	@Bean
	public UsersDao usersDao() {
		return new UsersDao(dataSource());
	}
	
	@Bean
	public UserIntDao userIntDao() {
		return new UserIntDao(dataSource());
	}
	
	@Bean
	public UserAccDao userAccDao() {
		return new UserAccDao(dataSource());
	}
	
	@Bean
	public IntDao intDao() {
		return new IntDao(dataSource());
	}
	
	@Bean
	public OccDao occDao() {
		return new OccDao(dataSource());
	}
	
	/////////////
	// service //
	/////////////
	
	@Bean
	public UsersService usersService() {
		return new UsersService(usersDao(), userIntService(), userAccService());
	}
	
	@Bean
	public UserIntService userIntService() {
		return new UserIntService(userIntDao());
	}
	
	@Bean
	public UserAccService userAccService() {
		return new UserAccService(userAccDao());
	}
	
	@Bean
	public IntService intService() {
		return new IntService(intDao());
	}
	
	@Bean
	public OccService occService() {
		return new OccService(occDao());
	}
}
