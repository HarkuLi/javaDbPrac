package com.harku.service.user;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.user.UserAccDao;
import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;

@Service
public class UserAccService {
	@Autowired
	private UserAccDao dao;
	
	public static final int EXPIRE_TIME_SEC = 604800;	//one week, 60*60*24*7
	
	public void saveAcc(UsersModel newData) {
		dao.create(newData);
	}
	
	public boolean isAccExist(String account) {
		UserFilterModel filter = new UserFilterModel();
		filter.setAccount(account);
		
		ArrayList<UsersModel> accList = dao.read(filter);
		if(!accList.isEmpty()) return true;
		
		return false;
	}
	
	public UsersModel getAcc(String account) {
		UserFilterModel filter = new UserFilterModel();
		filter.setAccount(account);
		
		ArrayList<UsersModel> accList = dao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	public UsersModel getAccById(String userId) {
		UserFilterModel filter = new UserFilterModel();
		filter.setId(userId);
		
		ArrayList<UsersModel> accList = dao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	public UsersModel getAccByToken(String token) {
		UserFilterModel filter = new UserFilterModel();
		filter.setToken(token);
		
		ArrayList<UsersModel> accList = dao.read(filter);
		
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
		
		ArrayList<UsersModel> accList = dao.read(filter);
		
		if(accList.isEmpty()) return false;
		
		//check expire time
		UsersModel acc = accList.get(0);
		long currentTime = System.currentTimeMillis();
		long signInTime = acc.getSignInTime();
		long elapsedTime = (long)(currentTime - signInTime);
		if(elapsedTime >= EXPIRE_TIME_SEC*1000) return false;
		
		return true;
	}
	
	public void updateAcc(UsersModel setData) {
		dao.update(setData);
	}
	
	public void delAcc(String userId) {
		dao.delete(userId);
	}
}
