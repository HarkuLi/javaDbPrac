package com.harku.dao.interest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.harku.model.interest.IntModel;

@Repository
public class IntDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String tableName = "interest";
	
	private final RowMapper<IntModel> interestRowMapper = new BeanPropertyRowMapper<IntModel>(IntModel.class);
	
	/**
	 * @param filter {IntModel}
	 * @return {int} total number of rows
	 */
	public int getRowNum(IntModel filter) {
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
	
	public void create(IntModel newData) {
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
	 * @param filter {IntModel}
	 * @return {ArrayList<IntModel>} a list of interest object
	 */
	public ArrayList<IntModel> read(IntModel filter) {
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
		
		ArrayList<IntModel> tableList
			= new ArrayList<IntModel>(jdbcTemplate.query(sqlStr.toString(), paramList.toArray(), interestRowMapper));
		
		return tableList;
	}
	
	/**
	 * 
	 * @param filter {IntModel}
	 * @param skipNum {int} how many rows to skip
	 * @param readNum {int} how many rows to read
	 * @return {ArrayList<IntModel>} a list of interest object
	 */
	public ArrayList<IntModel> read(IntModel filter, int skipNum, int readNum) {
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
		
		ArrayList<IntModel> tableList
			= new ArrayList<IntModel>(jdbcTemplate.query(sqlStr.toString(), paramList.toArray(), interestRowMapper));
		
		return tableList;
	}
	
	public void update(IntModel data) {
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
	 * @param filter {IntModel}
	 * @return {Map<String, Object>}
	 *   {
	 *     queryStr: String
	 *     paramList: List<Object>,
	 *   }
	 */
	private Map<String, Object> filterHandle(IntModel filter) {
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
