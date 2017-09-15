package controller.sign;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.user.UserAccService;

public class ShowSignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		UserAccService UAS = new UserAccService();
		
		//check token, and redirect to user page if signed in
		HashMap<String, String> cookie = cookieHandle(req.getCookies());
		if(cookie != null && UAS.checkToken(cookie.get("LOGIN_INFO"))) {
			res.sendRedirect("/javaDbPrac/user");
			return;
		}
		
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/sign_up.jsp");
		rd.forward(req, res);
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
