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

import com.harku.model.OccModel;

@Repository
public class OccDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String tableName = "occupation";

	private final RowMapper<OccModel> occupationRowMapper = new BeanPropertyRowMapper<OccModel>(OccModel.class);
	
	/**
	 * @param filter {OccModel}
	 * @return {int} total number of rows, and return -1 when the table doesn't exist
	 */
	public int getRowNum(OccModel filter) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select count(id) from ");
		sqlStr.append(tableName);
		
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
	
	public void create(OccModel newData) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("insert into ");
		sqlStr.append(tableName);
		sqlStr.append(" (id, name, state)");
		sqlStr.append(" values (?, ?, ?)");
		
		Object[] paramList = {newData.getId(), newData.getName(), newData.getState()};

		jdbcTemplate.update(sqlStr.toString(), paramList);
	}
	
	/**
	 * 
	 * @param filter {OccModel}
	 * @return {ArrayList<OccModel>} a list of occupation object
	 */
	public ArrayList<OccModel> read(OccModel filter) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from ");
		sqlStr.append(tableName);
		
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
		
		ArrayList<OccModel> tableList
			= new ArrayList<OccModel>(jdbcTemplate.query(sqlStr.toString(), paramList.toArray(), occupationRowMapper));
		
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
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select * from ");
		sqlStr.append(tableName);
		
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
		
		ArrayList<OccModel> tableList
			= new ArrayList<OccModel>(jdbcTemplate.query(sqlStr.toString(), paramList.toArray(), occupationRowMapper));
		
		return tableList;
	}
	
	public void update(OccModel data) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("update ");
		sqlStr.append(tableName);
		sqlStr.append(" set name = ?, state = ?");
		sqlStr.append(" where id = ?");
		
		Object[] paramList = {data.getName(), data.getState(), data.getId()};
		
		jdbcTemplate.update(sqlStr.toString(), paramList);
	}
	
	public void delete(String id) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("delete from ");
		sqlStr.append(tableName);
		sqlStr.append(" where id = ?");
		
		jdbcTemplate.update(sqlStr.toString(), new Object[] {id});
	}
	
	/**
	 * 
	 * @param filter {OccModel}
	 * @return {Map<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: List<Object>,
	 *   }
	 */
	private Map<String, Object> filterHandle(OccModel filter) {
		StringBuffer queryStr = new StringBuffer();
		List<Object> paramList = new ArrayList<Object>();
		Map<String, Object> rst = new HashMap<String, Object>();
		
		//get filters
		String id = filter.getId();
		String name = filter.getName();
		Boolean state = filter.getState();
		
		if(id != null) {
			queryStr.append("id = ?");
			paramList.add(id);
		}
		if(name != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("name like ?");
			paramList.add("%" + name + "%");
		}
		if(state != null) {
			if(queryStr.length() != 0) queryStr.append(" and ");
			queryStr.append("state = ?");
			paramList.add(state);
		}
		
		rst.put("queryStr", queryStr.toString());
		rst.put("paramList", paramList);
		
		return rst;
	}
}