package com.harku.rowMapper.interest;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.harku.model.interest.IntModel;

public class IntMapper implements RowMapper<IntModel>{
	public IntModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		IntModel data = new IntModel();
		data.setId(rs.getString("id"));
		data.setName(rs.getString("name"));
		data.setState(rs.getBoolean("state"));
		return data;
	}

}
