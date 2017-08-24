package dao.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.user.UsersModel;
import service.user.ConnectionPool;

public class UsersDao{
	private ConnectionPool conPool;
	private Connection con;
	private Statement stat;
	private ResultSet rs;
	private PreparedStatement pst;
	private final String datePattern = "yyyy-MM-dd";

	public UsersDao() {
		conPool = new ConnectionPool();
	}
	
	/**
	 * 
	 * @return total number of rows, and return -1 when the table doesn't exist
	 */
	public int getRowNum() {
		String sqlStr = "select count(id) from users";
		
		try {
			con = conPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			
			while(rs.next()) return rs.getInt(1);
		}
		catch(Exception e) {
			System.out.println("Exception in getRowNum.");
		}
		finally {
			close();
		}
		
		return -1;
	}
	
	public int getRowNum(String where) {
		String sqlStr = "select count(id) from users";
		sqlStr += " where " + where;
		
		try {
			con = conPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
			
			while(rs.next()) return rs.getInt(1);
		}
		catch(Exception e) {
			System.out.println("Exception in getRowNum.");
		}
		finally {
			close();
		}
		
		return -1;
	}
	
	public void create(String name, String age, String birth) {
		int id = getMaxId()+1;
		String sqlStr = "insert into users (id, name, age, birth)";
		sqlStr += " values ('" + id + "', '" + name + "', " + age + ", '" + birth + "')";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Exception in create: " + e.toString());
		}
		finally {
			close();
		}
	}
	
	public ArrayList<UsersModel> read(String sel) {
		String sqlStr = "select " + sel + " from users";
		ArrayList<UsersModel> tableList = new ArrayList<UsersModel>();
		
		System.out.println("sqlStr: " + sqlStr);
		
		try {
			con = conPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
						
			while(rs.next()){
				tableList.add(new UsersModel(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getInt("age"),
						rs.getDate("birth")
					)
				);
			}
			return tableList;
		}
		catch(Exception e) {
			System.out.println("Exception in read: " + e.toString());
		}
		finally {
			close();
		}
		
		return null;
	}
	
	public ArrayList<UsersModel> read(String sel, String where, String limit) {
		String sqlStr = "select " + sel + " from users where " + where + " limit " + limit;
		ArrayList<UsersModel> tableList = new ArrayList<UsersModel>();
		
		try {
			con = conPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
						
			while(rs.next()){
				tableList.add(new UsersModel(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getInt("age"),
						rs.getDate("birth")
					)
				);
			}
			return tableList;
		}
		catch(Exception e) {
			System.out.println("Exception in read: " + e.toString());
		}
		finally {
			close();
		}
		
		return null;
	}
	
	public void update(String id, String name, String age, String birth) {
		String sqlStr = "update users " +
						"set name = ?, age = ?, birth = ? " +
						"where id = ?";
		
		try {
			DateFormat sdf = new SimpleDateFormat(datePattern);
			//note: the type Date here is java.sql.date
			//but sdf.parse(String) returns java.util.date
			Date birthDate = new Date(sdf.parse(birth).getTime());
			
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, name);
			pst.setInt(2, Integer.parseInt(age));
			pst.setDate(3, birthDate);
			pst.setInt(4, Integer.parseInt(id));
			pst.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Exception in update: " + e.toString());
		}
		finally {
			close();
		}
	}
	
	public void delete(String id) {
		String sqlStr = "delete from users " +
						"where id = ?";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setInt(1, Integer.parseInt(id));
			pst.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Exception in delete: " + e.toString());
		}
		finally {
			close();
		}
	}
	
	private int getMaxId() {
		String sqlStr = "select max(id) from users";
		
		try {
			con = conPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
						
			while(rs.next()){
				return rs.getInt(1);
			}
		}
		catch(Exception e) {
			System.out.println("Exception in getMaxId: " + e.toString());
		}
		finally {
			close();
		}
		
		return 0;
	}
	
	private void close() {
		try {
			if(stat != null) {
				stat.close();
				stat = null;
			}
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(pst != null) {
				pst.close();
				pst = null;
			}
			if(con != null) {
				con.close();
				con = null;
			}
		}
		catch(Exception e) {
			System.out.println("Exception in close: " + e.toString());
		}
	}
}