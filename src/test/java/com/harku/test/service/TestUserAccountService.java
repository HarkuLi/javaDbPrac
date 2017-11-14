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

import com.harku.dao.UserAccountDao;
import com.harku.model.UserModel;
import com.harku.service.UserAccountService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserAccountService {
	@Mock
	private UserAccountDao userAccountDao;
	
	@Autowired
	@InjectMocks
	private UserAccountService userAccountService;
	
	@Test
	public void testSaveAcc() {
		UserModel newData = new UserModel();
		
		//call function
		userAccountService.saveAcc(newData);
		
		verify(userAccountDao).create(newData);
	}
	
	@Test
	public void testIsAccExist_exist() {
		ArrayList<UserModel> accList = new ArrayList<UserModel>();
		accList.add(new UserModel());
		String existAcc = RandomData.genStr(1, 32);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getAccount().equals(existAcc))))
			.thenReturn(accList);
		
		assertTrue(userAccountService.isAccExist(existAcc));
	}
	
	@Test
	public void testIsAccExist_notExist() {
		ArrayList<UserModel> emptyList = new ArrayList<UserModel>();
		String notExistAcc = RandomData.genStr(1, 32);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getAccount().equals(notExistAcc))))
			.thenReturn(emptyList);
		
		assertFalse(userAccountService.isAccExist(notExistAcc));
	}
	
	@Test
	public void testGetAcc() {
		UserModel acc = RandomData.genUser();
		ArrayList<UserModel> accList = new ArrayList<UserModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getAccount().equals(acc.getAccount()))))
			.thenReturn(accList);
		
		//call function
		UserModel getAcc = userAccountService.getAcc(acc.getAccount());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testGetAccById() {
		UserModel acc = RandomData.genUser();
		ArrayList<UserModel> accList = new ArrayList<UserModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getId().equals(acc.getId()))))
			.thenReturn(accList);
		
		//call function
		UserModel getAcc = userAccountService.getAccById(acc.getId());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testGetAccByToken() {
		UserModel acc = RandomData.genUser();
		ArrayList<UserModel> accList = new ArrayList<UserModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		//call function
		UserModel getAcc = userAccountService.getAccByToken(acc.getToken());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testCheckToken_validToken() {
		int deviationSec = 5;
		UserModel acc = RandomData.genUser();
		
		//set valid sign in time
		long currentTime = System.currentTimeMillis();
		long earliestValidTime = currentTime - (UserAccountService.EXPIRE_TIME_SEC - deviationSec)*1000;
		long validRandomTime = (long)(Math.random()*(UserAccountService.EXPIRE_TIME_SEC - deviationSec)*1000 + earliestValidTime);
		acc.setSignInTime(validRandomTime);
		
		ArrayList<UserModel> accList = new ArrayList<UserModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		assertTrue(userAccountService.checkToken(acc.getToken()));
	}
	
	@Test
	public void testCheckToken_notExistToken() {
		String notExistToken = "not existing token";
		
		ArrayList<UserModel> emptyAccList = new ArrayList<UserModel>();
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(notExistToken))))
			.thenReturn(emptyAccList);
		
		assertFalse(userAccountService.checkToken(notExistToken));
	}
	
	@Test
	public void testCheckToken_expiredToken() {
		int deviationSec = 5;
		UserModel acc = RandomData.genUser();
		
		//set expired sign in time
		long currentTime = System.currentTimeMillis();
		long earliestValidTime = currentTime - (UserAccountService.EXPIRE_TIME_SEC - deviationSec)*1000;
		long expiredRandomTime = (long)(Math.random()*(earliestValidTime-deviationSec*2*1000));
		acc.setSignInTime(expiredRandomTime);
		
		ArrayList<UserModel> accList = new ArrayList<UserModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccountDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		assertFalse(userAccountService.checkToken(acc.getToken()));
	}
	
	@Test
	public void testUpdateAcc() {
		UserModel setData = new UserModel();
		
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
