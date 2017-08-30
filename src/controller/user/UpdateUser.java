package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

import service.user.UsersService;

@MultipartConfig

public class UpdateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		UsersService dbService = new UsersService();
    	HashMap<String, String> rstMap = new HashMap<String, String>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	String name = req.getParameter("name");
    	String age = req.getParameter("age");
    	String birth = req.getParameter("birth");
    	Part photo = req.getPart("photo");
    	String photoType = req.getParameter("photoType");
    	String photoName = req.getParameter("photoName");
    	
    	//check data
    	String pattern = "^\\d+$";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(age);
    	if(!m.find()) {
	    	rstMap.put("errMsg", "Wrong input for age.");
	    	rstObj = new JSONObject(rstMap);
	    	res.setContentType("application/json");
	    	out.println(rstObj);
	    	return;
    	}
    	
    	//call service function
    	HashMap<String, Object> newData = new HashMap<String, Object>();
    	newData.put("id", id);
    	newData.put("name", name);
    	newData.put("age", Integer.parseInt(age));
    	newData.put("birth", birth);
    	if(photo.getSize() != 0) {
    		newData.put("photo", photo);
    		newData.put("photoType", photoType);
    		newData.put("photoName", photoName);
    	}
    	dbService.update(newData);
    }
}