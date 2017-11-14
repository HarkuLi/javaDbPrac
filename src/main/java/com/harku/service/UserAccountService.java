package com.harku.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.UserAccountDao;
import com.harku.model.UserFilterModel;
import com.harku.model.UserModel;

@Service
public class UserAccountService {
	@Autowired
	private UserAccountDao userAccountDao;
	
	public static final int EXPIRE_TIME_SEC = 604800;	//one week, 60*60*24*7
	
	public void saveAcc(UserModel newData) {
		userAccountDao.create(newData);
	}
	
	public boolean isAccExist(String account) {
		UserFilterModel filter = new UserFilterModel();
		filter.setAccount(account);
		
		ArrayList<UserModel> accList = userAccountDao.read(filter);
		if(!accList.isEmpty()) return true;
		
		return false;
	}
	
	public UserModel getAcc(String account) {
		UserFilterModel filter = new UserFilterModel();
		filter.setAccount(account);
		
		ArrayList<UserModel> accList = userAccountDao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	public UserModel getAccById(String userId) {
		UserFilterModel filter = new UserFilterModel();
		filter.setId(userId);
		
		ArrayList<UserModel> accList = userAccountDao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	/**
	 * 
	 * @param token
	 * @return the account has the token, null if not found
	 */
	public UserModel getAccByToken(String token) {
		UserFilterModel filter = new UserFilterModel();
		filter.setToken(token);
		
		ArrayList<UserModel> accList = userAccountDao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	/**
	 * check whether the token is valid
	 * @param token {String}
	 * @return {boolean}
	 */
	public boolean checkToken(String token) {
		if(token == null) return false;
		
		UserFilterModel filter = new UserFilterModel();
		filter.setToken(token);
		
		ArrayList<UserModel> accList = userAccountDao.read(filter);
		
		if(accList.isEmpty()) return false;
		
		//check expire time
		UserModel acc = accList.get(0);
		long currentTime = System.currentTimeMillis();
		long signInTime = acc.getSignInTime();
		long elapsedTime = (long)(currentTime - signInTime);
		if(elapsedTime >= EXPIRE_TIME_SEC*1000) return false;
		
		return true;
	}
	
	public void updateAcc(UserModel setData) {
		userAccountDao.update(setData);
	}
	
	public void delAcc(String userId) {
		userAccountDao.delete(userId);
	}
}
