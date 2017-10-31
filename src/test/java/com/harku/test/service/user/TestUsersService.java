package com.harku.test.service.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.harku.dao.user.UsersDao;
import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UserIntService;
import com.harku.service.user.UsersService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUsersService {
	private static UsersModel userTestData;
	private static UsersModel userAccountTestData;
	private static String[] interestsTestData;
	
	@Mock
	private UsersDao userDao;
	
	@Mock
	private UserIntService UIS;
	
	@Mock
	private UserAccService UAS;
	
	@Autowired
	@InjectMocks
	private UsersService US;
	
	@Before
	public void before() {
		prepareTestData();
	}
	
	@Test
	public void testCreateUser() {
		UsersModel newData = RandomData.genUser();
		String id = US.createUser(newData);
		
		assertNotNull(id);
		verify(UAS).saveAcc(newData);
		verify(UIS).saveInterests(id, newData.getInterest());
		verify(userDao).create(newData);
	}
	
	@Test
	public void testGetUser() {
		//set stub
		ArrayList<UsersModel> userList = new ArrayList<UsersModel>();
		userList.add(new UsersModel(userTestData));
		
		when(userDao.read(ArgumentMatchers.any(UserFilterModel.class),
						  ArgumentMatchers.anyInt(),
						  ArgumentMatchers.anyInt())).thenReturn(userList);
		when(UIS.getInterests(userTestData.getId())).thenReturn(interestsTestData);
		when(UAS.getAccById(userTestData.getId())).thenReturn(userAccountTestData);
		
		UsersModel user = US.getUser(userTestData.getId());
		assertEquals(userTestData.getName(), user.getName());
		assertEquals(userAccountTestData.getAccount(), user.getAccount());
		assertEquals(userAccountTestData.getPassword(), user.getPassword());
		assertEquals(userAccountTestData.getState(), user.getState());
		assertTrue(Arrays.equals(interestsTestData, user.getInterest()));
	}
	
	//check called times of getInterests() and getAccById() are equal to the number of user
	@Test
	public void testGetPage() {
		//set stub
		int userNum = (int)(Math.random()*50 + 50);
		ArrayList<UsersModel> userList = new ArrayList<UsersModel>();
		for(int i=0; i<userNum; ++i) userList.add(RandomData.genUser());
		when(UAS.getAccById(ArgumentMatchers.anyString())).thenReturn(new UsersModel());
		when(userDao.read(ArgumentMatchers.any(UserFilterModel.class),
						  ArgumentMatchers.anyInt(),
						  ArgumentMatchers.anyInt())).thenReturn(userList);
		
		US.getPage(1, new UserFilterModel());
		verify(UIS, times(userNum)).getInterests(ArgumentMatchers.anyString());
		verify(UAS, times(userNum)).getAccById(ArgumentMatchers.anyString());
	}
	
	@Test
	public void testGetInvalidPage() {
		int page = -(int)(Math.random()*10);
		assertTrue(US.getPage(page, new UserFilterModel()).isEmpty());
	}
	
	@Test
	public void testGetTotalPage() {
		UserFilterModel filter = new UserFilterModel();
		
		US.getTotalPage(filter);
		
		verify(userDao).getRowNum(filter);
	}
	
	@Test
	public void testUpdate() {
		UsersModel user = RandomData.genUser();
		
		US.update(user);
		
		verify(UIS).updateInterests(user.getId(), user.getInterest());
		verify(UAS).updateAcc(user);
		verify(userDao).update(user);
	}
	
	@Test
	public void testDelete() {
		String id = UUID.randomUUID().toString();
		
		US.delete(id);
		
		verify(UIS).delInterests(id);
		verify(UAS).delAcc(id);
		verify(userDao).delete(id);
	}
	
	private void prepareTestData() {
		userTestData = RandomData.genUser();
		
		userAccountTestData = new UsersModel();
		userAccountTestData.setAccount(userTestData.getAccount());
		userAccountTestData.setPassword(userTestData.getPassword());
		userAccountTestData.setState(userTestData.getState());
		userTestData.setAccount(null);
		userTestData.setPassword(null);
		userTestData.setState(null);
		
		interestsTestData = userTestData.getInterest();
		userTestData.setInterest(null);
	}
}
