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
		ArrayList<IntModel> interestList = dao.read(
			"*",
			"id = '" + id + "'",
			"1"
		);
		return interestList.get(0);
	}
	
	public ArrayList<IntModel> getPage(int page, HashMap<String, String> filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		String where = null;
		
		//paging
		String limit = Integer.toString(skipNum);
		limit += "," + Integer.toString(ENTRY_PER_PAGE);
		
		//filters
		String filterStr = filterQueryStr(filter);
		if(filterStr.length() != 0) where = filterStr;
		
		return dao.read(
			"*",
			where,
			limit
		);
	}
	
	public ArrayList<IntModel> getList() {
		return dao.read("*", "state = true", null);
	}
	
	public int getTotalPage(HashMap<String, String> filter) {
		String where = filterQueryStr(filter);
		int rowNum;
		
		rowNum = where.length()==0 ? dao.getRowNum() : dao.getRowNum(where);
		
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
	
	private String filterQueryStr(HashMap<String, String> filter) {
		String rst = "";
		String name = filter.get("name");
		String state = filter.get("state");
		
		if(name != null) {
			rst += "name like '%" + name + "%'";
		}
		if(state != null) {
			if(rst.length() != 0) rst += " and ";
			rst += "state = " + state;
		}
		
		return rst;
	}
}
