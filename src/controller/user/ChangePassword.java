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

import service.user.UserAccService;

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
	private final UserAccService UAS = new UserAccService();
	
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
    	HashMap<String, Object> originalAcc = UAS.getAccById(id);
    	String errMsg = checkData(originalAcc, account, password, passwordCheck);
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
    	HashMap<String, Object> setData = new HashMap<String, Object>();
    	setData.put("userId", id);
    	if(originalAcc.get("account") == null)
    		setData.put("account", account);
    	setData.put("password", password);
    	UAS.updateAcc(setData);
    }
	
	private String checkData(HashMap<String, Object> originalAcc, String account, String password, String passwordCheck) {
		//check account name
    	if(originalAcc.get("account") == null && UAS.isAccExist(account)) {
    		return "The account name already exists.";
    	}
    	
    	//check whether the password is equal to the checked password
    	if(!passwordCheck.equals(password)) {
    		return "The checked password doesn't match.";
    	}
    	
    	return null;
	}
}
