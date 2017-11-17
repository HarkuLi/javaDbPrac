package com.harku.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.harku.dao.OccupationDao;
import com.harku.model.Occupation;
import com.harku.service.OccupationService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccupationService {
	@Mock
	private OccupationDao occupationDao;
	
	@Autowired
	@InjectMocks
	private OccupationService occupationService;
	
	@Test
	public void testCreateOcc() {
		Occupation occ = RandomData.genOcc();
		
		//call function
		occupationService.createOcc(occ.getName(), occ.getState());
		
		//verify
		verify(occupationDao).create(argThat(newData -> newData.getName().equals(occ.getName())
											  && newData.getState() == occ.getState()));
	}
	
	@Test
	public void testGetOcc() {
		Occupation occ = RandomData.genOcc();
		ArrayList<Occupation> occList = new ArrayList<Occupation>();
		occList.add(occ);
		
		//set Stub
		when(occupationDao.read(argThat(filter -> filter.getId().equals(occ.getId())), eq(0), eq(1)))
			.thenReturn(occList);
		
		//call function
		Occupation getOcc = occupationService.getOcc(occ.getId());
		
		//verify
		assertEquals(occ.getName(), getOcc.getName());
		assertEquals(occ.getState(), getOcc.getState());
	}
	
	@Test
	public void testGetList() {
		//generate List
		int occNum = (int)(Math.random()*10 + 10);
		ArrayList<Occupation> occList = new ArrayList<Occupation>();
		for(int i=0; i<occNum; ++i) occList.add(RandomData.genOcc());
		
		//set Stub
		when(occupationDao.read(argThat(filter -> filter.getState())))
			.thenReturn(occList);
		
		//call function and verify
		assertTrue(occupationService.getList().equals(occList));
	}
	
	@Test
	public void testGetPage() {
		Occupation filter = new Occupation();
		int entryPerPage = 10;
		int page = (int)(Math.random()*10 +1);
		int skipNum = entryPerPage * (page - 1);
		
		//generate return value for Stub
		int listNum = (int)(Math.random()*10 +1);
		ArrayList<Occupation> occList = new ArrayList<Occupation>();
		for(int i=0; i<listNum; ++i) occList.add(new Occupation());
		
		//set Stub
		when(occupationDao.read(filter, skipNum, entryPerPage))
			.thenReturn(occList);
		
		//call function
		ArrayList<Occupation> getList = occupationService.getPage(page, filter);
		
		//verify
		assertEquals(occList, getList);
	}
	
	@Test
	public void testGetTotalPage() {
		Occupation filter = new Occupation();
		int entryPerPage = 10;
		int rowNum = (int)(Math.random()*100 +10);
		int totalPage = (int) Math.ceil((double) rowNum / entryPerPage);
		
		//set Stub
		when(occupationDao.getRowNum(filter)).thenReturn(rowNum);
		
		//verify
		assertEquals(totalPage, occupationService.getTotalPage(filter));
	}
	
	@Test
	public void testUpdate() {
		Occupation occupation = RandomData.genOcc();
		String id = occupation.getId();
		String name = occupation.getName();
		Boolean state = occupation.getState();
		
		//call function
		occupationService.update(id, name, state);
		
		//verify
		verify(occupationDao).update(argThat(data -> data.getId().equals(id)
										   && data.getName().equals(name)
										   && data.getState() == state));
	}
	
	@Test
	public void testDelete() {
		String id = UUID.randomUUID().toString();
		
		//call function
		occupationService.delete(id);
		
		//verify
		verify(occupationDao).delete(id);
	}
}








