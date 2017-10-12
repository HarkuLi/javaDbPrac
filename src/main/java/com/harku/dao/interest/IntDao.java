package com.harku.dao.interest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.harku.model.interest.IntModel;
import com.harku.rowMapper.interest.IntMapper;

@Repository
public class IntDao {
	@Autowired
	private JdbcTemplate jdbcObj;
	
	private final String tableName = "interest";
	
	/**
	 * @param filter {HashMap<String, String>}
	 * @return {int} total number of rows
	 */
	public int getRowNum(HashMap<String, String> filter) {
		String sqlStr = "select count(id) from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0) sqlStr += " where " + filterStr;
		
		return jdbcObj.queryForObject(sqlStr, paramList.toArray(), Integer.class);
	}
	
	public void create(HashMap<String, Object> newData) {
		String id = UUID.randomUUID().toString();
		String sqlStr = "insert into " + tableName;
		sqlStr		 += " (id, name, state)";
		sqlStr       += " values (?, ?, ?)";
		
		Object[] paramList = {id , newData.get("name"), newData.get("state")};
		
		jdbcObj.update(sqlStr, paramList);
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
		
		ArrayList<IntModel> tableList = new ArrayList<IntModel>(jdbcObj.query(sqlStr, paramList.toArray(), new IntMapper()));
		
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
		
		paramList.add(skipNum);
		paramList.add(readNum);
		
		ArrayList<IntModel> tableList = new ArrayList<IntModel>(jdbcObj.query(sqlStr, paramList.toArray(), new IntMapper()));
		
		return tableList;
	}
	
	public void update(HashMap<String, Object> data) {
		String sqlStr = "update " + tableName +
						" set name = ?, state = ?" +
						" where id = ?";
		
		Object[] paramList = {data.get("name"), data.get("state"), data.get("id")};
		
		jdbcObj.update(sqlStr, paramList);
	}
	
	public void delete(String id) {
		String sqlStr = "delete from " + tableName +
						" where id = ?";
		
		jdbcObj.update(sqlStr, new Object[] {id});
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
}
