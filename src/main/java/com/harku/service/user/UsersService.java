package com.harku.service.user;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.user.UsersDao;
import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;

@Service
public class UsersService{
	private final int ENTRY_PER_PAGE = 10;
	@Autowired
	private UsersDao dao;
	@Autowired
	private UserIntService UIS;
	@Autowired
	private UserAccService UAS;
	
	/**
	 * 
	 * @param newData
	 * @return id id of the created user
	 */
	public String createUser(UsersModel newData) {
		String id = UUID.randomUUID().toString();
		String[] interest = newData.getInterest();
		
		newData.setId(id);
		
		UAS.saveAcc(newData);
		UIS.saveInterests(id, interest);
		dao.create(newData);
		
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
		
		ArrayList<UsersModel> userList = dao.read(filter, 0, 1);
		if(userList.isEmpty()) return null;
		UsersModel user = userList.get(0);
		
		//set values from other tables
		user.setInterest(UIS.getInterests(id));
		UsersModel userAcc = UAS.getAccById(id);
		user.setAccount(userAcc.getAccount());
		user.setPassword(userAcc.getPassword());
		user.setState(userAcc.getState());
		
		return user;
	}
	
	public ArrayList<UsersModel> getPage(int page, UserFilterModel filter) {
		if(page <= 0) return new ArrayList<UsersModel>();
		
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		ArrayList<UsersModel> userList = dao.read(filter, skipNum, ENTRY_PER_PAGE);
		
		//set values from other table for each user
		for(UsersModel user : userList) {
			user.setInterest(UIS.getInterests(user.getId()));
			UsersModel userAcc = UAS.getAccById(user.getId());
			user.setAccount(userAcc.getAccount());
			user.setPassword(userAcc.getPassword());
			user.setState(userAcc.getState());
		}
		
		return userList;
	}
	
	public int getTotalPage(UserFilterModel filter) {
		int rowNum = dao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(UsersModel newData) {
		String userId = newData.getId();
		String[] interestList = newData.getInterest();
		
		UIS.updateInterests(userId, interestList);
		
		//when it comes to updating account and password, do it in the "change password" feature
		UAS.updateAcc(newData);
		
		dao.update(newData);
	}
	
	public void delete(String id) {
		UIS.delInterests(id);
		UAS.delAcc(id);
		dao.delete(id);
	}
}
