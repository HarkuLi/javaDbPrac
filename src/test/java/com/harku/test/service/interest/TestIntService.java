package com.harku.test.service.interest;

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

import com.harku.dao.interest.IntDao;
import com.harku.model.interest.IntModel;
import com.harku.service.interest.IntService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestIntService {
	@Mock
	private IntDao interestDao;
	
	@Autowired
	@InjectMocks
	private IntService interestService;
	
	@Test
	public void testCreateInt() {
		IntModel interest = RandomData.genInterest();
		
		//call function
		interestService.createInt(interest.getName(), interest.getState());
		
		//verify
		verify(interestDao).create(argThat(newData -> newData.getName().equals(interest.getName())
											  && newData.getState() == interest.getState()));
	}
	
	@Test
	public void testGetInterest() {
		IntModel interest = RandomData.genInterest();
		ArrayList<IntModel> interestList = new ArrayList<IntModel>();
		interestList.add(interest);
		
		//set Stub
		when(interestDao.read(argThat(filter -> filter.getId().equals(interest.getId())), eq(0), eq(1)))
			.thenReturn(interestList);
		
		//call function
		IntModel getInterest = interestService.getInterest(interest.getId());
		
		//verify
		assertEquals(interest.getName(), getInterest.getName());
		assertEquals(interest.getState(), getInterest.getState());
	}
	
	@Test
	public void testGetList() {
		//generate List
		int interestNum = (int)(Math.random()*10 + 10);
		ArrayList<IntModel> interestList = new ArrayList<IntModel>();
		for(int i=0; i<interestNum; ++i) interestList.add(RandomData.genInterest());
		
		//set Stub
		when(interestDao.read(argThat(filter -> filter.getState())))
			.thenReturn(interestList);
		
		//call function and verify
		assertTrue(interestService.getList().equals(interestList));
	}
	
	@Test
	public void testGetPage() {
		IntModel filter = new IntModel();
		int entryPerPage = 10;
		int page = (int)(Math.random()*10 +1);
		int skipNum = entryPerPage * (page - 1);
		
		//generate return value for Stub
		int listNum = (int)(Math.random()*10 +1);
		ArrayList<IntModel> interestList = new ArrayList<IntModel>();
		for(int i=0; i<listNum; ++i) interestList.add(new IntModel());
		
		//set Stub
		when(interestDao.read(filter, skipNum, entryPerPage))
			.thenReturn(interestList);
		
		//call function
		ArrayList<IntModel> getList = interestService.getPage(page, filter);
		
		//verify
		assertEquals(interestList, getList);
	}
	
	@Test
	public void testGetTotalPage() {
		IntModel filter = new IntModel();
		int entryPerPage = 10;
		int rowNum = (int)(Math.random()*100 +10);
		int totalPage = (int) Math.ceil((double) rowNum / entryPerPage);
		
		//set Stub
		when(interestDao.getRowNum(filter)).thenReturn(rowNum);
		
		//verify
		assertEquals(totalPage, interestService.getTotalPage(filter));
	}
	
	@Test
	public void testUpdate() {
		IntModel interest = RandomData.genInterest();
		String id = interest.getId();
		String name = interest.getName();
		Boolean state = interest.getState();
		
		//call function
		interestService.update(id, name, state);
		
		//verify
		verify(interestDao).update(argThat(data -> data.getId().equals(id)
										   && data.getName().equals(name)
										   && data.getState() == state));
	}
	
	@Test
	public void testDelete() {
		String id = UUID.randomUUID().toString();
		
		//call function
		interestService.delete(id);
		
		//verify
		verify(interestDao).delete(id);
	}
}
