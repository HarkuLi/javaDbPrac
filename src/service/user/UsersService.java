package service.user;

import java.util.ArrayList;
import java.util.HashMap;

import dao.user.UsersDao;
import model.user.UsersModel;

public class UsersService{
	public UsersService() {
		dao = new UsersDao();
	}
	
	public void createUser(String name, String age, String birth) {
		dao.create(name, age, birth);
	}
	
	public UsersModel getUser(String id) {
		ArrayList<UsersModel> userList = dao.read(
			"*",
			"id = " + id,
			"1"
		);
		return userList.get(0);
	}
	
	public ArrayList<UsersModel> getPage(int page) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		//paging
		String limit = Integer.toString(skipNum);
		limit += "," + Integer.toString(ENTRY_PER_PAGE);
		
		return dao.read(
			"*",
			null,
			limit
		);
	}
	
	public ArrayList<UsersModel> getPage(int page, HashMap<String, String> filter) {
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
	
	public void update(String id, String name, String age, String birth) {
		dao.update(id, name, age, birth);
	}
	
	public void delete(String id) {
		dao.delete(id);
	}
	
	private final int ENTRY_PER_PAGE = 10;
	
	private UsersDao dao;
	
	private String filterQueryStr(HashMap<String, String> filter) {
		String rst = "";
		String name = filter.get("name");
		String birthFrom = filter.get("birthFrom");
		String birthTo = filter.get("birthTo");
		
		if(name != null) {
			rst += "name like '%" + name + "%'";
		}
		if(birthFrom != null) {
			if(rst.length() != 0) rst += " and ";
			rst += "birth >= '" + birthFrom + "'";
		}
		if(birthTo != null) {
			if(rst.length() != 0) rst += " and ";
			rst += "birth <= '" + birthTo + "'";
		}
		
		return rst;
	}
}
