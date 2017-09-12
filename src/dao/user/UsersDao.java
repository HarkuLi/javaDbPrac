package dao.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import model.user.UsersModel;
import service.ConnectionPool;

public class UsersDao{
	private ConnectionPool conPool;
	private Connection con;
	private Statement stat;
	private ResultSet rs;
	private PreparedStatement pst;

	public UsersDao() {
		conPool = new ConnectionPool();
	}
	
	/**
	 * @param filter {HashMap<String, String>}
	 * @return {int} total number of rows, and return -1 when the table doesn't exist
	 */
	public int getRowNum(HashMap<String, Object> filter) {
		String sqlStr = "select count(id) from users";
		
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
		String sqlStr = "insert into users (id, name, age, birth, photoName, occupation)";
		sqlStr += " values (?, ?, ?, ?, ?, ?)";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			int idx = 1;
			pst.setString (idx++, (String)newData.get("id"));
			pst.setString (idx++, (String)newData.get("name"));
			pst.setInt    (idx++, (int)newData.get("age"));
			pst.setDate   (idx++, (Date)newData.get("birth"));
			pst.setString (idx++, (String)newData.get("photoName"));
			pst.setString (idx++, (String)newData.get("occupation"));
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
	 * @param skipNum {int} how many rows to skip
	 * @param readNum {int} how many rows to read
	 * @return {ArrayList<UsersModel>} a list of user object
	 */
	public ArrayList<UsersModel> read(HashMap<String, Object> filter, int skipNum, int readNum) {
		String sqlStr = "select * from users";
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0)
			sqlStr   += " where " + filterStr;
		
		sqlStr		 += " order by name" +
				        " limit ?,?";
		
		ArrayList<UsersModel> tableList = new ArrayList<UsersModel>();
		
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
				UsersModel newUser = new UsersModel();
				newUser.setId        (rs.getString("id"));
				newUser.setName      (rs.getString("name"));
				newUser.setAge       (rs.getInt("age"));
				newUser.setBirth     (rs.getDate("birth"));
				newUser.setPhotoName (rs.getString("photoName"));
				newUser.setOccupation(rs.getString("occupation"));
				tableList.add(newUser);
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
	
	public void update(HashMap<String, Object> setData) {
		String sqlStr = "update users";
		
		//handle the data to set
		String id = (String)setData.get("id");
		setData.remove("id");
		HashMap<String, Object> handledNewData = setDataHandle(setData);
		String setDataStr = (String)handledNewData.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledNewData.get("paramList");
		if(setDataStr.length() == 0) return;	//new data is null
		sqlStr += " set " + setDataStr;
		sqlStr += " where id = ?";
		
		try {
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			
			int idx = 1;
			for(Object param : paramList) {
				pst.setObject(idx++, param);
			}
			pst.setString(idx++, id);
			
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
		String sqlStr = "delete from users" +
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
		String id = (String)filter.get("id");
		String name = (String)filter.get("name");
		Date birthFrom = (Date)filter.get("birthFrom");
		Date birthTo = (Date)filter.get("birthTo");
		String occ = (String)filter.get("occ");
		Object state = filter.get("state");	//boolean
		String[] interest = (String[])filter.get("interest");
		
		if(id != null) {
			queryStr += "id = ?";
			paramList.add(id);
		}
		if(name != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "name like ?";
			paramList.add("%" + name + "%");
		}
		if(birthFrom != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "birth >= ?";
			paramList.add(birthFrom);
		}
		if(birthTo != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "birth <= ?";
			paramList.add(birthTo);
		}
		if(occ != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "occupation = ?";
			paramList.add(occ);
		}
		if(state != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "id in (select userId from userAccount where state = ?)";
			paramList.add((boolean)state);
		}
		if(interest != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "id in (select userId from userInterest where interest in (";
			
			for(int i=0; i<interest.length; ++i) {
				queryStr += "?";
				paramList.add(interest[i]);
				if(i != interest.length-1) queryStr += ", ";
			}
			
			queryStr += "))";
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