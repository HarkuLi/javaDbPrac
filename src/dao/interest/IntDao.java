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
	 * @param filter {HashMap<String, String>}
	 * @return {int} total number of rows, and return -1 when the table doesn't exist
	 */
	public int getRowNum(HashMap<String, String> filter) {
		String sqlStr = "select count(id) from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0) sqlStr += " where " + filterStr;
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			
			int idx = 1;
			for(Object param : paramList) {
				pst.setObject(idx, param);
				++idx;
			}
			
			rs = pst.executeQuery();
			
			while(rs.next()) return rs.getInt(1);
		}
		catch(Exception e) {
			System.out.println("Exception in getRowNum: " + e.toString());
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
	
	/**
	 * 
	 * @param filter {HashMap<String, String>}
	 * @return {ArrayList<IntModel>} a list of interest object
	 */
	public ArrayList<IntModel> read(HashMap<String, String> filter) {
		String sqlStr = "select * from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0)
			sqlStr   += " where " + filterStr;
		
		sqlStr		 += " order by name";
		
		ArrayList<IntModel> tableList = new ArrayList<IntModel>();
		
		try {
			//prepare query
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			
			int idx = 1;
			for(Object param : paramList) {
				pst.setObject(idx++, param);
			}
			
			rs = pst.executeQuery();
			
			//read
			while(rs.next()){
				IntModel data = new IntModel();
				data.setId(rs.getString("id"));
				data.setName(rs.getString("name"));
				data.setState(rs.getBoolean("state"));
				tableList.add(data);
			}
		}
		catch(Exception e) {
			System.out.println("Exception in read: " + e.toString());
		}
		finally {
			close();
		}
		
		return tableList;
	}
	
	/**
	 * 
	 * @param filter {HashMap<String, String>}
	 * @param skipNum {int} how many rows to skip
	 * @param readNum {int} how many rows to read
	 * @return {ArrayList<IntModel>} a list of interest object
	 */
	public ArrayList<IntModel> read(HashMap<String, String> filter, int skipNum, int readNum) {
		String sqlStr = "select * from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0)
			sqlStr   += " where " + filterStr;
		
		sqlStr		 += " order by name" +
				        " limit ?,?";
		
		ArrayList<IntModel> tableList = new ArrayList<IntModel>();
		
		try {
			//prepare query
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			
			int idx = 1;
			for(Object param : paramList) {
				pst.setObject(idx++, param);
			}
			pst.setInt(idx++, skipNum);
			pst.setInt(idx++, readNum);
			
			rs = pst.executeQuery();
			
			//read
			while(rs.next()){
				IntModel data = new IntModel();
				data.setId(rs.getString("id"));
				data.setName(rs.getString("name"));
				data.setState(rs.getBoolean("state"));
				tableList.add(data);
			}
		}
		catch(Exception e) {
			System.out.println("Exception in read: " + e.toString());
		}
		finally {
			close();
		}
		
		return tableList;
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
	
	/**
	 * 
	 * @param filter {HashMap<String, String>}
	 * @return {HashMap<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: ArrayList<Object>,
	 *   }
	 */
	private HashMap<String, Object> filterHandle(HashMap<String, String> filter) {
		String queryStr = "";
		ArrayList<Object> paramList = new ArrayList<Object>();
		HashMap<String, Object> rst = new HashMap<String, Object>();
		
		//get filters
		String id = filter.get("id");
		String name = filter.get("name");
		String state = filter.get("state");
		
		if(id != null) {
			queryStr += "id = ?";
			paramList.add(id);
		}
		if(name != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "name like ?";
			paramList.add("%" + name + "%");
		}
		if(state != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "state = ?";
			paramList.add(state.equals("1"));
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
