package com.harku.test.service.occ;

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

import com.harku.dao.occ.OccDao;
import com.harku.model.occ.OccModel;
import com.harku.service.occ.OccService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccService {
	@Mock
	private OccDao occDao;
	
	@Autowired
	@InjectMocks
	private OccService occService;
	
	@Test
	public void testCreateOcc() {
		OccModel occ = RandomData.genOcc();
		
		//call function
		occService.createOcc(occ.getName(), occ.getState());
		
		//verify
		verify(occDao).create(argThat(newData -> newData.getName().equals(occ.getName())
											  && newData.getState() == occ.getState()));
	}
	
	@Test
	public void testGetOcc() {
		OccModel occ = RandomData.genOcc();
		ArrayList<OccModel> occList = new ArrayList<OccModel>();
		occList.add(occ);
		
		//set Stub
		when(occDao.read(argThat(filter -> filter.getId().equals(occ.getId())), eq(0), eq(1)))
			.thenReturn(occList);
		
		//call function
		OccModel getOcc = occService.getOcc(occ.getId());
		
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
		when(occDao.read(argThat(filter -> filter.getState())))
			.thenReturn(occList);
		
		//call function and verify
		assertTrue(occService.getList().equals(occList));
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
		when(occDao.read(filter, skipNum, entryPerPage))
			.thenReturn(occList);
		
		//call function
		ArrayList<OccModel> getList = occService.getPage(page, filter);
		
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
		when(occDao.getRowNum(filter)).thenReturn(rowNum);
		
		//verify
		assertEquals(totalPage, occService.getTotalPage(filter));
	}
	
	@Test
	public void testUpdate() {
		OccModel occupation = RandomData.genOcc();
		String id = occupation.getId();
		String name = occupation.getName();
		Boolean state = occupation.getState();
		
		//call function
		occService.update(id, name, state);
		
		//verify
		verify(occDao).update(argThat(data -> data.getId().equals(id)
										   && data.getName().equals(name)
										   && data.getState() == state));
	}
	
	@Test
	public void testDelete() {
		String id = UUID.randomUUID().toString();
		
		//call function
		occService.delete(id);
		
		//verify
		verify(occDao).delete(id);
	}
}








