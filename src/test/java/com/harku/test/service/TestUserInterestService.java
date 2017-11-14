package com.harku.test.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.harku.dao.UserInterestDao;
import com.harku.service.UserInterestService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserInterestService {
	@Mock
	private UserInterestDao userInterestDao;
	
	@Autowired
	@InjectMocks
	private UserInterestService userInterestService;
	
	@Test
	public void testSaveInterests() {
		String userId = UUID.randomUUID().toString();
		int interestNum = (int)(Math.random()*10);
		String[] interests = (interestNum==0) ? null : new String[interestNum];
		
		//set interests
		for(int i=0; i<interestNum; ++i) {
			interests[i] = UUID.randomUUID().toString();
		}
		
		//call function
		userInterestService.saveInterests(userId, interests);
		
		verify(userInterestDao, times(interestNum)).create(anyString(), anyString());
	}
	
	@Test
	public void testGetInterests() {
		String userId = UUID.randomUUID().toString();
		ArrayList<String> interests = new ArrayList<String>(RandomData.genInterestSet(5, 10));
		
		//set Stub
		when(userInterestDao.read(userId)).thenReturn(interests);
		
		//call function
		String[] rst = userInterestService.getInterests(userId);
		
		assertTrue(Arrays.equals(interests.toArray(new String[0]), rst));
	}
	
	@Test
	public void testUpdateInterests() {
		String userId = UUID.randomUUID().toString();
		String[] interests = RandomData.genInterestSet(5, 10).toArray(new String[0]);
		
		//call function
		userInterestService.updateInterests(userId, interests);
		
		verify(userInterestDao).delete(userId);
		verify(userInterestDao, times(interests.length)).create(anyString(), anyString());
	}
	
	@Test
	public void testDelInterests() {
		String userId = UUID.randomUUID().toString();
		
		//call function
		userInterestService.delInterests(userId);
		
		verify(userInterestDao).delete(userId);
	}
}
