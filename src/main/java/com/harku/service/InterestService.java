package com.harku.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.InterestDao;
import com.harku.model.Interest;

@Service
public class InterestService {
	@Autowired
	private InterestDao interestDao;
	
	private final int ENTRY_PER_PAGE = 10;
	
	public void createInt(String name, Boolean state) {
		Interest newData = new Interest();
		
		newData.setId(UUID.randomUUID().toString());
		newData.setName(name);
		newData.setState(state);
		interestDao.create(newData);
	}
	
	/**
	 * 
	 * @param id
	 * @return interest data, null if not found
	 */
	public Interest getInterest(String id) {
		Interest filter = new Interest();
		filter.setId(id);
		ArrayList<Interest> interestList = interestDao.read(filter, 0, 1);
		
		if(interestList.isEmpty()) return null;
		return interestList.get(0);
	}
	
	public ArrayList<Interest> getPage(int page, Interest filter) {
		if(page <= 0) return new ArrayList<Interest>();
		
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return interestDao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public ArrayList<Interest> getList() {
		Interest filter = new Interest();
		filter.setState(true);
		return interestDao.read(filter);
	}
	
	public int getTotalPage(Interest filter) {
		int rowNum = interestDao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(String id, String name, Boolean state) {
		Interest newData = new Interest();
		
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		interestDao.update(newData);
	}
	
	public void delete(String id) {
		interestDao.delete(id);
	}
}
