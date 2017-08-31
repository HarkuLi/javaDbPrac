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
	
	public void delInterests(String id) {
		
	}
	
	public ArrayList<String> getInterestList(String id){
		return dao.read(id);
	}
}
