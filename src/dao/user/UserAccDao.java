package dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import service.ConnectionPool;

public class UserAccDao {
	private ConnectionPool conPool;
	private Connection con;
	private Statement stat;
	private ResultSet rs;
	private PreparedStatement pst;
	private final String tableName = "userAccount";

	public UserAccDao() {
		conPool = new ConnectionPool();
	}
	
	public void create(HashMap<String, Object> newData) {
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (userId, account, password, state)";
		sqlStr 		 += " values (?, ?, ?, ?)";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			pst.setObject(1, newData.get("userId"));
			pst.setObject(2, newData.get("account"));
			pst.setObject(3, newData.get("password"));
			pst.setObject(4, newData.get("state"));
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
	 * @param filter {HashMap<String, Object>}
	 * @return {ArrayList<HashMap<String, Object>>} return a list of account info.
	 */
	public ArrayList<HashMap<String, Object>> read(HashMap<String, Object> filter) {
		String sqlStr = "select *" +
				        " from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<String> paramList = (ArrayList<String>)handledFilter.get("paramList");
		if(filterStr.length() == 0) return null;
		sqlStr   += " where " + filterStr;
		
		
		ArrayList<HashMap<String, Object>> rstList = new ArrayList<HashMap<String, Object>>();
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			
			int idx = 1;
			for(String param : paramList) {
				pst.setString(idx++, param);
			}
			
			rs = pst.executeQuery();
						
			while(rs.next()){
				HashMap<String, Object> acc = new HashMap<String, Object>();
				acc.put("userId", rs.getString("userId"));
				acc.put("account", rs.getString("account"));
				acc.put("password", rs.getString("password"));
				acc.put("state", rs.getBoolean("state"));
				rstList.add(acc);
			}
		}
		catch(Exception e) {
			System.out.println("Exception in read: " + e.toString());
		}
		finally {
			close();
		}
		
		return rstList;
	}
	
	public void update(HashMap<String, Object> setData) {
		String sqlStr = "update " + tableName;

		//handle the data to set
		String userId = (String)setData.get("userId");
		setData.remove("userId");
		HashMap<String, Object> handledSetData = setDataHandle(setData);
		String setDataStr = (String)handledSetData.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<String> paramList = (ArrayList<String>)handledSetData.get("paramList");
		if(setDataStr.length() == 0) return;	//new data is null
		sqlStr += " set " + setDataStr;
		sqlStr += " where userId = ?";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			
			int idx = 1;
			for(Object param : paramList) {
				pst.setObject(idx++, param);
			}
			pst.setString(idx++, userId);
			
			pst.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("Exception in update: " + e.toString());
		}
		finally {
			close();
		}
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
	
	/**
	 * 
	 * @param setData {HashMap<String, Object>}
	 * @return {HashMap<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: ArrayList<Object>,
	 *   }
	 */
	private HashMap<String, Object> setDataHandle(HashMap<String, Object> setData) {
		String queryStr = "";
		ArrayList<Object> paramList = new ArrayList<Object>();
		HashMap<String, Object> rst = new HashMap<String, Object>();
		
		for(String key : setData.keySet()) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += key + " = ?";
			paramList.add(setData.get(key));
		}
		
		rst.put("queryStr", queryStr);
		rst.put("paramList", paramList);
		return rst;
	}
	
	/**
	 * 
	 * @param filter {HashMap<String, Object>}
	 * @return {HashMap<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: ArrayList<Object>,
	 *   }
	 */
	private HashMap<String, Object> filterHandle(HashMap<String, Object> filter) {
		String queryStr = "";
		ArrayList<Object> paramList = new ArrayList<Object>();
		HashMap<String, Object> rst = new HashMap<String, Object>();
		
		//get filters
		for(String key : filter.keySet()) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += key + " = ?";
			paramList.add(filter.get(key));
		}
		
		rst.put("queryStr", queryStr);
		rst.put("paramList", paramList);
		
		return rst;
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