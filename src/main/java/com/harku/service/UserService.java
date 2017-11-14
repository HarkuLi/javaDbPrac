package com.harku.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.UserDao;
import com.harku.model.UserFilterModel;
import com.harku.model.UserModel;

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
	public String createUser(UserModel newData) {
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
	public UserModel getUser(String id) {
		UserFilterModel filter = new UserFilterModel();
		filter.setId(id);
		
		ArrayList<UserModel> userList = usersDao.read(filter, 0, 1);
		if(userList.isEmpty()) return null;
		UserModel user = userList.get(0);
		
		//set values from other tables
		user.setInterest(userInterestService.getInterests(id));
		UserModel userAcc = userAccountService.getAccById(id);
		user.setAccount(userAcc.getAccount());
		user.setPassword(userAcc.getPassword());
		user.setState(userAcc.getState());
		
		return user;
	}
	
	public ArrayList<UserModel> getPage(int page, UserFilterModel filter) {
		if(page <= 0) return new ArrayList<UserModel>();
		
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		ArrayList<UserModel> userList = usersDao.read(filter, skipNum, ENTRY_PER_PAGE);
		
		//set values from other table for each user
		for(UserModel user : userList) {
			user.setInterest(userInterestService.getInterests(user.getId()));
			UserModel userAcc = userAccountService.getAccById(user.getId());
			user.setAccount(userAcc.getAccount());
			user.setPassword(userAcc.getPassword());
			user.setState(userAcc.getState());
		}
		
		return userList;
	}
	
	public int getTotalPage(UserFilterModel filter) {
		int rowNum = usersDao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(UserModel newData) {
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
