package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import model.user.UsersModel;
import service.user.UserAccService;
import service.user.UsersService;

/**
 * 
 *	response user info of the token
 */
public class GetUserByToken extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		UsersService US = new UsersService();
		UserAccService UAS = new UserAccService();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
		
		HashMap<String, String> cookie = cookieHandle(req.getCookies());
		if(cookie == null) {
			rstObj = new JSONObject();
			out.println(rstObj);
			return;
		}
		
		//get id by token in the cookie
		HashMap<String, Object> acc = UAS.getAccByToken(cookie.get("LOGIN_INFO"));
		String id = (String) acc.get("userId");
		
		//get user by id
		UsersModel user = US.getUser(id);
		user.eraseSecretInfo();
		
		//response
		rstObj = new JSONObject(user);
		res.setContentType("application/json");
		out.println(rstObj);
	}
	
	private HashMap<String, String> cookieHandle(Cookie[] cookies){
		if(cookies == null) return null;
		
		HashMap<String, String> rst = new HashMap<String, String>();
		
		for(Cookie cookie : cookies) {
			String name = cookie.getName();
			String value = cookie.getValue();
			rst.put(name, value);
		}
		
		return rst;
	}
}
