<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import = "java.sql.*, java.util.Properties" %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%= "hello world" %>
	<%!
		private static final String dbClassName = "com.mysql.jdbc.Driver";
		private static final String CONNECTION = "jdbc:mysql://127.0.0.1/db1";
	%>
	<%
		//System.out.println(dbClassName);
		// Class.forName(xxx) loads the jdbc classes and
		// creates a drivermanager class factory
		Class.forName(dbClassName);
		
		// Properties for user and password. Here the user and password are both 'paulr'
		Properties p = new Properties();
		p.put("user","root");
		p.put("password","1234,Qwer");
		
		// Now try to connect
		Connection c = DriverManager.getConnection(CONNECTION,p);
		
		Statement stat;
		ResultSet rs;
		PreparedStatement pst;
		String sqlStr = "select * from users";

		stat = c.createStatement();
		rs = stat.executeQuery(sqlStr);
	%>
	<table>
		<tr>
			<th>id</th>
			<th>name</th>
			<th>age</th>
			<th>birth</th>
		</tr>
	<% while(rs.next()){ %>
		<tr>
			<td><%= rs.getInt("id") %></td>
			<td><%= rs.getString("name") %></td>
			<td><%= rs.getInt("age") %></td>
			<td><%= rs.getString("birth") %></td>
		</tr>
	<% } %>
	</table>
	<%
		//System.out.println("It works !");
		c.close();
	%>
</body>
<style>
	table, td, th{
		/*border-collapse: collapse;*/
		border: 1px solid black;
	}
</style>
</html>