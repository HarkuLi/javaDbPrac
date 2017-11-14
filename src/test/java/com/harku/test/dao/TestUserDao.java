package com.harku.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.harku.dao.UserDao;
import com.harku.model.UserFilterModel;
import com.harku.model.UserModel;
import com.harku.test.config.AppConfigTest;
import com.harku.test.util.RandomData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Transactional
public class TestUserDao {
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testCreate() {
		userDao.create(RandomData.genUser());
	}
	
	@Test
	public void testReadOne() {
		//create random data
		UserModel newData = RandomData.genUser();
		userDao.create(newData);
		
		//read
		UserFilterModel filter = new UserFilterModel();
		filter.setId(newData.getId());
		ArrayList<UserModel> readRows = userDao.read(filter, 0, 1);
		assertEquals(newData.getName()     , readRows.get(0).getName());
		assertEquals(newData.getPhotoName(), readRows.get(0).getPhotoName());
	}
	
	@Test
	public void testReadMany() {
		List<String> idList = new ArrayList<String>();
		
		//create random data
		for(int i=0; i<100; ++i) {
			UserModel newData = RandomData.genUser();
			idList.add(newData.getId());
			userDao.create(newData);
		}
		
		//read
		List<UserModel> readRows = userDao.read(new UserFilterModel(), 0, 10000);
		for(UserModel user : readRows) {
			assertTrue(idList.indexOf(user.getId()) >= 0);
		}
	}
	
	@Test
	public void testUpdate() {
		//create random data
		UserModel newData = RandomData.genUser();
		String id = newData.getId();
		userDao.create(newData);
		
		//update
		String newName = "test update name";
		Integer newAge = 13;
		String newBirth = "2017-01-01";
		String newPhotoName = "testPhotoName.gif";
		String newOccupation = "testOccupation";
		UserModel setData = new UserModel();
		setData.setId(id);
		setData.setName(newName);
		setData.setAge(newAge);;
		setData.setBirth(newBirth);;
		setData.setPhotoName(newPhotoName);;
		setData.setOccupation(newOccupation);;
		userDao.update(setData);
		
		//read
		UserFilterModel filter = new UserFilterModel();
		filter.setId(id);
		UserModel readUser = userDao.read(filter, 0, 1).get(0);
		assertEquals(newName      , readUser.getName());
		assertEquals(newAge       , readUser.getAge());
		assertEquals(newBirth     , readUser.getBirth());
		assertEquals(newPhotoName , readUser.getPhotoName());
		assertEquals(newOccupation, readUser.getOccupation());
	}
	
	@Test
	public void testGetRowNum() {
		//create random numbers of data
		int rowNum = (int)(Math.random()*100 + 1);
		for(int i=0; i<rowNum; ++i) userDao.create(RandomData.genUser());
		
		//check number of rows
		assertEquals(rowNum, userDao.getRowNum(new UserFilterModel()));
	}
	
	@Test
	public void testDelete() {
		List<String> idList = new ArrayList<String>();
		
		//create random data
		for(int i=0; i<100; ++i) {
			UserModel newData = RandomData.genUser();
			idList.add(newData.getId());
			userDao.create(newData);
		}
		
		//delete
		for(String id : idList) userDao.delete(id);
		
		assertEquals(0, userDao.getRowNum(new UserFilterModel()));
	}
}
