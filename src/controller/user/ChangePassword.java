package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import service.user.UsersService;

@MultipartConfig

public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//workload for bcrypt
	private static final int workload = 12;
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		HashMap<String, String> rstMap = new HashMap<String, String>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
		UsersService dbService = new UsersService();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	String password = req.getParameter("password");
    	String passwordCheck = req.getParameter("passwordCheck");
    	
    	//check password
    	if(!password.equals(passwordCheck)) {
    		rstMap.put("errMsg", "The checked password doesn't match.");
        	rstObj = new JSONObject(rstMap);
        	res.setContentType("application/json");
        	out.println(rstObj);
        	return;
    	}
    	
    	//hash the password
    	password = BCrypt.hashpw(password, BCrypt.gensalt(workload));
    	
    	//call service to update the password
    	HashMap<String, Object> newData = new HashMap<String, Object>();
    	newData.put("id", id);
    	newData.put("password", password);
    	dbService.update(newData);
    }
}
