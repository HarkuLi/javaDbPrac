package com.harku.controller.sign;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harku.config.ConstantConfig;
import com.harku.model.User;
import com.harku.service.OccupationService;
import com.harku.service.PhotoService;
import com.harku.service.UserAccountService;
import com.harku.service.UserService;

@RestController
public class SignRestController {
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private UserService usersService;
	@Autowired
	private OccupationService occupationService;
	@Resource(name = "statusOption")
	private Map<String, String> statusOption;
	
	/**
	 * response:
	 * 201: (success)
	 * {
	 *   account: String
	 * }
	 * 400: (wrong input)
	 * {
	 *   account: String,
	 *   errMsg: String
	 * }
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value = {"/sign_up/action", "/user/new"}, method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> newUser(
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
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		StringBuffer errMsg = new StringBuffer();
		User newData = new User();
		String photoName = null;
		
		rstMap.put("account", account);
		
		//check data
		newData.setName(name);
    	newData.setAccount(account);
    	newData.setPassword(password);
    	try {
    		newData.setAge(Integer.parseInt(age));
    	} catch(Exception e) {
    		errMsg.append("Wrong input for age.\n");
    	}
    	newData.setBirth(birth);
    	newData.setInterest(interest);
    	newData.setOccupation(occupation);
    	newData.setState(state);
    	errMsg.append(checkUserData(newData, passwordCheck));
    	if(errMsg.length() != 0) {
    		rstMap.put("errMsg", errMsg.toString());
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
		
    	//hash the password
    	password = BCrypt.hashpw(password, BCrypt.gensalt(ConstantConfig.BCRYPT_WORKLOAD));
    	
    	//store photo
		if(photo != null && photo.getSize() != 0) {
			photoName = PhotoService.write(photo, photoType);
			
			if(photoName == null) {
				rstMap.put("errMsg", "Wrong input for photo.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
			}
		}
    	
    	//call service function
			//set hashed password
		newData.setPassword(password);
    	newData.setPhotoName(photoName);
    	usersService.createUser(newData);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(rstMap);
	}
	
	private String checkUserData(User user,  String passwordCheck) {
		StringBuffer errMsg = new StringBuffer();
		
		//name
		if(user.getName().length() > ConstantConfig.MAX_NAME_LENGTH)
			errMsg.append("The name can't be longer than " + ConstantConfig.MAX_NAME_LENGTH + " characters.\n");
		
		//account
		if(user.getAccount().length() > ConstantConfig.MAX_ACCOUNT_LENGTH)
			errMsg.append("The account can't be longer than " + ConstantConfig.MAX_ACCOUNT_LENGTH + " characters.\n");
		else if(userAccountService.isAccExist(user.getAccount()))
			errMsg.append("The account name already exists.\n");
		
		//password
		if(user.getPassword().length() > ConstantConfig.MAX_PASSWORD_LENGTH)
			errMsg.append("The password can't be longer than " + ConstantConfig.MAX_PASSWORD_LENGTH + " characters.\n");
		else if(!passwordCheck.equals(user.getPassword()))
			errMsg.append("The checked password doesn't match.\n");
		
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
		
		//state
		if(!statusOption.containsKey(user.getState())) {
			errMsg.append("Invalid state.\n");
		}
		
		return errMsg.toString();
	}
}
