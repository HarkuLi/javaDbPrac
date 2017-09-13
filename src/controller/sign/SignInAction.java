package controller.sign;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import service.user.UserAccService;

@MultipartConfig

public class SignInAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * response: {result: boolean}
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
		
		UserAccService UAS = new UserAccService();
		JSONObject resObj = new JSONObject();
    	PrintWriter out = res.getWriter();
    	String token = null;
    	boolean rst = false;
    	
    	//get passed parameters
    	String account = req.getParameter("account");
    	String password = req.getParameter("password");
    	
    	//check whether the account exists
    	HashMap<String, Object> acc = UAS.getAcc(account);
    	if(acc != null && (boolean)acc.get("state")) {
    		//check password
    		if(BCrypt.checkpw(password, (String)acc.get("password"))){
    			//set token
    			long signInTime = System.currentTimeMillis();
    			token = genToken();
    			//store the sign in info.
    			HashMap<String, Object> setData = new HashMap<String, Object>();
    			setData.put("userId", (String)acc.get("userId"));
    			setData.put("signInTime", signInTime);
    			setData.put("token", token);
    			UAS.updateAcc(setData);
    			
    			rst = true;
    		}
    	}
    	
    	//set cookie if passing checks
    	if(token != null) {
			Cookie cookie = new Cookie("LOGIN_INFO", token);
			cookie.setMaxAge(UserAccService.EXPIRE_TIME_SEC);
			cookie.setPath("/javaDbPrac");
			res.addCookie(cookie);
    	}
    	
    	resObj.put("result", rst);
    	res.setContentType("application/json");
    	out.println(resObj);
    }
	
	private String genToken() {
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
		String rst = "";
		int contentLen = 100;
		for(int i=0; i<contentLen; ++i) {
			int idx = (int)(Math.random()*64);
			rst += charSet.charAt(idx);
		}
		return rst;
	}
}



