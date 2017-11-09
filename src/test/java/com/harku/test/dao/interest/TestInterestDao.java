package com.harku.test.dao.interest;

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

import com.harku.dao.interest.IntDao;
import com.harku.model.interest.IntModel;
import com.harku.test.config.AppConfigTest;
import com.harku.test.util.RandomData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Transactional
public class TestInterestDao {
	@Autowired
	private IntDao interestDao;
	
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
			IntModel newInterest = RandomData.genInterest();
			newInterest.setName(interestNamePattern + RandomData.genStr(5, 10));
			interestDao.create(newInterest);
			idList.add(newInterest.getId());
		}
		
		//read
		IntModel filter = new IntModel();
		filter.setName("testInterest");
		List<IntModel> readInterestList = interestDao.read(filter, 0, 10);
		for(IntModel interest : readInterestList) assertTrue(idList.contains(interest.getId()));
	}
	
	@Test
	public void testReadAll() {
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<100; ++i) {
			IntModel newInterest = RandomData.genInterest();
			interestDao.create(newInterest);
			idList.add(newInterest.getId());
		}
		
		//read
		List<IntModel> readInterestList = interestDao.read(new IntModel());
		for(IntModel interest : readInterestList) assertTrue(idList.contains(interest.getId()));
	}
	
	@Test
	public void testGetRowNum() {
		int createNum = (int)(Math.random()*100);
		
		//create
		for(int i=0; i<createNum; ++i) {
			IntModel newInterest = RandomData.genInterest();
			interestDao.create(newInterest);
		}
		
		//read
		assertEquals(createNum, interestDao.getRowNum(new IntModel()));
	}
	
	@Test
	public void testUpdate() {
		//create
		IntModel interest = RandomData.genInterest();
		interestDao.create(interest);
		
		//update
		String newName = "test update name";
		Boolean newState = !interest.getState();
		IntModel updateData = new IntModel();
		updateData.setId(interest.getId());
		updateData.setName(newName);
		updateData.setState(newState);
		interestDao.update(updateData);
		
		//verify
		IntModel readInterest = interestDao.read(new IntModel()).get(0);
		assertEquals(newName, readInterest.getName());
		assertEquals(newState, readInterest.getState());
	}
	
	@Test
	public void testDelete() {
		int createNum = (int)(Math.random()*100 + 50);
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<createNum; ++i) {
			IntModel newInterest = RandomData.genInterest();
			interestDao.create(newInterest);
			idList.add(newInterest.getId());
		}
		
		//delete
		for(String id : idList) interestDao.delete(id);
		
		//verify
		List<IntModel> readInterestList = interestDao.read(new IntModel());
		assertTrue(readInterestList.isEmpty());
	}
}
