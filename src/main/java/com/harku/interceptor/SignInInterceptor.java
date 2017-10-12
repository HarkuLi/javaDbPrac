package com.harku.interceptor;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.harku.service.user.UserAccService;

@Component
public class SignInInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private UserAccService UAS;
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		
		final String signInRoute = "/javaDbPrac/sign_in/page";
		
		//check token
		HashMap<String, String> cookie = cookieHandle(req.getCookies());
		if(!(cookie != null && UAS.checkToken(cookie.get("LOGIN_INFO")))) {
			res.sendRedirect(signInRoute);
			return false;
		}
		
		return true;
	}
	
	private HashMap<String, String> cookieHandle(Cookie[] cookies) {
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
