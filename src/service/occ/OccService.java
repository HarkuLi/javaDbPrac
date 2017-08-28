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
