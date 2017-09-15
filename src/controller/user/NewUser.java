package controller.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import service.user.UserAccService;
import service.user.UsersService;

@MultipartConfig

public class NewUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	private static final String datePattern = "yyyy-MM-dd";
	//workload for bcrypt
	private static final int workload = 12;
	private final UsersService dbService = new UsersService();
	private final UserAccService UAS = new UserAccService();
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
		HashMap<String, String> rstMap = new HashMap<String, String>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	String fileName = null;
    	
    	//get passed parameters
    	String account = req.getParameter("account");
    	String password = req.getParameter("password");
    	String passwordCheck = req.getParameter("passwordCheck");
    	String name = req.getParameter("name");
    	String age = req.getParameter("age");
    	String birth = req.getParameter("birth");
    	Part photo = req.getPart("photo");
    	String photoType = req.getParameter("photoType");
    	String[] interest = req.getParameterValues("interest[]");
    	String occupation = req.getParameter("occupation");
    	String state = req.getParameter("state");
    	
    	//check data
    	String errMsg = checkData(age, account, password, passwordCheck);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
        	rstObj = new JSONObject(rstMap);
        	res.setContentType("application/json");
        	out.println(rstObj);
        	return;
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
    	
    	try {
			DateFormat sdf = new SimpleDateFormat(datePattern);
			//note: the type Date here is java.sql.date
			//      but sdf.parse(String) returns java.util.date
	    	Date birthDate = new Date(sdf.parse(birth).getTime());
			newData.put("birth", birthDate);
		} catch (Exception e) {
			System.out.println("Exception in NewUser: " + e.toString());
			return;
		}
    	
    	if(photo.getSize() != 0) {
    		newData.put("photoName", fileName);
    	}
    	newData.put("interest", interest);
    	newData.put("occupation", occupation);
    	newData.put("state", state.equals("1"));
    	dbService.createUser(newData);
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
	
	private String storePhoto(Part photo, String photoType) {
		String fileName = UUID.randomUUID().toString();
		fileName += "." + photoType;	//filename extension
		String path = STORE_PATH + fileName;
		
		File dir = new File(STORE_PATH);
		if(!dir.exists()) dir.mkdir();
		try {
			photo.write(path);
		} catch (Exception e) {
			System.out.println("Exception in storing photo: " + e.toString());
		}
		
		return fileName;
	}
}