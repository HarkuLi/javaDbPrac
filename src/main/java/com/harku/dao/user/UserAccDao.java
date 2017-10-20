package com.harku.dao.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;
import com.harku.rowMapper.user.UserAccMapper;

@Repository
public class UserAccDao {
	@Autowired
	private JdbcTemplate jdbcObj;
	
	private final String tableName = "userAccount";

	public void create(UsersModel newData) {
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (userId, account, password, state)";
		sqlStr 		 += " values (?, ?, ?, ?)";
		
		Object[] paramList = {newData.getId(), newData.getAccount(), newData.getPassword(), newData.getState()};
		
		jdbcObj.update(sqlStr, paramList);
	}
	
	public ArrayList<UsersModel> read(UserFilterModel filter) {
		String sqlStr = "select *" +
				        " from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<String> paramList = (ArrayList<String>)handledFilter.get("paramList");
		if(filterStr.length() == 0) return null;
		sqlStr   += " where " + filterStr;
		
		ArrayList<UsersModel> rstList = 
				new ArrayList<UsersModel>(jdbcObj.query(sqlStr, paramList.toArray(), new UserAccMapper()));
		
		return rstList;
	}
	
	public void update(UsersModel setData) {
		String sqlStr = "update " + tableName;

		//handle the data to set
		String userId = setData.getId();
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
	 * @param setData {UsersModel}
	 * @return {HashMap<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: ArrayList<Object>,
	 *   }
	 */
	private HashMap<String, Object> setDataHandle(UsersModel setData) {
		String queryStr = "";
		ArrayList<Object> paramList = new ArrayList<Object>();
		HashMap<String, Object> rst = new HashMap<String, Object>();
		
		String account = setData.getAccount();
		String password = setData.getPassword();
		Boolean state = setData.getState();
		Long signInTime = setData.getSignInTime();
		String token = setData.getToken();
		
		if(account != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "account = ?";
			paramList.add(account);
		}
		if(password != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "password = ?";
			paramList.add(password);
		}
		if(state != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "state = ?";
			paramList.add(state);
		}
		if(signInTime != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "signInTime = ?";
			paramList.add(signInTime);
		}
		if(token != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "token = ?";
			paramList.add(token);
		}
		
		rst.put("queryStr", queryStr);
		rst.put("paramList", paramList);
		return rst;
	}
	
	/**
	 * 
	 * @param filter {UserFilterModel}
	 * @return {HashMap<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: ArrayList<Object>,
	 *   }
	 */
	private HashMap<String, Object> filterHandle(UserFilterModel filter) {
		String queryStr = "";
		ArrayList<Object> paramList = new ArrayList<Object>();
		HashMap<String, Object> rst = new HashMap<String, Object>();
		
		String account = filter.getAccount();
		String userId = filter.getId();
		String token = filter.getToken();
		
		//get filters
		if(account != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "account = ?";
			paramList.add(account);
		}
		if(userId != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "userId = ?";
			paramList.add(userId);
		}
		if(token != null) {
			if(queryStr.length() != 0) queryStr += " and ";
			queryStr += "token = ?";
			paramList.add(token);
		}
		
		rst.put("queryStr", queryStr);
		rst.put("paramList", paramList);
		
		return rst;
	}
}