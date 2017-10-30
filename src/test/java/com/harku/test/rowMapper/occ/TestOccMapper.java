package com.harku.test.rowMapper.occ;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.harku.model.occ.OccModel;
import com.harku.rowMapper.occ.OccMapper;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccMapper {
	private OccMapper occMapper = new OccMapper();
	
	@Mock
	private ResultSet resultset;
	
	@Test
	public void testMapRow() throws SQLException {
		OccModel occupation = RandomData.genOcc();
		
		//set Stub
		when(resultset.getString("id")).thenReturn(occupation.getId());
		when(resultset.getString("name")).thenReturn(occupation.getName());
		when(resultset.getBoolean("state")).thenReturn(occupation.getState());
		
		//call function
		OccModel readOccupation = occMapper.mapRow(resultset, 1);
		
		//verify
		assertEquals(occupation.getId()   , readOccupation.getId());
		assertEquals(occupation.getName() , readOccupation.getName());
		assertEquals(occupation.getState(), readOccupation.getState());
	}
}
