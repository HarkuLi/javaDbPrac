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

import com.harku.dao.InterestDao;
import com.harku.model.Interest;
import com.harku.test.config.AppConfigTest;
import com.harku.test.util.RandomData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Transactional
public class TestInterestDao {
	@Autowired
	private InterestDao interestDao;
	
	@Test
	public void testCreate() {
		interestDao.create(RandomData.genInterest());
	}
	
	@Test
	public void testReadSome() {
		String interestNamePattern = "testInterest";
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<10; ++i) {
			Interest newInterest = RandomData.genInterest();
			newInterest.setName(interestNamePattern + RandomData.genStr(5, 10));
			interestDao.create(newInterest);
			idList.add(newInterest.getId());
		}
		
		//read
		Interest filter = new Interest();
		filter.setName("testInterest");
		List<Interest> readInterestList = interestDao.read(filter, 0, 10);
		for(Interest interest : readInterestList) assertTrue(idList.contains(interest.getId()));
	}
	
	@Test
	public void testReadAll() {
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<100; ++i) {
			Interest newInterest = RandomData.genInterest();
			interestDao.create(newInterest);
			idList.add(newInterest.getId());
		}
		
		//read
		List<Interest> readInterestList = interestDao.read(new Interest());
		for(Interest interest : readInterestList) assertTrue(idList.contains(interest.getId()));
	}
	
	@Test
	public void testGetRowNum() {
		int createNum = (int)(Math.random()*100);
		
		//create
		for(int i=0; i<createNum; ++i) {
			Interest newInterest = RandomData.genInterest();
			interestDao.create(newInterest);
		}
		
		//read
		assertEquals(createNum, interestDao.getRowNum(new Interest()));
	}
	
	@Test
	public void testUpdate() {
		//create
		Interest interest = RandomData.genInterest();
		interestDao.create(interest);
		
		//update
		String newName = "test update name";
		String newState = interest.getState().equals("1") ? "0" : "1";
		Interest updateData = new Interest();
		updateData.setId(interest.getId());
		updateData.setName(newName);
		updateData.setState(newState);
		interestDao.update(updateData);
		
		//verify
		Interest readInterest = interestDao.read(new Interest()).get(0);
		assertEquals(newName, readInterest.getName());
		assertEquals(newState, readInterest.getState());
	}
	
	@Test
	public void testDelete() {
		int createNum = (int)(Math.random()*100 + 50);
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<createNum; ++i) {
			Interest newInterest = RandomData.genInterest();
			interestDao.create(newInterest);
			idList.add(newInterest.getId());
		}
		
		//delete
		for(String id : idList) interestDao.delete(id);
		
		//verify
		List<Interest> readInterestList = interestDao.read(new Interest());
		assertTrue(readInterestList.isEmpty());
	}
}
