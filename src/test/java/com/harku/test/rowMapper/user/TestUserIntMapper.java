package com.harku.test.rowMapper.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.harku.rowMapper.user.UserIntMapper;

@RunWith(MockitoJUnitRunner.class)
public class TestUserIntMapper {
	private UserIntMapper userIntMapper = new UserIntMapper();
	
	@Mock
	private ResultSet resultSet;
	
	@Test
	public void testMapRow() throws SQLException {
		String interest = UUID.randomUUID().toString();
		
		//set Stub
		when(resultSet.getString("interest")).thenReturn(interest);
		
		//call function
		String readInterest = userIntMapper.mapRow(resultSet, 1);
		
		//verify
		assertEquals(interest, readInterest);
	}
}
