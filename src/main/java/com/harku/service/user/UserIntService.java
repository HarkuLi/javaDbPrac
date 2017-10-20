package com.harku.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.user.UserIntDao;

@Service
public class UserIntService {
	@Autowired
	private UserIntDao dao;
	
	public void saveInterests(String userId, String[] interestList) {
		if(interestList == null) return;
		for(String interestId : interestList) {
			dao.create(userId, interestId);
		}
	}
	
	public String[] getInterests(String userId) {
		return dao.read(userId).toArray(new String[0]);
	}
	
	public void updateInterests(String userId, String[] interestList) {
		delInterests(userId);
		saveInterests(userId, interestList);
	}
	
	public void delInterests(String userId) {
		dao.delete(userId);
	}
}
