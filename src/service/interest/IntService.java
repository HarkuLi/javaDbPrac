package service.interest;

import java.util.ArrayList;
import java.util.HashMap;

import dao.interest.IntDao;
import model.interest.IntModel;

public class IntService {
	private IntDao dao;
	private final int ENTRY_PER_PAGE = 10;
	
	public IntService() {
		dao = new IntDao();
	}
	
	public void createInt(String name, String state) {
		HashMap<String, Object> newData = new HashMap<String, Object>();
		
		newData.put("name", name);
		newData.put("state", state.equals("1"));
		dao.create(newData);
	}
	
	public IntModel getInterest(String id) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", id);
		ArrayList<IntModel> interestList = dao.read(filter, 0, 1);
		return interestList.get(0);
	}
	
	public ArrayList<IntModel> getPage(int page, HashMap<String, String> filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return dao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public ArrayList<IntModel> getList() {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("state", "1");
		return dao.read(filter);
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
