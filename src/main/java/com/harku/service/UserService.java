package com.harku.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.UserDao;
import com.harku.model.UserFilter;
import com.harku.model.User;

@Service
public class UserService{
	private final int ENTRY_PER_PAGE = 10;
	@Autowired
	private UserDao usersDao;
	@Autowired
	private UserInterestService userInterestService;
	@Autowired
	private UserAccountService userAccountService;
	
	/**
	 * 
	 * @param newData
	 * @return id id of the created user
	 */
	public String createUser(User newData) {
		String id = UUID.randomUUID().toString();
		String[] interest = newData.getInterest();
		
		newData.setId(id);
		
		userAccountService.saveAcc(newData);
		userInterestService.saveInterests(id, interest);
		usersDao.create(newData);
		
		return id;
	}
	
	/**
	 * 
	 * @param id
	 * @return user, or null if not found
	 */
	public User getUser(String id) {
		UserFilter filter = new UserFilter();
		filter.setId(id);
		
		ArrayList<User> userList = usersDao.read(filter, 0, 1);
		if(userList.isEmpty()) return null;
		User user = userList.get(0);
		
		//set values from other tables
		user.setInterest(userInterestService.getInterests(id));
		User userAcc = userAccountService.getAccById(id);
		user.setAccount(userAcc.getAccount());
		user.setPassword(userAcc.getPassword());
		user.setState(userAcc.getState());
		
		return user;
	}
	
	public ArrayList<User> getPage(int page, UserFilter filter) {
		if(page <= 0) return new ArrayList<User>();
		
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		ArrayList<User> userList = usersDao.read(filter, skipNum, ENTRY_PER_PAGE);
		
		//set values from other table for each user
		for(User user : userList) {
			user.setInterest(userInterestService.getInterests(user.getId()));
			User userAcc = userAccountService.getAccById(user.getId());
			user.setAccount(userAcc.getAccount());
			user.setPassword(userAcc.getPassword());
			user.setState(userAcc.getState());
		}
		
		return userList;
	}
	
	public int getTotalPage(UserFilter filter) {
		int rowNum = usersDao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(User newData) {
		String userId = newData.getId();
		String[] interestList = newData.getInterest();
		
		userInterestService.updateInterests(userId, interestList);
		
		//when it comes to updating account and password, do it in the "change password" feature
		userAccountService.updateAcc(newData);
		
		usersDao.update(newData);
	}
	
	public void delete(String id) {
		userInterestService.delInterests(id);
		userAccountService.delAcc(id);
		usersDao.delete(id);
	}
}
