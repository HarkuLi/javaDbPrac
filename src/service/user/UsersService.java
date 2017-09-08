package service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import dao.user.UsersDao;
import model.user.UsersModel;

public class UsersService{
	private final int ENTRY_PER_PAGE = 10;
	private UsersDao dao;
	private UserIntService UIS;
	
	public UsersService() {
		dao = new UsersDao();
		UIS = new UserIntService();
	}
	
	/**
	 * 
	 * @param newData {HashMap<String, Object>}
	 * 		{
	 *       	name: String,
	 * 		 	age: int,
	 *       	birth: String,
	 *       	photoName: String,    //not required
	 *       	interest: String[],
	 * 			occupation: String,
	 * 			state: Boolean
	 *      }
	 */
	public void createUser(HashMap<String, Object> newData) {
		String id = UUID.randomUUID().toString();
		newData.put("id", id);
		
		UIS.saveInterests(id, (String[])newData.get("interest"));
		
		dao.create(newData);
	}
	
	public UsersModel getUser(String id) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", id);
		ArrayList<UsersModel> userList = dao.read(filter, 0, 1);
		UsersModel user = userList.get(0);
		user.setInterest(UIS.getInterests(id));
		return user;
	}
	
	public boolean isAccExist(String account) {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put("account", account);
		
		ArrayList<UsersModel> userList = dao.read(filter, 0, 1);
		if(userList.size() > 0) return true;
		
		return false;
	}
	
	public ArrayList<UsersModel> getPage(int page, HashMap<String, Object> filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		ArrayList<UsersModel> userList = dao.read(filter, skipNum, ENTRY_PER_PAGE);
		
		//set interests of each user
		for(UsersModel user : userList) {
			user.setInterest(UIS.getInterests(user.getId()));
		}
		
		return userList;
	}
	
	public int getTotalPage(HashMap<String, Object> filter) {
		int rowNum = dao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	/**
	 * 
	 * @param newData {HashMap<String, Object>}
	 * 		{
	 * 			id: String,	//required
	 * 			password: String,
	 * 			name: String,
	 * 			age: int,
	 * 			birth: String,
	 * 			photoName: String,
	 * 			interest: String[],
	 * 			occupation: String,
	 * 			state: Boolean
	 * 		}
	 */
	public void update(HashMap<String, Object> newData) {
		String[] interestList = (String[])newData.get("interest");
		String userId = (String) newData.get("id");
		UIS.updateInterests(userId, interestList);
		
		newData.remove("interest");
		dao.update(newData);
	}
	
	public void delete(String id) {
		UIS.delInterests(id);
		dao.delete(id);
	}
}
