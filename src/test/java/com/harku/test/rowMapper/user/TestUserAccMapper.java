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
import com.harku.rowMapper.user.UserAccMapper;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserAccMapper {
	private UserAccMapper userAccMapper = new UserAccMapper();
	
	@Mock
	private ResultSet resultSet;
	
	@Test
	public void testMapRow() throws SQLException {
		UsersModel account = RandomData.genUser();
		
		//set Stub
		when(resultSet.getString("userId")).thenReturn(account.getId());
		when(resultSet.getString("account")).thenReturn(account.getAccount());
		when(resultSet.getString("password")).thenReturn(account.getPassword());
		when(resultSet.getBoolean("state")).thenReturn(account.getState());
		when(resultSet.getLong("signInTime")).thenReturn(account.getSignInTime());
		when(resultSet.getString("token")).thenReturn(account.getToken());
		
		//call function
		UsersModel readAccount = userAccMapper.mapRow(resultSet, 1);
		
		//verify
		assertEquals(account.getId(), readAccount.getId());
		assertEquals(account.getAccount(), readAccount.getAccount());
		assertEquals(account.getPassword(), readAccount.getPassword());
		assertEquals(account.getState(), readAccount.getState());
		assertEquals(account.getSignInTime(), readAccount.getSignInTime());
		assertEquals(account.getToken(), readAccount.getToken());
	}
}
