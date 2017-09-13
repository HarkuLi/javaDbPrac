package service.user;

import java.util.ArrayList;
import java.util.HashMap;

import dao.user.UserAccDao;

public class UserAccService {
	private UserAccDao dao;
	
	public UserAccService() {
		dao = new UserAccDao();
	}
	
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
	 * 
	 * @param account {String}
	 * @return {HashMap<String, Object>} {userId: String, account: String, password: String, state: boolean}, null if not found
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
	 * @return {HashMap<String, Object>} {userId: String, account: String, password: String, state: boolean}, null if not found
	 */
	public HashMap<String, Object> getAccById(String userId) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("userId", userId);
		
		ArrayList<HashMap<String, Object>> accList = dao.read(filter);
		
		if(accList.isEmpty()) return null;
		return accList.get(0);
	}
	
	public void updateAcc(HashMap<String, Object> setData) {
		dao.update(setData);
	}
	
	public void delAcc(String userId) {
		dao.delete(userId);
	}
}
