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
public class UserDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String TABLE_NAME = "user";
	
	private final RowMapper<User> userRowMapper = new BeanPropertyRowMapper<User>(User.class);
	
	/**
	 * @param filter {UserFilterModel}
	 * @return {int} total number of rows
	 */
	public int getRowNum(UserFilter filter) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select count(id) from ");
		sqlStr.append(TABLE_NAME);
		
		//handle the filter
		Map<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		List<Object> paramList = (List<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0) {
			sqlStr.append(" where ");
			sqlStr.append(filterStr);
		}
		
		return jdbcTemplate.queryForObject(sqlStr.toString(), paramList.toArray(), Integer.class);
	}
	
	public void create(User newData) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("insert into ");
		sqlStr.append(TABLE_NAME);
		sqlStr.append(" (id, name, age, birth, photo_name, occupation)");
		sqlStr.append(" values (?, ?, ?, ?, ?, ?)");
		
		Object[] paramList = {newData.getId(), newData.getName(), newData.getAge(),
							  newData.getBirth(), newData.getPhotoName(), newData.getOccupation()};
		
		jdbcTemplate.update(sqlStr.toString(), paramList);
	}
	
	/**
	 * 
	 * @param filter {UserFilterModel}
	 * @param skipNum {int} how many rows to skip
	 * @param readNum {int} how many rows to read
	 * @return {ArrayList<UsersModel>} a list of user object
	 */
	public ArrayList<User> read(UserFilter filter, int skipNum, int readNum) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from ");
		sqlStr.append(TABLE_NAME);
		
		//handle the filter
		Map<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		List<Object> paramList = (List<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0) {
			sqlStr.append(" where ");
			sqlStr.append(filterStr);
		}
		
		sqlStr.append(" order by name");
		sqlStr.append(" limit ?,?");
		
		paramList.add(skipNum);
		paramList.add(readNum);
		
		ArrayList<User> tableList
			= new ArrayList<User>(jdbcTemplate.query(sqlStr.toString(), paramList.toArray(), userRowMapper));
		
		return tableList;
	}
	
	public void update(User setData) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update ");
		sqlStr.append(TABLE_NAME);
		
		//handle the data to set
		String id = setData.getId();
		Map<String, Object> handledNewData = setDataHandle(setData);
		String setDataStr = (String)handledNewData.get("queryStr");
		@SuppressWarnings("unchecked")
		List<Object> paramList = (List<Object>)handledNewData.get("paramList");
		if(setDataStr.length() == 0) return;	//new data is null
		sqlStr.append(" set ");
		sqlStr.append(setDataStr);
		sqlStr.append(" where id = ?");
		
		paramList.add(id);
		
		jdbcTemplate.update(sqlStr.toString(), paramList.toArray());
	}
	
	public void delete(String id) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("delete from ");
		sqlStr.append(TABLE_NAME);
		sqlStr.append(" where id = ?");
		
		jdbcTemplate.update(sqlStr.toString(), new Object[] {id});
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
		
		String name = setData.getName();
		Integer age = setData.getAge();
		String birth = setData.getBirth();
		String photoName = setData.getPhotoName();
		String occupation = setData.getOccupation();
		
		if(name != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("name = ?");
			paramList.add(name);
		}
		if(age != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("age = ?");
			paramList.add(age);
		}
		if(birth != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("birth = ?");
			paramList.add(birth);
		}
		if(photoName != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("photo_name = ?");
			paramList.add(photoName);
		}
		if(occupation != null) {
			if(queryStr.length() != 0) queryStr.append(", ");
			queryStr.append("occupation = ?");
			paramList.add(occupation);
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
		
		//get filters
		String id = filter.getId();
		String name = filter.getName();
		String birthFrom = filter.getBirthFrom();
		String birthTo = filter.getBirthTo();
		String occ = filter.getOccupation();
		String state = filter.getState();
		String[] interest = filter.getInterest();
		
		if(id != null) {
			queryStr.append("id = ?");
			paramList.add(id);
		}
		if(name != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("name like ?");
			paramList.add("%" + name + "%");
		}
		if(birthFrom != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("birth >= ?");
			paramList.add(birthFrom);
		}
		if(birthTo != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("birth <= ?");
			paramList.add(birthTo);
		}
		if(occ != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("occupation = ?");
			paramList.add(occ);
		}
		if(state != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("id in (select id from userAccount where state = ?)");
			paramList.add(state);
		}
		if(interest != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("id in (select id from userInterest where interest in (");
			
			for(int i=0; i<interest.length; ++i) {
				queryStr.append("?");
				paramList.add(interest[i]);
				if(i != interest.length-1) queryStr.append(", ");
			}
			
			queryStr.append("))");
		}
		
		rst.put("queryStr", queryStr.toString());
		rst.put("paramList", paramList);
		
		return rst;
	}
}