package com.harku.service.occ;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harku.dao.occ.OccDao;
import com.harku.model.occ.OccModel;

@Service
public class OccService {
	@Autowired
	private OccDao dao;
	
	private final int ENTRY_PER_PAGE = 10;
	
	public void createOcc(String name, Boolean state) {
		OccModel newData = new OccModel();
		
		newData.setId(UUID.randomUUID().toString());
		newData.setName(name);
		newData.setState(state);
		dao.create(newData);
	}
	
	public OccModel getOcc(String id) {
		OccModel filter = new OccModel();
		filter.getId();
		ArrayList<OccModel> occList = dao.read(filter, 0, 1);
		return occList.get(0);
	}
	
	public ArrayList<OccModel> getList() {
		OccModel filter = new OccModel();
		filter.setState(true);
		return dao.read(filter);
	}
	
	public ArrayList<OccModel> getPage(int page, OccModel filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return dao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public int getTotalPage(OccModel filter) {
		int rowNum = dao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(String id, String name, Boolean state) {
		OccModel newData = new OccModel();
		
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		dao.update(newData);
	}
	
	public void delete(String id) {
		dao.delete(id);
	}
}
