package com.harku.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.config.ConstantConfig;
import com.harku.dao.UserAccountDao;
import com.harku.model.User;
import com.harku.model.UserFilter;

@Service
public class UserAccountService {
	@Autowired
	private UserAccountDao userAccountDao;
	
	public void saveAcc(User newData) {
		userAccountDao.create(newData);
	}
	
	public boolean isAccExist(String account) {
		UserFilter filter = new UserFilter();
		filter.setAccount(account);
		
		ArrayList<User> accList = userAccountDao.read(filter);
		if(!accList.isEmpty()) return true;
		
		return false;
	}
	
	public User getAcc(String account) {
		UserFilter filter = new UserFilter();
		filter.setAccount(account);
		
		ArrayList<User> accList = userAccountDao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	public User getAccById(String userId) {
		UserFilter filter = new UserFilter();
		filter.setId(userId);
		
		ArrayList<User> accList = userAccountDao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	/**
	 * 
	 * @param token
	 * @return the account has the token, null if not found
	 */
	public User getAccByToken(String token) {
		UserFilter filter = new UserFilter();
		filter.setToken(token);
		
		ArrayList<User> accList = userAccountDao.read(filter);
		
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
		
		UserFilter filter = new UserFilter();
		filter.setToken(token);
		
		ArrayList<User> accList = userAccountDao.read(filter);
		
		if(accList.isEmpty()) return false;
		
		//check expire time
		User acc = accList.get(0);
		long currentTime = System.currentTimeMillis();
		long signInTime = acc.getSignInTime();
		long elapsedTime = (long)(currentTime - signInTime);
		if(elapsedTime >= ConstantConfig.EXPIRE_TIME_SEC*1000) return false;
		
		return true;
	}
	
	public void updateAcc(User setData) {
		userAccountDao.update(setData);
	}
	
	public void delAcc(String userId) {
		userAccountDao.delete(userId);
	}
}
