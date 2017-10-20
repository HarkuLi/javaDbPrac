package com.harku.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.harku.dao.user.UsersDao;
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
		HashMap<String, Object> newData = genRandomData();
		userDao.create(newData);
		
		//read
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", newData.get("id"));
		ArrayList<UsersModel> readRows = userDao.read(filter, 0, 1);
		assertEquals((String)newData.get("name"), readRows.get(0).getName());
		assertEquals((String)newData.get("photoName"), readRows.get(0).getPhotoName());
	}
	
	@Test
	public void testReadMany() {
		List<String> idList = new ArrayList<String>();
		
		//create random data
		for(int i=0; i<100; ++i) {
			HashMap<String, Object> newData = genRandomData();
			idList.add((String)newData.get("id"));
			userDao.create(newData);
		}
		
		//read
		List<UsersModel> readRows = userDao.read(new HashMap<String, Object>(), 0, 10000);
		for(UsersModel user : readRows) {
			assertTrue(idList.indexOf(user.getId()) >= 0);
		}
	}
	
	@Test
	public void testUpdate() {
		//create random data
		HashMap<String, Object> newData = genRandomData();
		String id = (String)newData.get("id");
		userDao.create(newData);
		
		//update
		String newName = "test update name";
		int newAge = 13;
		String newBirth = "2017-01-01";
		String newPhotoName = "testPhotoName.gif";
		String newOccupation = "testOccupation";
		HashMap<String, Object> setData = new HashMap<String, Object>();
		setData.put("id", id);
		setData.put("name", newName);
		setData.put("age", newAge);
		setData.put("birth", newBirth);
		setData.put("photoName", newPhotoName);
		setData.put("occupation", newOccupation);
		userDao.update(setData);
		
		//read
		HashMap<String, Object> filter = new HashMap<String, Object>();
		//filter.put("id", id);
		List<UsersModel> readRows = userDao.read(filter, 0, 1);
		assertEquals(readRows.get(0).getName(), newName);
		assertEquals(readRows.get(0).getAge(), newAge);
		assertEquals(readRows.get(0).getBirth(), newBirth);
		assertEquals(readRows.get(0).getPhotoName(), newPhotoName);
		assertEquals(readRows.get(0).getOccupation(), newOccupation);
	}
	
	@Test
	public void testGetRowNum() {
		//create random numbers of data
		int rowNum = (int)(Math.random()*100 + 1);
		for(int i=0; i<rowNum; ++i) userDao.create(genRandomData());
		
		//check number of rows
		assertEquals(userDao.getRowNum(new HashMap<String, Object>()), rowNum);
	}
	
	@Test
	public void testDelete() {
		List<String> idList = new ArrayList<String>();
		
		//create random data
		for(int i=0; i<100; ++i) {
			HashMap<String, Object> newData = genRandomData();
			idList.add((String)newData.get("id"));
			userDao.create(newData);
		}
		
		//delete
		for(String id : idList) userDao.delete(id);
	}
	
	private HashMap<String, Object> genRandomData() {
		String id = UUID.randomUUID().toString();
		String name = genRandomStr(3, 10);
		int age = (int)(Math.random()*80 + 1);
		String birth = genRandomBirth();
		String photoName = UUID.randomUUID().toString() + "." + genImageType();
		String occupation = genRandomStr(5, 10);
		
		HashMap<String, Object> newData = new HashMap<String, Object>();
		newData.put("id", id);
		newData.put("name", name);
		newData.put("age", age);
		newData.put("birth", birth);
		newData.put("photoName", photoName);
		newData.put("occupation", occupation);
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
