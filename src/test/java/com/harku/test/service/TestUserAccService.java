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

import com.harku.dao.user.UserAccDao;
import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserAccService {
	@Mock
	private UserAccDao userAccDao;
	
	@Autowired
	@InjectMocks
	private UserAccService UAS;
	
	@Test
	public void testSaveAcc() {
		UsersModel newData = new UsersModel();
		
		//call function
		UAS.saveAcc(newData);
		
		verify(userAccDao).create(newData);
	}
	
	@Test
	public void testIsAccExist_exist() {
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(new UsersModel());
		String existAcc = RandomData.genStr(1, 32);
		
		//set Stub
		when(userAccDao.read(argThat(filter -> filter.getAccount().equals(existAcc))))
			.thenReturn(accList);
		
		assertTrue(UAS.isAccExist(existAcc));
	}
	
	@Test
	public void testIsAccExist_notExist() {
		ArrayList<UsersModel> emptyList = new ArrayList<UsersModel>();
		String notExistAcc = RandomData.genStr(1, 32);
		
		//set Stub
		when(userAccDao.read(argThat(filter -> filter.getAccount().equals(notExistAcc))))
			.thenReturn(emptyList);
		
		assertFalse(UAS.isAccExist(notExistAcc));
	}
	
	@Test
	public void testGetAcc() {
		UsersModel acc = RandomData.genUser();
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccDao.read(argThat(filter -> filter.getAccount().equals(acc.getAccount()))))
			.thenReturn(accList);
		
		//call function
		UsersModel getAcc = UAS.getAcc(acc.getAccount());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testGetAccById() {
		UsersModel acc = RandomData.genUser();
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccDao.read(argThat(filter -> filter.getId().equals(acc.getId()))))
			.thenReturn(accList);
		
		//call function
		UsersModel getAcc = UAS.getAccById(acc.getId());
		
		assertEquals(acc.getId(), getAcc.getId());
	}
	
	@Test
	public void testGetAccByToken() {
		UsersModel acc = RandomData.genUser();
		ArrayList<UsersModel> accList = new ArrayList<UsersModel>();
		accList.add(acc);
		
		//set Stub
		when(userAccDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		//call function
		UsersModel getAcc = UAS.getAccByToken(acc.getToken());
		
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
		when(userAccDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		assertTrue(UAS.checkToken(acc.getToken()));
	}
	
	@Test
	public void testCheckToken_notExistToken() {
		String notExistToken = "not existing token";
		
		ArrayList<UsersModel> emptyAccList = new ArrayList<UsersModel>();
		
		//set Stub
		when(userAccDao.read(argThat(filter -> filter.getToken().equals(notExistToken))))
			.thenReturn(emptyAccList);
		
		assertFalse(UAS.checkToken(notExistToken));
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
		when(userAccDao.read(argThat(filter -> filter.getToken().equals(acc.getToken()))))
			.thenReturn(accList);
		
		assertFalse(UAS.checkToken(acc.getToken()));
	}
	
	@Test
	public void testUpdateAcc() {
		UsersModel setData = new UsersModel();
		
		//call function
		UAS.updateAcc(setData);
		
		verify(userAccDao).update(setData);
	}
	
	@Test
	public void testDelAcc() {
		String userId = UUID.randomUUID().toString();
		
		//call function
		UAS.delAcc(userId);
		
		verify(userAccDao).delete(userId);
	}
}
