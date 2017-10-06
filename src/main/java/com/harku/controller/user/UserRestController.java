package com.harku.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harku.config.BeanConfig;
import com.harku.model.user.UsersModel;
import com.harku.service.photo.PhotoService;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;

@RestController
@RequestMapping("/user")
public class UserRestController {
	//workload for bcrypt
	private static final int workload = 12;
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	
	private final UsersService dbService = ctx.getBean(UsersService.class);
	private final UserAccService UAS = ctx.getBean(UserAccService.class);
	
	@RequestMapping(value = "/photo", method = RequestMethod.GET, produces = {"image/jpeg", "image/gif", "image/png"})
	public ResponseEntity<byte[]> UserPhoto(
		@RequestParam(value = "n", required = false, defaultValue = "default.png") String fileName) throws IOException {
		
		HttpHeaders headers = new HttpHeaders();
		
		//read photo
		byte[] photo = PhotoService.read(fileName);
		if(photo == null) {
			fileName = PhotoService.DEFAULT_PHOTO_NAME;
			photo = PhotoService.read(fileName);
		}
		
		//set the content type of header
		headers.setContentType(PhotoService.parseType(fileName));
		
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(photo, headers, HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * response format:
	 * {
	 *   id: String
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, String> UpdateUser(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String age,
		@RequestParam String birth,
		@RequestParam(required = false) MultipartFile photo,
		@RequestParam(required = false) String photoType,
		@RequestParam(required = false) String photoName,
		@RequestParam(value = "interest[]", required = false) String[] interest,
		@RequestParam String occupation,
		@RequestParam String state) {
		
    	HashMap<String, String> rstMap = new HashMap<String, String>();
    	
    	rstMap.put("id", id);
    	
    	//check data
    	String pattern = "^\\d+$";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(age);
    	if(!m.find()) {
	    	rstMap.put("errMsg", "Wrong input for age.");
	    	return rstMap;
    	}
    	
    	//update photo
		if(photo.getSize() != 0) {
			//delete original photo
			PhotoService.delete(photoName);
			
			//store new photo
			photoName = PhotoService.write(photo, photoType);
		}
		
		//call service function
    	HashMap<String, Object> newData = new HashMap<String, Object>();
    	newData.put("id", id);
    	newData.put("name", name);
    	newData.put("age", Integer.parseInt(age));
		newData.put("birth", birth);
    	if(photoName != null) {
    		newData.put("photoName", photoName);
    	}
    	newData.put("interest", interest);
    	newData.put("occupation", occupation);
    	newData.put("state", state.equals("1"));
    	dbService.update(newData);
    	
    	return rstMap;
	}
	
	//@RequestMapping(value = {"/sign_up/action", "/user/new"}, method = RequestMethod.POST, produces = "application/json")
	//in the SignRestServlet.java
	
	/**
	 * response format:
	 * {
	 *   list: Array<Object>,
	 *   totalPage: Number
	 * }
	 */
	@RequestMapping(value = "/get_page", method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, Object> GetPage(
		@RequestParam int page,
		@RequestParam(required=false) String name,
		@RequestParam(required=false) String birthFrom,
		@RequestParam(required=false) String birthTo,
		@RequestParam(required=false) String occ,
		@RequestParam(required=false) String state,
		@RequestParam(value = "interest[]", required=false) String[] interest) {
		
		int totalPage;
		ArrayList<UsersModel> tableList;
		HashMap<String, Object> filter = new HashMap<String, Object>();
		HashMap<String, Object> rstMap = new HashMap<String, Object>();
		
		//set filter
    	filter.put("name", name);
    	filter.put("birthFrom", birthFrom);
    	filter.put("birthTo", birthTo);
    	filter.put("occ", occ);
    	if(state != null)
    		filter.put("state", state.equals("1"));
    	filter.put("interest", interest);
    	
    	//check page range
    	totalPage = dbService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		//set result
		if(page > 0) {
			tableList = dbService.getPage(page, filter);
			rstMap.put("list", tableList);
		}
		rstMap.put("totalPage", totalPage);
		
		return rstMap;
	}
	
	@RequestMapping(value = "/get_by_token", method = RequestMethod.POST, produces = "application/json")
	public UsersModel GetUserByToken(@CookieValue("LOGIN_INFO") String LOGIN_INFO) {
		
		if(LOGIN_INFO == null) return null;
		
		//get id by token in the cookie
		HashMap<String, Object> acc = UAS.getAccByToken(LOGIN_INFO);
		String id = (String) acc.get("userId");
		
		//get user by id
		UsersModel user = dbService.getUser(id);
		user.eraseSecretInfo();
		
		//response
		return user;
	}
	
	/**
	 * response: user data
	 */
	@RequestMapping(value = "/get_one", method = RequestMethod.POST, produces = "application/json")
	public UsersModel GetUser(@RequestParam String id) {
		
		UsersModel user = dbService.getUser(id);
    	user.eraseSecretInfo();
    	
    	return user;
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, String> DeleteUser(@RequestParam String id) {
		
		HashMap<String, String> rstMap = new HashMap<String, String>();
		
		//delete the photo
		String photoName = dbService.getUser(id).getPhotoName();
		PhotoService.delete(photoName);
		
		dbService.delete(id);
		
		rstMap.put("id", id);
		return rstMap;
	}
	
	@RequestMapping(value = "/change_password", method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, String> ChangePassword(
		@RequestParam String id,
		@RequestParam(required = false) String account,
		@RequestParam String password,
		@RequestParam String passwordCheck) {
		
		HashMap<String, String> rstMap = new HashMap<String, String>();
		
		//check data
    	HashMap<String, Object> originalAcc = UAS.getAccById(id);
    	String errMsg = checkAccountData(originalAcc, account, password, passwordCheck);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
        	return rstMap;
    	}
    	
    	//hash the password
    	password = BCrypt.hashpw(password, BCrypt.gensalt(workload));
    	
    	//call service to update the password
    	HashMap<String, Object> setData = new HashMap<String, Object>();
    	setData.put("userId", id);
    	if(originalAcc.get("account") == null)
    		setData.put("account", account);
    	setData.put("password", password);
    	UAS.updateAcc(setData);
    	
		return null;
	}
	
	private String checkAccountData(HashMap<String, Object> originalAcc, String account, String password, String passwordCheck) {
		//check account name
    	if(originalAcc.get("account") == null && UAS.isAccExist(account)) {
    		return "The account name already exists.";
    	}
    	
    	//check whether the password is equal to the checked password
    	if(!passwordCheck.equals(password)) {
    		return "The checked password doesn't match.";
    	}
    	
    	return null;
	}
}
