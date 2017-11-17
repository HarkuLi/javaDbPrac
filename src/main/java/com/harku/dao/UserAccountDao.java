package com.harku.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.harku.model.UserFilter;
import com.harku.model.User;

@Repository
public class UserAccountDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String TABLE_NAME = "userAccount";
	
	private final RowMapper<User> userAccountRowMapper = new BeanPropertyRowMapper<User>(User.class);

	public void create(User newData) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("insert into ");
		sqlStr.append(TABLE_NAME);
		sqlStr.append(" (id, account, password, state)");
		sqlStr.append(" values (?, ?, ?, ?)");
		
		Object[] paramList = {newData.getId(), newData.getAccount(), newData.getPassword(), newData.getState()};
		
		jdbcTemplate.update(sqlStr.toString(), paramList);
	}
	
	public ArrayList<User> read(UserFilter filter) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from ");
		sqlStr.append(TABLE_NAME);
		
		//handle the filter
		Map<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		List<String> paramList = (List<String>)handledFilter.get("paramList");
		if(filterStr.length() != 0) {
			sqlStr.append(" where ");
			sqlStr.append(filterStr);
		}
		
		ArrayList<User> rstList = 
				new ArrayList<User>(jdbcTemplate.query(sqlStr.toString(), paramList.toArray(), userAccountRowMapper));
		
		return rstList;
	}
	
	public void update(User setData) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update ");
		sqlStr.append(TABLE_NAME);

		//handle the data to set
		String userId = setData.getId();
		Map<String, Object> handledSetData = setDataHandle(setData);
		String setDataStr = (String)handledSetData.get("queryStr");
		@SuppressWarnings("unchecked")
		List<String> paramList = (List<String>)handledSetData.get("paramList");
		if(setDataStr.length() == 0) return;	//new data is null
		sqlStr.append(" set ");
		sqlStr.append(setDataStr);
		sqlStr.append(" where id = ?");
		
		paramList.add(userId);
		
		jdbcTemplate.update(sqlStr.toString(), paramList.toArray());
	}

	public void delete(String userId) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("delete from ");
		sqlStr.append(TABLE_NAME);
		sqlStr.append(" where id = ?");
		
		jdbcTemplate.update(sqlStr.toString(), new Object[] {userId});
	}
	
	/**
	 * 
	 * @param setData {UsersModel}
	 * @return {Map<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: List<Object>,
	 *   }
	 */
	private Map<String, Object> setDataHandle(User setData) {
		StringBuffer queryStr = new StringBuffer();
		List<Object> paramList = new ArrayList<Object>();
		Map<String, Object> rst = new HashMap<String, Object>();
		
		String account = setData.getAccount();
		String password = setData.getPassword();
		Boolean state = setData.getState();
		Long signInTime = setData.getSignInTime();
		String token = setData.getToken();
		
		if(account != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("account = ?");
			paramList.add(account);
		}
		if(password != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("password = ?");
			paramList.add(password);
		}
		if(state != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("state = ?");
			paramList.add(state);
		}
		if(signInTime != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("sign_in_time = ?");
			paramList.add(signInTime);
		}
		if(token != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("token = ?");
			paramList.add(token);
		}
		
		rst.put("queryStr", queryStr.toString());
		rst.put("paramList", paramList);
		return rst;
	}
	
	/**
	 * 
	 * @param filter {UserFilterModel}
	 * @return {Map<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: List<Object>,
	 *   }
	 */
	private Map<String, Object> filterHandle(UserFilter filter) {
		StringBuffer queryStr = new StringBuffer();
		List<Object> paramList = new ArrayList<Object>();
		Map<String, Object> rst = new HashMap<String, Object>();
		
		String account = filter.getAccount();
		String userId = filter.getId();
		String token = filter.getToken();
		
		//get filters
		if(account != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("account = ?");
			paramList.add(account);
		}
		if(userId != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("id = ?");
			paramList.add(userId);
		}
		if(token != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("token = ?");
			paramList.add(token);
		}
		
		rst.put("queryStr", queryStr.toString());
		rst.put("paramList", paramList);
		
		return rst;
	}
}