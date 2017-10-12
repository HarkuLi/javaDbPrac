package com.harku.service.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.user.UserAccDao;

@Service
public class UserAccService {
	@Autowired
	private UserAccDao dao;
	
	public static final int EXPIRE_TIME_SEC = 604800;	//one week, 60*60*24*7
	
	public void saveAcc(HashMap<String, Object> newData) {
		dao.create(newData);
	}
	
	public boolean isAccExist(String account) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("account", account);
		
		ArrayList<HashMap<String, Object>> acc = dao.read(filter);
		if(!acc.isEmpty()) return true;
		
		return false;
	}
	
	/**
	 * return the user account
	 * @param account {String}
	 * @return {HashMap<String, Object>} null if not found
	 * 		{
	 * 			userId: String,
	 * 			account: String,
	 * 			password: String,
	 * 			state: boolean,
	 * 			signInTime: long,
	 * 			token: String
	 * 		}
	 */
	public HashMap<String, Object> getAcc(String account) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("account", account);
		
		ArrayList<HashMap<String, Object>> accList = dao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	/**
	 * return the user account
	 * @param userId {String}
	 * @return {HashMap<String, Object>} null if not found
	 * 		{
	 * 			userId: String,
	 * 			account: String,
	 * 			password: String,
	 * 			state: boolean,
	 * 			signInTime: long,
	 * 			token: String
	 * 		}
	 */
	public HashMap<String, Object> getAccById(String userId) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);
		
		ArrayList<HashMap<String, Object>> accList = dao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	/**
	 * return the user account
	 * @param token {String}
	 * @return {HashMap<String, Object>} null if not found
	 * 		{
	 * 			userId: String,
	 * 			account: String,
	 * 			password: String,
	 * 			state: boolean,
	 * 			signInTime: long,
	 * 			token: String
	 * 		}
	 */
	public HashMap<String, Object> getAccByToken(String token) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("token", token);
		
		ArrayList<HashMap<String, Object>> accList = dao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	/**
	 * check whether the token is valid
	 * @param token {String}
	 * @return {boolean} 
	 */
	public boolean checkToken(String token) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("token", token);
		
		ArrayList<HashMap<String, Object>> accList = dao.read(filter);
		
		if(accList.isEmpty()) return false;
		
		//check expire time
		HashMap<String, Object> acc = accList.get(0);
		long currentTime = System.currentTimeMillis();
		long signInTime = (Long)acc.get("signInTime");
		int elapsedTime = (int)(currentTime - signInTime);
		if(elapsedTime >= EXPIRE_TIME_SEC*1000) return false;
		
		return true;
	}
	
	public void updateAcc(HashMap<String, Object> setData) {
		dao.update(setData);
	}
	
	public void delAcc(String userId) {
		dao.delete(userId);
	}
}
