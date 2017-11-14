package com.harku.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.UsersDao;
import com.harku.model.UserFilterModel;
import com.harku.model.UsersModel;

@Service
public class UsersService{
	private final int ENTRY_PER_PAGE = 10;
	@Autowired
	private UsersDao usersDao;
	@Autowired
	private UserIntService userInterestService;
	@Autowired
	private UserAccService userAccountService;
	
	/**
	 * 
	 * @param newData
	 * @return id id of the created user
	 */
	public String createUser(UsersModel newData) {
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
	public UsersModel getUser(String id) {
		UserFilterModel filter = new UserFilterModel();
		filter.setId(id);
		
		ArrayList<UsersModel> userList = usersDao.read(filter, 0, 1);
		if(userList.isEmpty()) return null;
		UsersModel user = userList.get(0);
		
		//set values from other tables
		user.setInterest(userInterestService.getInterests(id));
		UsersModel userAcc = userAccountService.getAccById(id);
		user.setAccount(userAcc.getAccount());
		user.setPassword(userAcc.getPassword());
		user.setState(userAcc.getState());
		
		return user;
	}
	
	public ArrayList<UsersModel> getPage(int page, UserFilterModel filter) {
		if(page <= 0) return new ArrayList<UsersModel>();
		
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		ArrayList<UsersModel> userList = usersDao.read(filter, skipNum, ENTRY_PER_PAGE);
		
		//set values from other table for each user
		for(UsersModel user : userList) {
			user.setInterest(userInterestService.getInterests(user.getId()));
			UsersModel userAcc = userAccountService.getAccById(user.getId());
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
	
	public void update(UsersModel newData) {
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
