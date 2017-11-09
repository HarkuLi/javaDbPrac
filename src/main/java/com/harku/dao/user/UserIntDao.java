package com.harku.dao.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.harku.rowMapper.user.UserIntMapper;

@Repository
public class UserIntDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String tableName = "userInterest";
	
	public void create(String userId, String interest) {
		String sqlStr = "insert into " + tableName;
		sqlStr	     += " (userId, interest)";
		sqlStr 		 += " values (?, ?)";
		
		jdbcTemplate.update(sqlStr, new Object[] {userId, interest});
	}
	
	/**
	 * 
	 * @param userId {String} user's id
	 * @return {List<String>} return a list of interest id of the user
	 */
	public List<String> read(String userId) {
		String sqlStr = "select interest" +
				        " from " + tableName +
			            " where userId = ?";
		
		List<String> rstList = new ArrayList<String>(jdbcTemplate.query(sqlStr, new Object[] {userId}, new UserIntMapper()));
		
		return rstList;
	}

	public void delete(String userId) {
		String sqlStr = "delete from " + tableName +
						" where userId = ?";
		
		jdbcTemplate.update(sqlStr, new Object[] {userId});
	}
}
