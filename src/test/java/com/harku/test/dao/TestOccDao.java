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

import com.harku.dao.OccDao;
import com.harku.model.OccModel;
import com.harku.test.config.AppConfigTest;
import com.harku.test.util.RandomData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Transactional
public class TestOccDao {
	@Autowired
	private OccDao occupationDao;
	
	@Test
	public void testCreate() {
		occupationDao.create(RandomData.genOcc());
	}
	
	@Test
	public void testReadSome() {
		String occNamePattern = "testOcc";
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<10; ++i) {
			OccModel newOcc = RandomData.genOcc();
			newOcc.setName(occNamePattern + RandomData.genStr(5, 10));
			occupationDao.create(newOcc);
			idList.add(newOcc.getId());
		}
		
		//read
		OccModel filter = new OccModel();
		filter.setName("testOcc");
		List<OccModel> readOccList = occupationDao.read(filter, 0, 10);
		for(OccModel occ : readOccList) assertTrue(idList.contains(occ.getId()));
	}
	
	@Test
	public void testReadAll() {
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<100; ++i) {
			OccModel newOcc = RandomData.genOcc();
			occupationDao.create(newOcc);
			idList.add(newOcc.getId());
		}
		
		//read
		List<OccModel> readOccList = occupationDao.read(new OccModel());
		for(OccModel occ : readOccList) assertTrue(idList.contains(occ.getId()));
	}
	
	@Test
	public void testGetRowNum() {
		int createNum = (int)(Math.random()*100);
		
		//create
		for(int i=0; i<createNum; ++i) {
			OccModel newOcc = RandomData.genOcc();
			occupationDao.create(newOcc);
		}
		
		//read
		assertEquals(createNum, occupationDao.getRowNum(new OccModel()));
	}
	
	@Test
	public void testUpdate() {
		//create
		OccModel occ = RandomData.genOcc();
		occupationDao.create(occ);
		
		//update
		String newName = "test update name";
		Boolean newState = !occ.getState();
		OccModel updateData = new OccModel();
		updateData.setId(occ.getId());
		updateData.setName(newName);
		updateData.setState(newState);
		occupationDao.update(updateData);
		
		//verify
		OccModel readOcc = occupationDao.read(new OccModel()).get(0);
		assertEquals(newName, readOcc.getName());
		assertEquals(newState, readOcc.getState());
	}
	
	@Test
	public void testDelete() {
		int createNum = (int)(Math.random()*100 + 50);
		List<String> idList = new ArrayList<String>();
		
		//create
		for(int i=0; i<createNum; ++i) {
			OccModel newOcc = RandomData.genOcc();
			occupationDao.create(newOcc);
			idList.add(newOcc.getId());
		}
		
		//delete
		for(String id : idList) occupationDao.delete(id);
		
		//verify
		List<OccModel> readOccList = occupationDao.read(new OccModel());
		assertTrue(readOccList.isEmpty());
	}
}