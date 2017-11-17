package com.harku.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.config.ConstantConfig;
import com.harku.dao.OccupationDao;
import com.harku.model.Occupation;

@Service
public class OccupationService {
	@Autowired
	private OccupationDao occupationDao;
	
	public void createOcc(String name, Boolean state) {
		Occupation newData = new Occupation();
		
		newData.setId(UUID.randomUUID().toString());
		newData.setName(name);
		newData.setState(state);
		occupationDao.create(newData);
	}
	
	/**
	 * 
	 * @param id
	 * @return occupation data, null if not found
	 */
	public Occupation getOcc(String id) {
		Occupation filter = new Occupation();
		filter.setId(id);
		ArrayList<Occupation> occList = occupationDao.read(filter, 0, 1);
		
		if(occList.isEmpty()) return null;
		return occList.get(0);
	}
	
	public ArrayList<Occupation> getList() {
		Occupation filter = new Occupation();
		filter.setState(true);
		return occupationDao.read(filter);
	}
	
	public ArrayList<Occupation> getPage(int page, Occupation filter) {
		if(page <= 0) return new ArrayList<Occupation>();
		
		int skipNum = ConstantConfig.ENTRY_PER_PAGE * (page - 1);
		
		return occupationDao.read(filter, skipNum, ConstantConfig.ENTRY_PER_PAGE);
	}
	
	public int getTotalPage(Occupation filter) {
		int rowNum = occupationDao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ConstantConfig.ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(String id, String name, Boolean state) {
		Occupation newData = new Occupation();
		
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		occupationDao.update(newData);
	}
	
	public void delete(String id) {
		occupationDao.delete(id);
	}
}
