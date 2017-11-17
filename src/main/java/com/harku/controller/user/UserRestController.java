package com.harku.controller.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

import com.harku.config.ConstantConfig;
import com.harku.model.UserFilter;
import com.harku.model.User;
import com.harku.service.OccupationService;
import com.harku.service.PhotoService;
import com.harku.service.UserAccountService;
import com.harku.service.UserService;

@RestController
@RequestMapping("/user")
public class UserRestController {
	@Autowired
	private UserService usersService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private OccupationService occupationService;
	
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
    	StringBuffer errMsg = new StringBuffer();
    	User newData = new User();
    	String newPhotoName = null;
    	
    	rstMap.put("id", id);
    	
    	//check data
    	newData.setId(id);
    	newData.setName(name);
    	try {
    		newData.setAge(Integer.parseInt(age));
    	} catch(Exception e) {
    		errMsg.append("Wrong input for age.\n");
    	}
		newData.setBirth(birth);
		newData.setInterest(interest);
    	newData.setOccupation(occupation);
    	newData.setState(state.equals("1"));
    	errMsg.append(checkUserData(newData));
    	if(errMsg.length() != 0) {
    		rstMap.put("errMsg", errMsg.toString());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	//update photo
		if(photo != null && photo.getSize() != 0) {
			//store new photo
			newPhotoName = PhotoService.write(photo, photoType);
			
			if(newPhotoName == null) {
				rstMap.put("errMsg", "Wrong input for photo.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
			}
				
			//delete original photo
			PhotoService.delete(photoName);
		}
		
		//call service function
		newData.setPhotoName(newPhotoName);
    	usersService.update(newData);
    	
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
		List<User> tableList;
		UserFilter filter = new UserFilter();
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
    	totalPage = usersService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		//set result
		tableList = usersService.getPage(page, filter);
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
	public ResponseEntity<User> GetUserByToken(@CookieValue(value = ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, required = false) String LOGIN_INFO) {
		
		if(LOGIN_INFO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
		//get id by token in the cookie
		User acc = userAccountService.getAccByToken(LOGIN_INFO);
		if(acc == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		String id = acc.getId();
		
		//get user by id
		User user = usersService.getUser(id);
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
	public ResponseEntity<User> GetUser(@RequestParam String id) {
		
		User user = usersService.getUser(id);
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
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> DeleteUser(@RequestParam String id) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("id", id);
		
		//get the user by id
		User user = usersService.getUser(id);
		if(user == null) {
			rstMap.put("errMsg", "No user matches the id.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
		}
		
		//delete the photo
		String photoName = user.getPhotoName();
		PhotoService.delete(photoName);
		
		usersService.delete(id);
		
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
		@RequestParam String account,
		@RequestParam String password,
		@RequestParam String passwordCheck) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("id", id);
		
		//check data
    	User originalAcc = userAccountService.getAccById(id);
    	String errMsg = checkAccountData(originalAcc, account, password, passwordCheck);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	//hash the password
    	password = BCrypt.hashpw(password, BCrypt.gensalt(ConstantConfig.BCRYPT_WORKLOAD));
    	
    	//call service to update the password
    	if(originalAcc.getAccount() == null)
    		originalAcc.setAccount(account);
    	originalAcc.setPassword(password);
    	userAccountService.updateAcc(originalAcc);
    	
		return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	private String checkAccountData(User originalAccount, String account, String password, String passwordCheck) {
		if(originalAccount == null) {
			return "No account matches the id.";
		}
		
		//check account name
		if(account.length() > ConstantConfig.MAX_ACCOUNT_LENGTH)
			return "The account can't be longer than " + ConstantConfig.MAX_ACCOUNT_LENGTH + " characters.\n";
		else if(originalAccount.getAccount() == null && userAccountService.isAccExist(account))
   			return "The account name already exists.";
    	
    	//check whether the password is equal to the checked password
    	if(password.length() > ConstantConfig.MAX_PASSWORD_LENGTH)
    		return "The password can't be longer than " + ConstantConfig.MAX_PASSWORD_LENGTH + " characters.\n";
    	else if(!passwordCheck.equals(password)) {
    		return "The checked password doesn't match.";
    	}
    	
    	return null;
	}
	
	private String checkUserData(User user) {
		StringBuffer errMsg = new StringBuffer();
		
		//id
		String id = user.getId();
		if(id != null) {
			User getUser = usersService.getUser(id);
	    	if(getUser == null) errMsg.append("No such user id.\n");
		}
		
		//name
		if(user.getName().length() > ConstantConfig.MAX_NAME_LENGTH)
			errMsg.append("The name can't be longer than " + ConstantConfig.MAX_NAME_LENGTH + " characters.\n");
		
		//age
		Integer age = user.getAge();
		if(age != null && age < 0) errMsg.append("The age shouldn't be lower than 0.\n");
		
		//birth
    	String patternStr = "^\\d{1,4}-\\d{2}-\\d{2}$";
    	Pattern pattern = Pattern.compile(patternStr);
    	Matcher matcher = pattern.matcher(user.getBirth());
    	if(!matcher.find()) errMsg.append("Wrong format for birth.\n");
    	
		//occupation
    	String occ = user.getOccupation();
		if(!occ.equals("other") && occupationService.getOcc(occ) == null)
			errMsg.append("No such occupation.\n");
		
		return errMsg.toString();
	}
}
