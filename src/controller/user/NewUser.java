package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import service.user.UsersService;

public class NewUser extends HttpServlet {
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
    	String name = req.getParameter("name");
    	String age = req.getParameter("age");
    	String birth = req.getParameter("birth");
    	
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
    	
    	dbService.createUser(name, age, birth);
    }
}