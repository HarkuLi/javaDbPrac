package dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import service.ConnectionPool;

public class UserIntDao {
	private ConnectionPool conPool;
	private Connection con;
	private Statement stat;
	private ResultSet rs;
	private PreparedStatement pst;
	private final String tableName = "userInterest";

	public UserIntDao() {
		conPool = new ConnectionPool();
	}
	
	public void create(String id, String interest) {
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (id, interest)";
		sqlStr 		 += " values (?, ?)";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, id);
			pst.setString(2, interest);
			pst.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Exception in create: " + e.toString());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 
	 * @param id {String} user's id
	 * @return {ArrayList<String>} return a list of interest id of the user
	 */
	public ArrayList<String> read(String id) {
		String sqlStr = "select interest" +
				        " from " + tableName +
			            " where id = ?";
		
		ArrayList<String> rstList = new ArrayList<String>();
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, id);
			rs = pst.executeQuery();
						
			while(rs.next()){
				rstList.add(rs.getString("interest"));
			}
			return rstList;
		}
		catch(Exception e) {
			System.out.println("Exception in read: " + e.toString());
		}
		finally {
			close();
		}
		
		return null;
	}
//	
//	public void update(HashMap<String, Object> newData) {
//		String sqlStr = "update users " +
//						"set name = ?, age = ?, birth = ? ";
//		String photoName = (String) newData.get("photo");
//		if(photoName != null) {
//			sqlStr   += ", photo_name = '" + photoName + "' ";
//		}
//		sqlStr       += "where id = ?";
//		
//		try {
//			DateFormat sdf = new SimpleDateFormat(datePattern);
//			//note: the type Date here is java.sql.date
//			//      but sdf.parse(String) returns java.util.date
//			Date birthDate = new Date(sdf.parse((String)newData.get("birth")).getTime());
//			
//			con = conPool.getConnection();
//			pst = con.prepareStatement(sqlStr);
//			pst.setString(1, (String)newData.get("name"));
//			pst.setInt(2, (int)newData.get("age"));
//			pst.setDate(3, birthDate);
//			pst.setString(4, (String)newData.get("id"));
//			pst.executeUpdate();
//		}
//		catch(Exception e) {
//			System.out.println("Exception in update: " + e.toString());
//		}
//		finally {
//			close();
//		}
//	}
//	
//	public void delete(String id) {
//		String sqlStr = "delete from users" +
//						" where id = ?";
//		
//		try {
//			con = conPool.getConnection();
//			pst = con.prepareStatement(sqlStr);
//			pst.setString(1, id);
//			pst.executeUpdate();
//		}
//		catch(Exception e) {
//			System.out.println("Exception in delete: " + e.toString());
//		}
//		finally {
//			close();
//		}
//	}
	
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
