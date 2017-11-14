package com.harku.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.OccupationDao;
import com.harku.model.OccupationModel;

@Service
public class OccupationService {
	@Autowired
	private OccupationDao occupationDao;
	
	private final int ENTRY_PER_PAGE = 10;
	
	public void createOcc(String name, Boolean state) {
		OccupationModel newData = new OccupationModel();
		
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
	public OccupationModel getOcc(String id) {
		OccupationModel filter = new OccupationModel();
		filter.setId(id);
		ArrayList<OccupationModel> occList = occupationDao.read(filter, 0, 1);
		
		if(occList.isEmpty()) return null;
		return occList.get(0);
	}
	
	public ArrayList<OccupationModel> getList() {
		OccupationModel filter = new OccupationModel();
		filter.setState(true);
		return occupationDao.read(filter);
	}
	
	public ArrayList<OccupationModel> getPage(int page, OccupationModel filter) {
		if(page <= 0) return new ArrayList<OccupationModel>();
		
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return occupationDao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public int getTotalPage(OccupationModel filter) {
		int rowNum = occupationDao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(String id, String name, Boolean state) {
		OccupationModel newData = new OccupationModel();
		
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		occupationDao.update(newData);
	}
	
	public void delete(String id) {
		occupationDao.delete(id);
	}
}
