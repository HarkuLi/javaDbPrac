package com.harku.dao.user;

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
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (id, interest)";
		sqlStr 		 += " values (?, ?)";
		
		jdbcTemplate.update(sqlStr, userId, interest);
	}
	
	/**
	 * 
	 * @param userId {String} user's id
	 * @return {List<String>} return a list of interest id of the user
	 */
	public List<String> read(String userId) {
		String sqlStr = "select interest" +
				        " from " + tableName +
			            " where id = ?";
		
		List<String> rstList = jdbcTemplate.queryForList(sqlStr, String.class, userId);
		
		return rstList;
	}

	public void delete(String userId) {
		String sqlStr = "delete from " + tableName +
						" where id = ?";
		
		jdbcTemplate.update(sqlStr, new Object[] {userId});
	}
}
