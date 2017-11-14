package com.harku.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserIntDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String tableName = "userInterest";
	
	public void create(String userId, String interest) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("insert into ");
		sqlStr.append(tableName);
		sqlStr.append(" (id, interest) values (?, ?)");
		
		jdbcTemplate.update(sqlStr.toString(), userId, interest);
	}
	
	/**
	 * 
	 * @param userId {String} user's id
	 * @return {List<String>} return a list of interest id of the user
	 */
	public List<String> read(String userId) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select interest from ");
		sqlStr.append(tableName);
		sqlStr.append(" where id = ?");
		
		List<String> rstList = jdbcTemplate.queryForList(sqlStr.toString(), String.class, userId);
		
		return rstList;
	}

	public void delete(String userId) {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("delete from ");
		sqlStr.append(tableName);
		sqlStr.append(" where id = ?");
		
		jdbcTemplate.update(sqlStr.toString(), new Object[] {userId});
	}
}
