package com.harku.test.rowMapper.interest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.harku.model.interest.IntModel;
import com.harku.rowMapper.interest.IntMapper;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestIntMapper {
	private IntMapper intMapper = new IntMapper();
	
	@Mock
	private ResultSet resultSet;
	
	@Test
	public void testMapRow() throws SQLException {
		IntModel interest = RandomData.genInterest();
		
		//set Stub
		when(resultSet.getString("id")).thenReturn(interest.getId());
		when(resultSet.getString("name")).thenReturn(interest.getName());
		when(resultSet.getBoolean("state")).thenReturn(interest.getState());
		
		//call function
		IntModel readInterest = intMapper.mapRow(resultSet, 1);
		
		//verify
		assertEquals(interest.getId()   , readInterest.getId());
		assertEquals(interest.getName() , readInterest.getName());
		assertEquals(interest.getState(), readInterest.getState());
	}
}
