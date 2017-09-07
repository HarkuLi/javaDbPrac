package controller.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import service.user.UsersService;

@MultipartConfig

public class NewUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	
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
		UsersService dbService = new UsersService();
    	PrintWriter out = res.getWriter();
    	String fileName = null;
    	
    	//get passed parameters
//    	String account = req.getParameter("account");
//    	String password = req.getParameter("password");
//    	String passwordCheck = req.getParameter("passwordCheck");
    	String name = req.getParameter("name");
    	String age = req.getParameter("age");
    	String birth = req.getParameter("birth");
    	Part photo = req.getPart("photo");
    	String photoType = req.getParameter("photoType");
    	String[] interestList = req.getParameterValues("interest[]");
    	String occupation = req.getParameter("occupation");
    	String state = req.getParameter("state");
    	
    	//test
//    	password = BCrypt.hashpw(password, BCrypt.gensalt());
    	
    	//check data
    	String errMsg = checkData(age);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
        	rstObj = new JSONObject(rstMap);
        	res.setContentType("application/json");
        	out.println(rstObj);
        	return;
    	}
    	
    	//store photo
		if(photo.getSize() != 0) {
			fileName = storePhoto(photo, photoType);
		}
    	
    	//call service function
    	HashMap<String, Object> newData = new HashMap<String, Object>();
    	newData.put("name", name);
    	newData.put("age", Integer.parseInt(age));
    	newData.put("birth", birth);
    	if(photo.getSize() != 0) {
    		newData.put("photoName", fileName);
    	}
    	newData.put("interests", interestList);
    	newData.put("occupation", occupation);
    	newData.put("state", state.equals("1"));
    	dbService.createUser(newData);
    }
	
	private String checkData(String age) {
    	String pattern = "^\\d+$";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(age);
    	if(!m.find()) {
	    	return "Wrong input for age.";
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