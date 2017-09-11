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

import model.user.UsersModel;
import service.user.UsersService;

@MultipartConfig

/**
 * 
 * @response
 * 		{
 * 			errMsg: String		//if error
 * 		}
 */
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//workload for bcrypt
	private static final int workload = 12;
	private final UsersService dbService = new UsersService();
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		HashMap<String, String> rstMap = new HashMap<String, String>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
		
    	//get passed parameters
    	String id = req.getParameter("id");
    	String account = req.getParameter("account");
    	String password = req.getParameter("password");
    	String passwordCheck = req.getParameter("passwordCheck");
    	
    	//check data
    	UsersModel originalUser = dbService.getUser(id);
    	String errMsg = checkData(originalUser, account, password, passwordCheck);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
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
    	if(originalUser.getAccount() == null)
    		newData.put("account", account);
    	newData.put("password", password);
    	dbService.update(newData);
    }
	
	private String checkData(UsersModel originalUser, String account, String password, String passwordCheck) {
		//check account name
    	if(originalUser.getAccount() == null && dbService.isAccExist(account)) {
    		return "The account name already exists.";
    	}
    	
    	//check whether the password is equal to the checked password
    	if(!passwordCheck.equals(password)) {
    		return "The checked password doesn't match.";
    	}
    	
    	return null;
	}
}