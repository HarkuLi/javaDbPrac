package com.harku.dao.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.harku.rowMapper.user.UserAccMapper;

@Repository
public class UserAccDao {
	@Autowired
	private JdbcTemplate jdbcObj;
	
	private final String tableName = "userAccount";

	public void create(HashMap<String, Object> newData) {
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (userId, account, password, state)";
		sqlStr 		 += " values (?, ?, ?, ?)";
		
		Object[] paramList = {newData.get("userId"), newData.get("account"), newData.get("password"), newData.get("state")};
		
		jdbcObj.update(sqlStr, paramList);
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
		
		ArrayList<HashMap<String, Object>> rstList = 
				new ArrayList<HashMap<String, Object>>(jdbcObj.query(sqlStr, paramList.toArray(), new UserAccMapper()));
		
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
		
		paramList.add(userId);
		
		jdbcObj.update(sqlStr, paramList.toArray());
	}

	public void delete(String userId) {
		String sqlStr = "delete from " + tableName +
						" where userId = ?";
		
		jdbcObj.update(sqlStr, new Object[] {userId});
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
}