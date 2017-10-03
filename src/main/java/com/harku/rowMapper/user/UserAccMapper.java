package com.harku.rowMapper.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.jdbc.core.RowMapper;

public class UserAccMapper implements RowMapper<HashMap<String, Object>> {
	public HashMap<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		HashMap<String, Object> acc = new HashMap<String, Object>();
		acc.put("userId", rs.getString("userId"));
		acc.put("account", rs.getString("account"));
		acc.put("password", rs.getString("password"));
		acc.put("state", rs.getBoolean("state"));
		acc.put("signInTime", rs.getLong("signInTime"));
		acc.put("token", rs.getString("token"));
		
		return acc;
	}

}
