package com.harku.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.IntDao;
import com.harku.model.IntModel;

@Service
public class IntService {
	@Autowired
	private IntDao interestDao;
	
	private final int ENTRY_PER_PAGE = 10;
	
	public void createInt(String name, Boolean state) {
		IntModel newData = new IntModel();
		
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
	public IntModel getInterest(String id) {
		IntModel filter = new IntModel();
		filter.setId(id);
		ArrayList<IntModel> interestList = interestDao.read(filter, 0, 1);
		
		if(interestList.isEmpty()) return null;
		return interestList.get(0);
	}
	
	public ArrayList<IntModel> getPage(int page, IntModel filter) {
		if(page <= 0) return new ArrayList<IntModel>();
		
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return interestDao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public ArrayList<IntModel> getList() {
		IntModel filter = new IntModel();
		filter.setState(true);
		return interestDao.read(filter);
	}
	
	public int getTotalPage(IntModel filter) {
		int rowNum = interestDao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(String id, String name, Boolean state) {
		IntModel newData = new IntModel();
		
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		interestDao.update(newData);
	}
	
	public void delete(String id) {
		interestDao.delete(id);
	}
}