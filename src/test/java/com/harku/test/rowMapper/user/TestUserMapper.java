package com.harku.test.rowMapper.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.harku.model.user.UsersModel;
import com.harku.rowMapper.user.UserMapper;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserMapper {
	private UserMapper userMapper = new UserMapper();
	
	@Mock
	private ResultSet resultSet;
	
	@Test
	public void testMapRow() throws SQLException {
		UsersModel user = RandomData.genUser();
		
		//set Stub
		when(resultSet.getString("id")).thenReturn(user.getId());
		when(resultSet.getString("name")).thenReturn(user.getName());
		when(resultSet.getInt("age")).thenReturn(user.getAge());
		when(resultSet.getString("birth")).thenReturn(user.getBirth());
		when(resultSet.getString("photoName")).thenReturn(user.getPhotoName());
		when(resultSet.getString("occupation")).thenReturn(user.getOccupation());
		
		//call function
		UsersModel readUser = userMapper.mapRow(resultSet, 1);
		
		//verify
		assertEquals(user.getId()        , readUser.getId());
		assertEquals(user.getName()      , readUser.getName());
		assertEquals(user.getAge()       , readUser.getAge());
		assertEquals(user.getBirth()     , readUser.getBirth());
		assertEquals(user.getPhotoName() , readUser.getPhotoName());
		assertEquals(user.getOccupation(), readUser.getOccupation());
	}
}
