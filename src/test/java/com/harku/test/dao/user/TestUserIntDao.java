package com.harku.test.dao.user;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.harku.dao.user.UserIntDao;
import com.harku.test.config.AppConfigTest;
import com.harku.test.util.RandomData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Transactional
public class TestUserIntDao {
	@Autowired
	private UserIntDao userIntDao;
	
	@Test
	public void testCreate() {
		String userId = UUID.randomUUID().toString();
		String interest = RandomData.genStr(5, 10);
		userIntDao.create(userId, interest);
	}
	
	@Test
	public void testRead() {
		String userId = UUID.randomUUID().toString();
		Set<String> interestSet = RandomData.genInterestSet(1, 10);
		
		for(String interest : interestSet) userIntDao.create(userId, interest);
		
		Set<String> readInterestSet = new HashSet<String>(userIntDao.read(userId));
		
		assertTrue(interestSet.equals(readInterestSet));		
	}
	
	@Test
	public void testDelete() {
		String userId = UUID.randomUUID().toString();
		Set<String> interestSet = RandomData.genInterestSet(1, 10);
		
		for(String interest : interestSet) userIntDao.create(userId, interest);
		
		userIntDao.delete(userId);
		
		List<String> readInterestList = userIntDao.read(userId);
		assertTrue(readInterestList.isEmpty());
	}
}