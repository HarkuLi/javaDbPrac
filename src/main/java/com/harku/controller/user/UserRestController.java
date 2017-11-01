package com.harku.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harku.model.user.UserFilterModel;
import com.harku.model.user.UsersModel;
import com.harku.service.photo.PhotoService;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;

@RestController
@RequestMapping("/user")
public class UserRestController {
	//workload for bcrypt
	private static final int workload = 12;
	
	@Autowired
	private UsersService dbService;
	@Autowired
	private UserAccService UAS;
	
	/**
	 * 
	 * response:
	 * 200:
	 *   an image
	 * 404: (the image name is not found)
	 */
	@RequestMapping(value = "/photo", method = RequestMethod.GET, produces = {"image/jpeg", "image/gif", "image/png"})
	public ResponseEntity<byte[]> UserPhoto(
		@RequestParam(value = "n", required = false, defaultValue = PhotoService.DEFAULT_PHOTO_NAME) String fileName) throws IOException {
		
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> responseEntity;
		
		//read photo
		byte[] photo = PhotoService.read(fileName);
		if(photo == null) {
			responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return responseEntity;
		}
		
		//set the content type of header
		headers.setContentType(PhotoService.parseType(fileName));
		
		responseEntity = new ResponseEntity<>(photo, headers, HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * response:
	 * 200: (success)
	 * {
	 *   id: String
	 * }
	 * 400: (wrong input)
	 * {
	 *   id: String,
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> UpdateUser(
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
		
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	
    	rstMap.put("id", id);
    	
    	//check data
    		//id
    	UsersModel user = dbService.getUser(id);
    	if(user == null) {
    		rstMap.put("errMsg", "No such user id.");
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    		//age
    	String pattern = "^\\d+$";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(age);
    	if(!m.find()) {
	    	rstMap.put("errMsg", "Wrong input for age.");
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	//update photo
		if(photo.getSize() != 0) {
			//delete original photo
			PhotoService.delete(photoName);
			
			//store new photo
			photoName = PhotoService.write(photo, photoType);
		}
		
		//call service function
    	UsersModel newData = new UsersModel();
    	newData.setId(id);
    	newData.setName(name);
    	newData.setAge(Integer.parseInt(age));
		newData.setBirth(birth);
    	if(photoName != null) {
    		newData.setPhotoName(photoName);
    	}
    	newData.setInterest(interest);
    	newData.setOccupation(occupation);
    	newData.setState(state.equals("1"));
    	dbService.update(newData);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	//@RequestMapping(value = {"/sign_up/action", "/user/new"}, method = RequestMethod.POST, produces = "application/json")
	//in the SignRestServlet.java
	
	/**
	 * response:
	 * 200:
	 * {
	 *   list: Array<Object>,
	 *   totalPage: Number
	 * }
	 */
	@RequestMapping(value = "/get_page", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> GetPage(
		@RequestParam int page,
		@RequestParam(required=false) String name,
		@RequestParam(required=false) String birthFrom,
		@RequestParam(required=false) String birthTo,
		@RequestParam(required=false) String occ,
		@RequestParam(required=false) String state,
		@RequestParam(value = "interest[]", required=false) String[] interest) {
		
		int totalPage;
		ArrayList<UsersModel> tableList;
		UserFilterModel filter = new UserFilterModel();
		Map<String, Object> rstMap = new HashMap<String, Object>();
		
		//set filter
    	filter.setName(name);
    	filter.setBirthFrom(birthFrom);
    	filter.setBirthTo(birthTo);
    	filter.setOccupation(occ);
    	if(state != null)
    		filter.setState(state.equals("1"));
    	filter.setInterest(interest);
    	
    	//check page range
    	totalPage = dbService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		//set result
		tableList = dbService.getPage(page, filter);
		rstMap.put("list", tableList); 
		rstMap.put("totalPage", totalPage);
		
		return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	/**
	 * response:
	 * 200:
	 *   user object
	 * 404: (no user matches the token)
	 */
	@RequestMapping(value = "/get_by_token", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<UsersModel> GetUserByToken(@CookieValue(value = "LOGIN_INFO", required = false) String LOGIN_INFO) {
		
		if(LOGIN_INFO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		//get id by token in the cookie
		UsersModel acc = UAS.getAccByToken(LOGIN_INFO);
		if(acc == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		String id = acc.getId();
		
		//get user by id
		UsersModel user = dbService.getUser(id);
		user.eraseSecretInfo();
		
		//response
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	/**
	 * response:
	 * 200:
	 *   user object
	 * 404: (no user matches the id)
	 */
	@RequestMapping(value = "/get_one", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<UsersModel> GetUser(@RequestParam String id) {
		
		UsersModel user = dbService.getUser(id);
		if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	user.eraseSecretInfo();
    	
    	return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	/**
	 * response:
	 * 200:
	 *   {
	 *   	id: String
	 *   }
	 * 400: (no user matches the id)
	 *   {
	 *   	id: String,
	 *   	errMsg: String
	 *   }
	 */
	@RequestMapping(value = "/del", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Map<String, Object>> DeleteUser(@RequestParam String id) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("id", id);
		
		//get the user by id
		UsersModel user = dbService.getUser(id);
		if(user == null) {
			rstMap.put("errMsg", "No user matches the id.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
		}
		
		//delete the photo
		String photoName = user.getPhotoName();
		PhotoService.delete(photoName);
		
		dbService.delete(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	/**
	 * 
	 * response:
	 * 200:
	 *   {
	 *   	id: String
	 *   }
	 * 400:
	 *   {
	 *   	id: String,
	 *   	errMsg: String
	 *   }
	 */
	@RequestMapping(value = "/change_password", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> ChangePassword(
		@RequestParam String id,
		@RequestParam(required = false) String account,
		@RequestParam String password,
		@RequestParam String passwordCheck) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("id", id);
		
		//check data
    	UsersModel originalAcc = UAS.getAccById(id);
    	String errMsg = checkAccountData(originalAcc, account, password, passwordCheck);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	//hash the password
    	password = BCrypt.hashpw(password, BCrypt.gensalt(workload));
    	
    	//call service to update the password
    	if(originalAcc.getAccount() == null)
    		originalAcc.setAccount(account);
    	originalAcc.setPassword(password);
    	UAS.updateAcc(originalAcc);
    	
		return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	private String checkAccountData(UsersModel originalAcc, String account, String password, String passwordCheck) {
		if(originalAcc == null) {
			return "No account matches the id.";
		}
		
		//check account name
    	if(originalAcc.getAccount() == null && UAS.isAccExist(account)) {
    		return "The account name already exists.";
    	}
    	
    	//check whether the password is equal to the checked password
    	if(!passwordCheck.equals(password)) {
    		return "The checked password doesn't match.";
    	}
    	
    	return null;
	}
}
