package service.user;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.Part;

import dao.user.UsersDao;
import model.user.UsersModel;

public class UsersService{
	private final int ENTRY_PER_PAGE = 10;
	private final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	private UsersDao dao;
	
	public UsersService() {
		dao = new UsersDao();
	}
	
	/**
	 * 
	 * @param newData {HashMap<String, Object>}
	 * 		{
	 *       name: String,
	 * 		 age: int,
	 *       birth: String,
	 *       photo: Part,    //not required
	 *       photoType: String    //not required
	 *      }
	 */
	public void createUser(HashMap<String, Object> newData) {
		String id = UUID.randomUUID().toString();
		newData.put("id", id);
		
		//store photo
		Part photo = (Part) newData.get("photo");
		if(photo != null) {
			String fileName = UUID.randomUUID().toString();
			String photoType = (String) newData.get("photoType");
			fileName += "." + photoType;	//filename extension
			String path = STORE_PATH + fileName;
			
			File dir = new File(STORE_PATH);
			if(!dir.exists()) dir.mkdir();
			try {
				photo.write(path);
				newData.put("photo", fileName);
			} catch (Exception e) {
				System.out.println("Exception in storing photo: " + e.toString());
			}
		}
		
		dao.create(newData);
	}
	
	public UsersModel getUser(String id) {
		ArrayList<UsersModel> userList = dao.read(
			"*",
			"id = '" + id + "'",
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
	
	/**
	 * 
	 * @param newData {HashMap<String, Object>} {id: String, name: String, age: int, birth: String}
	 */
	public void update(HashMap<String, Object> newData) {
		Part photo = (Part) newData.get("photo");
		if(photo != null) {
			//delete original photo
			String fileName = (String)newData.get("photoName");
			if(fileName != null) {
				String path = STORE_PATH + "/" + fileName;
				File file = new File(path);
				if(file.exists()) file.delete();
			}
			
			//store new photo
			fileName = UUID.randomUUID().toString();
			String photoType = (String) newData.get("photoType");
			fileName += "." + photoType;	//filename extension
			
			String path = STORE_PATH + fileName;
			
			File dir = new File(STORE_PATH);
			if(!dir.exists()) dir.mkdir();
			try {
				photo.write(path);
				newData.put("photo", fileName);
			} catch (Exception e) {
				System.out.println("Exception in storing photo: " + e.toString());
			}
		}
		
		dao.update(newData);
	}
	
	public void delete(String id) {
		dao.delete(id);
	}
	
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
