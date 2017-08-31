package dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
	
	public void create(String userId, String interest) {
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (userId, interest)";
		sqlStr 		 += " values (?, ?)";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, userId);
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
	 * @param userId {String} user's id
	 * @return {ArrayList<String>} return a list of interest id of the user
	 */
	public ArrayList<String> read(String userId) {
		String sqlStr = "select interest" +
				        " from " + tableName +
			            " where userId = ?";
		
		ArrayList<String> rstList = new ArrayList<String>();
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, userId);
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

	public void delete(String userId) {
		String sqlStr = "delete from " + tableName +
						" where userId = ?";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setString(1, userId);
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
