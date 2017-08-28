package dao.interest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import model.interest.IntModel;
import service.ConnectionPool;

public class IntDao {
	private ConnectionPool conPool;
	private Connection con;
	private Statement stat;
	private ResultSet rs;
	private PreparedStatement pst;
	private final String tableName = "interest";

	public IntDao() {
		conPool = new ConnectionPool();
	}
	
	/**
	 * 
	 * @return total number of rows, and return -1 when the table doesn't exist
	 */
	public int getRowNum() {
		String sqlStr = "select count(id) from " + tableName;
		
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
	
	/**
	 * @param where {String} mySQL query string of where
	 * @return total number of rows, and return -1 when the table doesn't exist
	 */
	public int getRowNum(String where) {
		String sqlStr = "select count(id) from " + tableName;
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
	
	public void create(HashMap<String, Object> newData) {
		String id = UUID.randomUUID().toString();
		String sqlStr = "insert into " + tableName;
		sqlStr		 += " (id, name, state)";
		sqlStr       += " values (?, ?, ?)";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, id);
			pst.setString(2, (String) newData.get("name"));
			pst.setBoolean(3, (boolean) newData.get("state"));
			pst.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Exception in create: " + e.toString());
		}
		finally {
			close();
		}
	}
	
	public ArrayList<IntModel> read(String sel, String where, String limit) {
		String sqlStr = "select " + sel +
				        " from " + tableName;
		if(where != null && where.length() != 0) {
			sqlStr   += " where " + where;
		}
		sqlStr		 += " order by name" +
				        " limit " + limit;
		
		ArrayList<IntModel> tableList = new ArrayList<IntModel>();
		
		try {
			con = conPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(sqlStr);
						
			while(rs.next()){
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("id", rs.getString("id"));
				data.put("name", rs.getString("name"));
				data.put("state", rs.getBoolean("state"));
				tableList.add(new IntModel(data));
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
	
	public void update(HashMap<String, Object> data) {
		String sqlStr = "update " + tableName +
						" set name = ?, state = ?" +
						" where id = ?";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, (String) data.get("name"));
			pst.setBoolean(2, (boolean) data.get("state"));
			pst.setString(3, (String) data.get("id"));
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
		String sqlStr = "delete from " + tableName +
						" where id = ?";
				
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, id);
			pst.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Exception in delete: " + e.toString());
		}
		finally {
			close();
		}
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
