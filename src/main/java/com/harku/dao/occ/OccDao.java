package com.harku.dao.occ;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.harku.model.occ.OccModel;
import com.harku.rowMapper.occ.OccMapper;

@Repository
public class OccDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String tableName = "occ";

	/**
	 * @param filter {OccModel}
	 * @return {int} total number of rows, and return -1 when the table doesn't exist
	 */
	public int getRowNum(OccModel filter) {
		String sqlStr = "select count(id) from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0) sqlStr += " where " + filterStr;
		
		return jdbcTemplate.queryForObject(sqlStr, paramList.toArray(), Integer.class);
	}
	
	public void create(OccModel newData) {
		String sqlStr = "insert into " + tableName;
		sqlStr		 += " (id, name, state)";
		sqlStr       += " values (?, ?, ?)";
		
		Object[] paramList = {newData.getId(), newData.getName(), newData.getState()};

		jdbcTemplate.update(sqlStr, paramList);
	}
	
	/**
	 * 
	 * @param filter {OccModel}
	 * @return {ArrayList<OccModel>} a list of occupation object
	 */
	public ArrayList<OccModel> read(OccModel filter) {
		String sqlStr = "select * from " + tableName;
		
		//handle the filter
		HashMap<String, Object> handledFilter = filterHandle(filter);
		String filterStr = (String)handledFilter.get("queryStr");
		@SuppressWarnings("unchecked")
		ArrayList<Object> paramList = (ArrayList<Object>)handledFilter.get("paramList");
		if(filterStr.length() > 0)
			sqlStr   += " where " + filterStr;
		
		sqlStr		 += " order by name";
		
		ArrayList<OccModel> tableList = new ArrayList<OccModel>(jdbcTemplate.query(sqlStr, paramList.toArray(), new OccMapper()));
		
		return tableList;
	}
	
	/**
	 * 
	 * @param filter {OccModel}
	 * @param skipNum {int} how many rows to skip
	 * @param readNum {int} how many rows to read
	 * @return {ArrayList<OccModel>} a list of occupation object
	 */
	public ArrayList<OccModel> read(OccModel filter, int skipNum, int readNum) {
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
		
		ArrayList<OccModel> tableList = new ArrayList<OccModel>(jdbcTemplate.query(sqlStr, paramList.toArray(), new OccMapper()));
		
		return tableList;
	}
	
	public void update(OccModel data) {
		String sqlStr = "update " + tableName +
						" set name = ?, state = ?" +
						" where id = ?";
		
		Object[] paramList = {data.getName(), data.getState(), data.getId()};
		
		jdbcTemplate.update(sqlStr, paramList);
	}
	
	public void delete(String id) {
		String sqlStr = "delete from " + tableName +
						" where id = ?";
		
		jdbcTemplate.update(sqlStr, new Object[] {id});
	}
	
	/**
	 * 
	 * @param filter {OccModel}
	 * @return {HashMap<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: ArrayList<Object>,
	 *   }
	 */
	private HashMap<String, Object> filterHandle(OccModel filter) {
		String queryStr = "";
		ArrayList<Object> paramList = new ArrayList<Object>();
		HashMap<String, Object> rst = new HashMap<String, Object>();
		
		//get filters
		String id = filter.getId();
		String name = filter.getName();
		Boolean state = filter.getState();
		
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
			paramList.add(state);
		}
		
		rst.put("queryStr", queryStr);
		rst.put("paramList", paramList);
		
		return rst;
	}
}
