package com.harku.controller.sign;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harku.config.BeanConfig;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;

@RestController
public class SignRestServlet {
	private static final int workload = 12;
	private static final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	private final UserAccService UAS = ctx.getBean(UserAccService.class);
	private final UsersService dbService = ctx.getBean(UsersService.class);
	
	/**
	 * response format:
	 * {
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
		String fileName = null;
		
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
			fileName = storePhoto(photo, photoType);
		}
    	
    	//call service function
    	HashMap<String, Object> newData = new HashMap<String, Object>();
    	newData.put("name", name);
    	newData.put("account", account);
    	newData.put("password", password);
    	newData.put("age", Integer.parseInt(age));
    	newData.put("birth", birth);
    	if(photo.getSize() != 0) {
    		newData.put("photoName", fileName);
    	}
    	newData.put("interest", interest);
    	newData.put("occupation", occupation);
    	newData.put("state", state.equals("1"));
    	dbService.createUser(newData);
    	
    	return null;
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
	
	private String storePhoto(MultipartFile photo, String photoType) throws IllegalStateException, IOException {
		String fileName = UUID.randomUUID().toString();
		fileName += "." + photoType;	//filename extension
		String path = STORE_PATH + fileName;
		
		File dir = new File(STORE_PATH);
		if(!dir.exists()) dir.mkdir();
		
		dir = new File(path);
		photo.transferTo(dir);
		
		return fileName;
	}
}
