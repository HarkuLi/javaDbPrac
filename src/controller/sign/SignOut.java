package controller.sign;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		Cookie cookie = new Cookie("LOGIN_INFO", "");
		cookie.setMaxAge(0);
		cookie.setPath("/javaDbPrac");
		res.addCookie(cookie);
		res.sendRedirect("/javaDbPrac/sign_in");
	}
}