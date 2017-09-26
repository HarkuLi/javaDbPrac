package bean;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import dao.interest.IntDao;
import dao.occ.OccDao;
import dao.user.UserAccDao;
import dao.user.UserIntDao;
import dao.user.UsersDao;
import service.interest.IntService;
import service.occ.OccService;
import service.user.UserAccService;
import service.user.UserIntService;
import service.user.UsersService;

@Configuration
public class Config {
	@Bean
	//@Scope("singleton") //default
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
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
