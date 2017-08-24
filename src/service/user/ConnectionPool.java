package service.user;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import java.sql.Connection;

public class ConnectionPool {
	//static for reuses
	private static DataSource datasource;
	
	public ConnectionPool() {
		if(datasource != null) return;
		System.out.println("new datasource");
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:mysql://127.0.0.1/db1?useSSL=false");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("root");
        p.setPassword("1234,Qwer");
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(3);
        p.setMaxWait(5);
        p.setRemoveAbandonedTimeout(5);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(3);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(p);
	}
	
	public Connection getConnection() {
		try {
			Connection con = datasource.getConnection();
			return con;
		}
		catch(Exception e) {
			System.out.println("Exception in connection: " + e.toString());
		}
		return null;
	}
}













