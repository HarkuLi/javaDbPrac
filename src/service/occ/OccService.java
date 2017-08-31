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
		ArrayList<OccModel> occList = dao.read(
			"*",
			"id = '" + id + "'",
			"1"
		);
		return occList.get(0);
	}
	
	public ArrayList<OccModel> getList() {
		return dao.read("*", "state = true", null);
	}
	
	public ArrayList<OccModel> getPage(int page, HashMap<String, String> filter) {
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
