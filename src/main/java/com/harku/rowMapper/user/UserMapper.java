package com.harku.rowMapper.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.harku.model.user.UsersModel;

public class UserMapper implements RowMapper<UsersModel> {
	public UsersModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		UsersModel newUser = new UsersModel();
		newUser.setId        (rs.getString("id"));
		newUser.setName      (rs.getString("name"));
		newUser.setAge       (rs.getInt("age"));
		newUser.setBirth     (rs.getString("birth"));
		newUser.setPhotoName (rs.getString("photoName"));
		newUser.setOccupation(rs.getString("occupation"));
		
		return newUser;
	}
}
