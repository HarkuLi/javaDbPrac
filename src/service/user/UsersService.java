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
	private UserIntService UIS;
	
	public UsersService() {
		dao = new UsersDao();
		UIS = new UserIntService();
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
		
		UIS.saveInterests(id, (String[])newData.get("interests"));
		
		dao.create(newData);
	}
	
	public UsersModel getUser(String id) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", id);
		ArrayList<UsersModel> userList = dao.read(filter, 0, 1);
		UsersModel user = userList.get(0);
		user.setInterest(UIS.getInterests(id));
		return user;
	}
	
	public ArrayList<UsersModel> getPage(int page, HashMap<String, String> filter) {
		int skipNum = ENTRY_PER_PAGE * (page - 1);
		
		return dao.read(filter, skipNum, ENTRY_PER_PAGE);
	}
	
	public int getTotalPage(HashMap<String, String> filter) {
		int rowNum = dao.getRowNum(filter);
		
		final int totalPage = (int) Math.ceil((double) rowNum / ENTRY_PER_PAGE);
		return totalPage;
	}
	
	/**
	 * 
	 * @param newData {HashMap<String, Object>} {id: String, name: String, age: int, birth: String}
	 */
	public void update(HashMap<String, Object> newData) {
		//update photo
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
		
		String[] interestList = (String[])newData.get("interests");
		String userId = (String) newData.get("id");
		UIS.updateInterests(userId, interestList);
		
		dao.update(newData);
	}
	
	public void delete(String id) {
		//delete the photo
		String photoName = getUser(id).getPhotoName();
		if(photoName != null) {
			String path = STORE_PATH + "/" + photoName;
			File file = new File(path);
			if(file.exists()) file.delete();
		}
		
		UIS.delInterests(id);
		
		dao.delete(id);
	}
}
