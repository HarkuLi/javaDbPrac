package com.harku.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.argThat;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.harku.dao.UserAccDao;
import com.harku.model.UsersModel;
import com.harku.service.UserAccService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserAccService {
	@Mock
	private UserAccDao userAccountDao;
	
	@Autowired
	@InjectMocks
	private UserAccService userAccountService;
	
	@Test
	public void testSaveAcc() {
		UsersModel newData = new UsersModel();
		
		//call function
		userAccountService.saveAcc(newData);
		
		verify(userAccountDao).create(newData);
	}
	
	@Test
	public void testIsAccExist_exist() {
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(new UsersModel());
		String existAcc = RandomData.genStr(1, 32);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getAccount().equals(existAcc))))
			.thenReturn(accList);
		
		assertTrue(userAccountService.isAccExist(existAcc));
	}
	
	@Test
	public void testIsAccExist_notExist() {
		ArrayList<UsersModel> emptyList = new ArrayList<UsersModel>();
		String notExistAcc = RandomData.genStr(1, 32);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getAccount().equals(notExistAcc))))
			.thenReturn(emptyList);
		
		assertFalse(userAccountService.isAccExist(notExistAcc));
	}
	
	@Test
	public void testGetAcc() {
		UsersModel acc = RandomData.genUser();
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getAccount().equals(acc.getAccount()))))
			.thenReturn(accList);
		
		//call function
		UsersModel getAcc = userAccountService.getAcc(acc.getAccount());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testGetAccById() {
		UsersModel acc = RandomData.genUser();
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getId().equals(acc.getId()))))
			.thenReturn(accList);
		
		//call function
		UsersModel getAcc = userAccountService.getAccById(acc.getId());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testGetAccByToken() {
		UsersModel acc = RandomData.genUser();
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		//call function
		UsersModel getAcc = userAccountService.getAccByToken(acc.getToken());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testCheckToken_validToken() {
		int deviationSec = 5;
		UsersModel acc = RandomData.genUser();
		
		//set valid sign in time
		long currentTime = System.currentTimeMillis();
		long earliestValidTime = currentTime - (UserAccService.EXPIRE_TIME_SEC - deviationSec)*1000;
		long validRandomTime = (long)(Math.random()*(UserAccService.EXPIRE_TIME_SEC - deviationSec)*1000 + earliestValidTime);
		acc.setSignInTime(validRandomTime);
		
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		assertTrue(userAccountService.checkToken(acc.getToken()));
	}
	
	@Test
	public void testCheckToken_notExistToken() {
		String notExistToken = "not existing token";
		
		ArrayList<UsersModel> emptyAccList = new ArrayList<UsersModel>();
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(notExistToken))))
			.thenReturn(emptyAccList);
		
		assertFalse(userAccountService.checkToken(notExistToken));
	}
	
	@Test
	public void testCheckToken_expiredToken() {
		int deviationSec = 5;
		UsersModel acc = RandomData.genUser();
		
		//set expired sign in time
		long currentTime = System.currentTimeMillis();
		long earliestValidTime = currentTime - (UserAccService.EXPIRE_TIME_SEC - deviationSec)*1000;
		long expiredRandomTime = (long)(Math.random()*(earliestValidTime-deviationSec*2*1000));
		acc.setSignInTime(expiredRandomTime);
		
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		assertFalse(userAccountService.checkToken(acc.getToken()));
	}
	
	@Test
	public void testUpdateAcc() {
		UsersModel setData = new UsersModel();
		
		//call function
		userAccountService.updateAcc(setData);
		
		verify(userAccountDao).update(setData);
	}
	
	@Test
	public void testDelAcc() {
		String userId = UUID.randomUUID().toString();
		
		//call function
		userAccountService.delAcc(userId);
		
		verify(userAccountDao).delete(userId);
	}
}
