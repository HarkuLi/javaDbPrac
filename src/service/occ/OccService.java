package service.occ;

import java.util.ArrayList;
import java.util.HashMap;

import dao.occ.OccDao;
import model.occ.OccModel;

public class OccService {
	private OccDao dao;
	private final int ENTRY_PER_PAGE = 10;
	
	public OccService() {
		dao = new OccDao();
	}
	
	public void createOcc(String name, String state) {
		HashMap<String, Object> newData = new HashMap<String, Object>();
		
		newData.put("name", name);
		newData.put("state", state.equals("1"));
		dao.create(newData);
	}
	
	public OccModel getOcc(String id) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", id);
		ArrayList<OccModel> occList = dao.read(filter, 0, 1);
		return occList.get(0);
	}
	
	public ArrayList<OccModel> getList() {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("state", "1");
		return dao.read(filter);
	}
	
	public ArrayList<OccModel> getPage(int page, HashMap<String, String> filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return dao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public int getTotalPage(HashMap<String, String> filter) {
		int rowNum = dao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	public void update(String id, String name, String state) {
		HashMap<String, Object> newData = new HashMap<String, Object>();
		
		newData.put("id", id);
		newData.put("name", name);
		newData.put("state", state.equals("1"));
		dao.update(newData);
	}
	
	public void delete(String id) {
		dao.delete(id);
	}
}
