package com.harku.dao.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;
import com.harku.rowMapper.user.UserMapper;

@Repository
public class UsersDao{
	@Autowired
	private JdbcTemplate jdbcObj;
	
	/**
	 * @param filter {UserFilterModel}
	 * @return {int} total number of rows
	 */
	public int getRowNum(UserFilterModel filter) {
		String sqlStr = "select count(id) from users";
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0) sqlStr += " where " + filterStr;
		
		return jdbcObj.queryForObject(sqlStr, paramList.toArray(), Integer.class);
	}
	
	public void create(UsersModel newData) {
		String sqlStr = "insert into users (id, name, age, birth, photoName, occupation)";
		sqlStr += " values (?, ?, ?, ?, ?, ?)";
		
		Object[] paramList = {newData.getId(), newData.getName(), newData.getAge(),
							  newData.getBirth(), newData.getPhotoName(), newData.getOccupation()};
		
		jdbcObj.update(sqlStr, paramList);
	}
	
	/**
	 * 
	 * @param filter {UserFilterModel}
	 * @param skipNum {int} how many rows to skip
	 * @param readNum {int} how many rows to read
	 * @return {ArrayList<UsersModel>} a list of user object
	 */
	public ArrayList<UsersModel> read(UserFilterModel filter, int skipNum, int readNum) {
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
		
		paramList.add(skipNum);
		paramList.add(readNum);
		
		ArrayList<UsersModel> tableList = new ArrayList<UsersModel>(jdbcObj.query(sqlStr, paramList.toArray(), new UserMapper()));
		
		return tableList;
	}
	
	public void update(UsersModel setData) {
		String sqlStr = "update users";
		
		//handle the data to set
		String id = setData.getId();
		HashMap<String, Object> handledNewData = setDataHandle(setData);
		String setDataStr = (String)handledNewData.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledNewData.get("paramList");
		if(setDataStr.length() == 0) return;	//new data is null
		sqlStr += " set " + setDataStr;
		sqlStr += " where id = ?";
		
		paramList.add(id);
		
		jdbcObj.update(sqlStr, paramList.toArray());
	}
	
	public void delete(String id) {
		String sqlStr = "delete from users" +
						" where id = ?";
		
		jdbcObj.update(sqlStr, new Object[] {id});
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
		
		String name = setData.getName();
		Integer age = setData.getAge();
		String birth = setData.getBirth();
		String photoName = setData.getPhotoName();
		String occupation = setData.getOccupation();
		
		if(name != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "name = ?";
			paramList.add(name);
		}
		if(age != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "age = ?";
			paramList.add(age);
		}
		if(birth != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "birth = ?";
			paramList.add(birth);
		}
		if(photoName != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "photoName = ?";
			paramList.add(photoName);
		}
		if(occupation != null) {
			if(queryStr.length() != 0) queryStr += ", ";
			queryStr += "occupation = ?";
			paramList.add(occupation);
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
		
		//get filters
		String id = filter.getId();
		String name = filter.getName();
		String birthFrom = filter.getBirthFrom();
		String birthTo = filter.getBirthTo();
		String occ = filter.getOccupation();
		Boolean state = filter.getState();
		String[] interest = filter.getInterest();
		
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
			paramList.add(state);
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
}