package service.user;

import java.util.ArrayList;

import dao.user.UserIntDao;

public class UserIntService {
	private UserIntDao dao;
	
	public UserIntService() {
		dao = new UserIntDao();
	}
	
	public void saveInterests(String userId, String[] interestList) {
		for(String interestId : interestList) {
			dao.create(userId, interestId);
		}
	}
	
	/**
	 * return a list of interest id
	 * @param userId {String}
	 * @return {ArrayList<String>}
	 */
	public ArrayList<String> getInterests(String userId) {
		return dao.read(userId);
	}
	
	public void updateInterests(String userId, String[] interestList) {
		dao.delete(userId);
		for(String interestId : interestList) {
			dao.create(userId, interestId);
		}
	}
	
	public void delInterests(String userId) {
		dao.delete(userId);
	}
}
