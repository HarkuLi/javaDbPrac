package service.user;

import java.util.ArrayList;

import dao.user.UserIntDao;

public class UserIntService {
	private UserIntDao dao;
	
	public UserIntService() {
		dao = new UserIntDao();
	}
	
	public void saveInterests(String id, String[] interestList) {
		for(String interestId : interestList) {
			dao.create(id, interestId);
		}
	}
	
	/**
	 * return a list of interest id
	 * @param id {String}
	 * @return {ArrayList<String>}
	 */
	public ArrayList<String> getInterests(String id) {
		return dao.read(id);
	}
	
	public void delInterests(String id) {
		
	}
}
