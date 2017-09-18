package dao.occ;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.occ.OccModel;
import service.ConnectionPool;

public class OccDao {
	private static Logger log = LoggerFactory.getLogger(OccDao.class);
	private ConnectionPool conPool;
	private Connection con;
	private Statement stat;
	private ResultSet rs;
	private PreparedStatement pst;
	private final String tableName = "occ";

	public OccDao() {
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
			log.error("In getRowNum: {}", e.toString());
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
			log.error("In create: {}", e.toString());
		}
		finally {
			close();
		}
	}
	
	/**
	 * 
	 * @param filter {HashMap<String, String>}
	 * @return {ArrayList<OccModel>} a list of occupation object
	 */
	public ArrayList<OccModel> read(HashMap<String, String> filter) {
		String sqlStr = "select * from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0)
			sqlStr   += " where " + filterStr;
		
		sqlStr		 += " order by name";
		
		ArrayList<OccModel> tableList = new ArrayList<OccModel>();
		
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
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("id", rs.getString("id"));
				data.put("name", rs.getString("name"));
				data.put("state", rs.getBoolean("state"));
				tableList.add(new OccModel(data));
			}
		}
		catch(Exception e) {
			log.error("In read: {}", e.toString());
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
	 * @return {ArrayList<OccModel>} a list of occupation object
	 */
	public ArrayList<OccModel> read(HashMap<String, String> filter, int skipNum, int readNum) {
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
		
		ArrayList<OccModel> tableList = new ArrayList<OccModel>();
		
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
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("id", rs.getString("id"));
				data.put("name", rs.getString("name"));
				data.put("state", rs.getBoolean("state"));
				tableList.add(new OccModel(data));
			}
		}
		catch(Exception e) {
			log.error("In read: {}", e.toString());
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
			log.error("In update: {}", e.toString());
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
			log.error("In delete: {}", e.toString());
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
			log.error("In close: {}", e.toString());
		}
	}
}
