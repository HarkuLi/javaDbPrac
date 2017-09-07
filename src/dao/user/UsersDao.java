package dao.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	private final String datePattern = "yyyy-MM-dd";

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
		String sqlStr = "insert into users (id, account, password, name, age, birth, photo_name, occupation, state)";
		sqlStr += " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			DateFormat sdf = new SimpleDateFormat(datePattern);
			//note: the type Date here is java.sql.date
			//      but sdf.parse(String) returns java.util.date
			Date birthDate = new Date(sdf.parse((String)newData.get("birth")).getTime());
			
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			int idx = 1;
			pst.setString (idx++, (String)newData.get("id"));
			pst.setString (idx++, (String)newData.get("account"));
			pst.setString (idx++, (String)newData.get("password"));
			pst.setString (idx++, (String)newData.get("name"));
			pst.setInt    (idx++, (int)newData.get("age"));
			pst.setDate   (idx++, birthDate);
			pst.setString (idx++, (String)newData.get("photoName"));
			pst.setString (idx++, (String)newData.get("occupation"));
			pst.setBoolean(idx++, (boolean)newData.get("state"));
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
				newUser.setAccount   (rs.getString("account"));
				newUser.setPassword  (rs.getString("password"));
				newUser.setName      (rs.getString("name"));
				newUser.setAge       (rs.getInt("age"));
				newUser.setBirth     (rs.getDate("birth"));
				newUser.setPhotoName (rs.getString("photo_name"));
				newUser.setOccupation(rs.getString("occupation"));
				newUser.setState     (rs.getBoolean("state"));
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
	
	public void update(HashMap<String, Object> newData) {
		String sqlStr = "update users" +
						" set name = ?, age = ?, birth = ?, occupation = ?, state = ?";
		String photoName = (String) newData.get("photoName");
		if(photoName != null) {
			sqlStr   += ", photo_name = ?";
		}
		sqlStr       += " where id = ?";
		
		try {
			DateFormat sdf = new SimpleDateFormat(datePattern);
			//note: the type Date here is java.sql.date
			//      but sdf.parse(String) returns java.util.date
			Date birthDate = new Date(sdf.parse((String)newData.get("birth")).getTime());
			
			con = conPool.getConnection();
			pst = con.prepareStatement(sqlStr);
			int idx = 1;
			pst.setString (idx++, (String)newData.get("name"));
			pst.setInt    (idx++, (int)newData.get("age"));
			pst.setDate   (idx++, birthDate);
			pst.setString (idx++, (String)newData.get("occupation"));
			pst.setBoolean(idx++, (boolean)newData.get("state"));
			if(photoName != null) {
				pst.setString(idx++, photoName);
			}
			pst.setString (idx++, (String)newData.get("id"));
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
	 * @param filter {HashMap<String, String>}
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
		String account = (String)filter.get("account");
		String name = (String)filter.get("name");
		String birthFrom = (String)filter.get("birthFrom");
		String birthTo = (String)filter.get("birthTo");
		String occ = (String)filter.get("occ");
		String state = (String)filter.get("state");
		String[] interest = (String[])filter.get("interest[]");
		
		if(id != null) {
			queryStr += "id = ?";
			paramList.add(id);
		}
		if(account != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "account = ?";
			paramList.add(account);
		}
		if(name != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "name like ?";
			paramList.add("%" + name + "%");
		}
		if(birthFrom != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "birth >= ?";
			
			try {
				DateFormat sdf = new SimpleDateFormat(datePattern);
				Date birthFromDate = new Date(sdf.parse(birthFrom).getTime());
				paramList.add(birthFromDate);
			} catch(Exception e) {
				System.out.println("Exception in filterHandle: " + e.toString());
			}
		}
		if(birthTo != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "birth <= ?";
			
			try {
				DateFormat sdf = new SimpleDateFormat(datePattern);
				Date birthToDate = new Date(sdf.parse(birthTo).getTime());
				paramList.add(birthToDate);
			} catch(Exception e) {
				System.out.println("Exception in filterHandle: " + e.toString());
			}
		}
		if(occ != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "occupation = ?";
			paramList.add(occ);
		}
		if(state != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "state = ?";
			paramList.add(state.equals("1"));
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