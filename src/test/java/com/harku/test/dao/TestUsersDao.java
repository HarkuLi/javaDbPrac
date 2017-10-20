package com.harku.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.harku.dao.user.UsersDao;
import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;
import com.harku.test.config.AppConfigTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Transactional
public class TestUsersDao {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private UsersDao userDao;
	
	@Test
	public void testCreate() {
		userDao.create(genRandomData());
	}
	
	@Test
	public void testReadOne() {
		//create random data
		UsersModel newData = genRandomData();
		userDao.create(newData);
		
		//read
		UserFilterModel filter = new UserFilterModel();
		filter.setId(newData.getId());
		ArrayList<UsersModel> readRows = userDao.read(filter, 0, 1);
		assertEquals(newData.getName()     , readRows.get(0).getName());
		assertEquals(newData.getPhotoName(), readRows.get(0).getPhotoName());
	}
	
	@Test
	public void testReadMany() {
		List<String> idList = new ArrayList<String>();
		
		//create random data
		for(int i=0; i<100; ++i) {
			UsersModel newData = genRandomData();
			idList.add(newData.getId());
			userDao.create(newData);
		}
		
		//read
		List<UsersModel> readRows = userDao.read(new UserFilterModel(), 0, 10000);
		for(UsersModel user : readRows) {
			assertTrue(idList.indexOf(user.getId()) >= 0);
		}
	}
	
	@Test
	public void testUpdate() {
		//create random data
		UsersModel newData = genRandomData();
		String id = newData.getId();
		userDao.create(newData);
		
		//update
		String newName = "test update name";
		Integer newAge = 13;
		String newBirth = "2017-01-01";
		String newPhotoName = "testPhotoName.gif";
		String newOccupation = "testOccupation";
		UsersModel setData = new UsersModel();
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
		List<UsersModel> readRows = userDao.read(filter, 0, 1);
		assertEquals(newName      , readRows.get(0).getName());
		assertEquals(newAge       , readRows.get(0).getAge());
		assertEquals(newBirth     , readRows.get(0).getBirth());
		assertEquals(newPhotoName , readRows.get(0).getPhotoName());
		assertEquals(newOccupation, readRows.get(0).getOccupation());
	}
	
	@Test
	public void testGetRowNum() {
		//create random numbers of data
		int rowNum = (int)(Math.random()*100 + 1);
		for(int i=0; i<rowNum; ++i) userDao.create(genRandomData());
		
		//check number of rows
		assertEquals(rowNum, userDao.getRowNum(new UserFilterModel()));
	}
	
	@Test
	public void testDelete() {
		List<String> idList = new ArrayList<String>();
		
		//create random data
		for(int i=0; i<100; ++i) {
			UsersModel newData = genRandomData();
			idList.add(newData.getId());
			userDao.create(newData);
		}
		
		//delete
		for(String id : idList) userDao.delete(id);
		
		assertEquals(0, userDao.getRowNum(new UserFilterModel()));
	}
	
	private UsersModel genRandomData() {
		String id = UUID.randomUUID().toString();
		String name = genRandomStr(3, 10);
		int age = (int)(Math.random()*80 + 1);
		String birth = genRandomBirth();
		String photoName = UUID.randomUUID().toString() + "." + genImageType();
		String occupation = genRandomStr(5, 10);
		
		UsersModel newData = new UsersModel();
		newData.setId(id);
		newData.setName(name);
		newData.setAge(age);
		newData.setBirth(birth);
		newData.setPhotoName(photoName);
		newData.setOccupation(occupation);;
		return newData;
	}
	
	private String genRandomStr(int minLength, int maxLength) {
		int lengthRange = maxLength - minLength + 1;
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
		String rst = "";
		int contentLen = (int)(Math.random()*lengthRange + minLength);
		
		for(int i=0; i<contentLen; ++i) {
			int idx = (int)(Math.random()*charSet.length());
			rst += charSet.charAt(idx);
		}
		return rst;
	}
	
	private String genRandomBirth() {
		long currentTime = System.currentTimeMillis();
		long randomTime = (long)(Math.random()*currentTime);
		Date date = new Date(randomTime);
 
		return sdf.format(date).toString();
	}
 
	private String genImageType() {
		String[] imageTypes = {"jpeg", "png", "gif"};
		int idx = (int)(Math.random()*3);
		return imageTypes[idx];
	}
}
