package com.harku.test.dao.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.harku.dao.user.UserAccDao;
import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;
import com.harku.test.config.AppConfigTest;
import com.harku.test.util.RandomData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Transactional
public class TestUserAccDao {
	@Autowired
	private UserAccDao userAccDao;
	
	@Test
	public void testCreate() {
		userAccDao.create(RandomData.genUser());
	}
	
	@Test
	public void testReadOne() {
		UsersModel newUser = RandomData.genUser();
		userAccDao.create(newUser);
		
		UsersModel readUser = userAccDao.read(new UserFilterModel()).get(0);
		
		assertEquals(newUser.getAccount(), readUser.getAccount());
	}
	
	@Test
	public void testReadMany() {
		Set<String> accountSet = new HashSet<String>();
		
		//create users and record their accounts
		for(int i=0; i<100; ++i) {
			UsersModel newUser = RandomData.genUser();
			userAccDao.create(newUser);
			accountSet.add(newUser.getAccount());
		}
		
		//read and get account set
		List<UsersModel> readUserList = userAccDao.read(new UserFilterModel());
		Set<String> readAccountSet = new HashSet<String>();
		for(UsersModel user : readUserList) readAccountSet.add(user.getAccount());
		
		//compare the read set and original set
		assertTrue(readAccountSet.equals(accountSet));
	}
	
	@Test
	public void testUpdate() {
		//create random data
		UsersModel newData = RandomData.genUser();
		userAccDao.create(newData);
		
		//update
		String newAccount = "testUpdateAccount";
		String newPassword = BCrypt.hashpw("testUpdatePassword", BCrypt.gensalt(RandomData.workload));
		Boolean newState = !newData.getState();
		Long newSignInTime = System.currentTimeMillis();
		String newToken = "testUpdateToken";
		UsersModel setData = new UsersModel();
		setData.setId(newData.getId());
		setData.setAccount(newAccount);
		setData.setPassword(newPassword);
		setData.setState(newState);
		setData.setSignInTime(newSignInTime);
		setData.setToken(newToken);
		userAccDao.update(setData);
		
		//read
		UserFilterModel filter = new UserFilterModel();
		UsersModel readUser = userAccDao.read(filter).get(0);
		assertEquals(newAccount   , readUser.getAccount());
		assertEquals(newPassword  , readUser.getPassword());
		assertEquals(newState     , readUser.getState());
		assertEquals(newSignInTime, readUser.getSignInTime());
		assertEquals(newToken     , readUser.getToken());
	}
	
	@Test
	public void testDelete() {
		List<String> idList = new ArrayList<String>();
		
		//create random data
		for(int i=0; i<100; ++i) {
			UsersModel newData = RandomData.genUser();
			userAccDao.create(newData);
			idList.add(newData.getId());
		}
		
		//delete
		for(String id : idList) userAccDao.delete(id);
		
		assertTrue(userAccDao.read(new UserFilterModel()).isEmpty());
	}
}
