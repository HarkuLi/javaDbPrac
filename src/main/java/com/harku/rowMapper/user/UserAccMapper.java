package com.harku.rowMapper.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.harku.model.user.UsersModel;

public class UserAccMapper implements RowMapper<UsersModel> {
	public UsersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		UsersModel acc = new UsersModel();
		acc.setId(rs.getString("userId"));
		acc.setAccount(rs.getString("account"));
		acc.setPassword(rs.getString("password"));
		acc.setState(rs.getBoolean("state"));
		acc.setSignInTime(rs.getLong("signInTime"));
		acc.setToken(rs.getString("token"));
		
		return acc;
	}

}
