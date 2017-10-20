package com.harku.controller.sign;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harku.model.user.UsersModel;
import com.harku.service.photo.PhotoService;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;

@RestController
public class SignRestController {
	private static final int workload = 12;
	@Autowired
	private UserAccService UAS;
	@Autowired
	private UsersService dbService;
	
	/**
	 * response format:
	 * {
	 *   account: String
	 *   errMsg: String
	 * }
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value = {"/sign_up/action", "/user/new"}, method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, String> NewUser(
		@RequestParam String account, 
		@RequestParam String password,
		@RequestParam String passwordCheck,
		@RequestParam String name,
		@RequestParam String age,
		@RequestParam String birth,
		@RequestParam(required = false) MultipartFile photo,
		@RequestParam(required = false) String photoType,
		@RequestParam(value = "interest[]", required = false) String[] interest,
		@RequestParam String occupation,
		@RequestParam(required = false, defaultValue = "1") String state) throws IllegalStateException, IOException {
		
		HashMap<String, String> rstMap = new HashMap<String, String>();
		String photoName = null;
		
		rstMap.put("account", account);
		
		//check data
    	String errMsg = checkData(age, account, password, passwordCheck);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
        	return rstMap;
    	}
		
    	//hash the password
    	password = BCrypt.hashpw(password, BCrypt.gensalt(workload));
    	
    	//store photo
		if(photo.getSize() != 0) {
			photoName = PhotoService.write(photo, photoType);
		}
    	
    	//call service function
    	UsersModel newData = new UsersModel();
    	newData.setName(name);
    	newData.setAccount(account);
    	newData.setPassword(password);;
    	newData.setAge(Integer.parseInt(age));
    	newData.setBirth(birth);
    	if(photoName != null) {
    		newData.setPhotoName(photoName);
    	}
    	newData.setInterest(interest);
    	newData.setOccupation(occupation);
    	newData.setState(state.equals("1"));
    	dbService.createUser(newData);
    	
    	return rstMap;
	}
	
	private String checkData(String age, String account, String password, String passwordCheck) {
    	//check age
		String pattern = "^\\d+$";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(age);
    	if(!m.find()) {
	    	return "Wrong input for age.";
    	}
    	
    	//check account name
    	if(UAS.isAccExist(account)) {
    		return "The account name already exists.";
    	}
    	
    	//check whether the password is equal to the checked password
    	if(!passwordCheck.equals(password)) {
    		return "The checked password doesn't match.";
    	}
    	
    	return null;
	}
}
