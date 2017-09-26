package service.user;

import java.util.ArrayList;

import dao.user.UserIntDao;

public class UserIntService {
	private UserIntDao dao;
	
	public UserIntService(UserIntDao _dao) {
		dao = _dao;
	}
	
	public void saveInterests(String userId, String[] interestList) {
		if(interestList == null) return;
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
		delInterests(userId);
		saveInterests(userId, interestList);
	}
	
	public void delInterests(String userId) {
		dao.delete(userId);
	}
}
