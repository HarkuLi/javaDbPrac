package com.harku.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.harku.dao.user.UsersDao;
import com.harku.model.user.UsersModel;

public class UsersService{
	private final int ENTRY_PER_PAGE = 10;
	private UsersDao dao;
	private UserIntService UIS;
	private UserAccService UAS;
	
	public UsersService(UsersDao _dao, UserIntService _UIS, UserAccService _UAS) {
		dao = _dao;
		UIS = _UIS;
		UAS = _UAS;
	}
	
	/**
	 * 
	 * @param newData {HashMap<String, Object>}
	 * 		{
	 *       	name: String,
	 *       	account: String,
	 *       	password: String,
	 * 		 	age: int,
	 *       	birth: String,
	 *       	photoName: String,    //not required
	 *       	interest: String[],
	 * 			occupation: String,
	 * 			state: boolean,
	 *      }
	 */
	public void createUser(HashMap<String, Object> newData) {
		String id = UUID.randomUUID().toString();
		String account = (String)newData.get("account");
		String password = (String)newData.get("password");
		boolean state = (boolean)newData.get("state");
		String[] interest = (String[])newData.get("interest");
		
		HashMap<String, Object> newAccData = new HashMap<String, Object>();
		newAccData.put("userId", id);
		newAccData.put("account", account);
		newAccData.put("password", password);
		newAccData.put("state", state);
		UAS.saveAcc(newAccData);
		
		UIS.saveInterests(id, interest);
		
		//modify the passed data
		//add data created in the service and remove the data used
		newData.put("id", id);
		newData.remove("account");
		newData.remove("password");
		newData.remove("state");
		newData.remove("interest");
		
		dao.create(newData);
	}
	
	public UsersModel getUser(String id) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", id);
		
		ArrayList<UsersModel> userList = dao.read(filter, 0, 1);
		UsersModel user = userList.get(0);
		
		//set values from other tables
		HashMap<String, Object> userAcc = UAS.getAccById(id);
		user.setInterest(UIS.getInterests(id));
		user.setAccount((String)userAcc.get("account"));
		user.setPassword((String)userAcc.get("password"));
		user.setState((boolean)userAcc.get("state"));
		
		return user;
	}
	
	public ArrayList<UsersModel> getPage(int page, HashMap<String, Object> filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		ArrayList<UsersModel> userList = dao.read(filter, skipNum, ENTRY_PER_PAGE);
		
		//set values from other table for each user
		for(UsersModel user : userList) {
			user.setInterest(UIS.getInterests(user.getId()));
			HashMap<String, Object> userAcc = UAS.getAccById(user.getId());
			user.setAccount((String)userAcc.get("account"));
			user.setPassword((String)userAcc.get("password"));
			user.setState((boolean)userAcc.get("state"));
		}
		
		return userList;
	}
	
	public int getTotalPage(HashMap<String, Object> filter) {
		int rowNum = dao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	/**
	 * 
	 * @param newData {HashMap<String, Object>}
	 * 		{
	 * 			id: String,	//required
	 * 			name: String,
	 * 			age: int,
	 * 			birth: String,
	 * 			photoName: String,
	 * 			interest: String[],
	 * 			occupation: String,
	 * 			state: boolean
	 * 		}
	 */
	public void update(HashMap<String, Object> newData) {
		String[] interestList = (String[])newData.get("interest");
		boolean state = (boolean)newData.get("state");
		String userId = (String) newData.get("id");
		
		UIS.updateInterests(userId, interestList);
		
		//about updating account and password, do it in the "change password" feature
		HashMap<String, Object> setAccData = new HashMap<String, Object>();
		setAccData.put("userId", userId);
		setAccData.put("state", state);
		UAS.updateAcc(setAccData);
		
		//modify the passed data
		//remove the data used
		newData.remove("interest");
		newData.remove("state");
		
		dao.update(newData);
	}
	
	public void delete(String id) {
		UIS.delInterests(id);
		UAS.delAcc(id);
		dao.delete(id);
	}
}
