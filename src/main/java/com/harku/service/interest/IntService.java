package com.harku.service.interest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.interest.IntDao;
import com.harku.model.interest.IntModel;

@Service
public class IntService {
	@Autowired
	private IntDao dao;
	
	private final int ENTRY_PER_PAGE = 10;
	
	public void createInt(String name, Boolean state) {
		IntModel newData = new IntModel();
		
		newData.setName(name);
		newData.setState(state);
		dao.create(newData);
	}
	
	public IntModel getInterest(String id) {
		IntModel filter = new IntModel();
		filter.setId(id);
		ArrayList<IntModel> interestList = dao.read(filter, 0, 1);
		return interestList.get(0);
	}
	
	public ArrayList<IntModel> getPage(int page, IntModel filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return dao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public ArrayList<IntModel> getList() {
		IntModel filter = new IntModel();
		filter.setState(true);
		return dao.read(filter);
	}
	
	public int getTotalPage(IntModel filter) {
		int rowNum = dao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(String id, String name, Boolean state) {
		IntModel newData = new IntModel();
		
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		dao.update(newData);
	}
	
	public void delete(String id) {
		dao.delete(id);
	}
}
