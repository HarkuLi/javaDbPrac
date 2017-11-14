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

import com.harku.dao.OccDao;
import com.harku.model.OccModel;
import com.harku.service.OccService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccService {
	@Mock
	private OccDao occupationDao;
	
	@Autowired
	@InjectMocks
	private OccService occupationService;
	
	@Test
	public void testCreateOcc() {
		OccModel occ = RandomData.genOcc();
		
		//call function
		occupationService.createOcc(occ.getName(), occ.getState());
		
		//verify
		verify(occupationDao).create(argThat(newData -> newData.getName().equals(occ.getName())
											  && newData.getState() == occ.getState()));
	}
	
	@Test
	public void testGetOcc() {
		OccModel occ = RandomData.genOcc();
		ArrayList<OccModel> occList = new ArrayList<OccModel>();
		occList.add(occ);
		
		//set Stub
		when(occupationDao.read(argThat(filter -> filter.getId().equals(occ.getId())), eq(0), eq(1)))
			.thenReturn(occList);
		
		//call function
		OccModel getOcc = occupationService.getOcc(occ.getId());
		
		//verify
		assertEquals(occ.getName(), getOcc.getName());
		assertEquals(occ.getState(), getOcc.getState());
	}
	
	@Test
	public void testGetList() {
		//generate List
		int occNum = (int)(Math.random()*10 + 10);
		ArrayList<OccModel> occList = new ArrayList<OccModel>();
		for(int i=0; i<occNum; ++i) occList.add(RandomData.genOcc());
		
		//set Stub
		when(occupationDao.read(argThat(filter -> filter.getState())))
			.thenReturn(occList);
		
		//call function and verify
		assertTrue(occupationService.getList().equals(occList));
	}
	
	@Test
	public void testGetPage() {
		OccModel filter = new OccModel();
		int entryPerPage = 10;
		int page = (int)(Math.random()*10 +1);
		int skipNum = entryPerPage * (page - 1);
		
		//generate return value for Stub
		int listNum = (int)(Math.random()*10 +1);
		ArrayList<OccModel> occList = new ArrayList<OccModel>();
		for(int i=0; i<listNum; ++i) occList.add(new OccModel());
		
		//set Stub
		when(occupationDao.read(filter, skipNum, entryPerPage))
			.thenReturn(occList);
		
		//call function
		ArrayList<OccModel> getList = occupationService.getPage(page, filter);
		
		//verify
		assertEquals(occList, getList);
	}
	
	@Test
	public void testGetTotalPage() {
		OccModel filter = new OccModel();
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
		OccModel occupation = RandomData.genOcc();
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








