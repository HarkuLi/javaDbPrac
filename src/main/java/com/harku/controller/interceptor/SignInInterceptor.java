package com.harku.controller.interceptor;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.harku.config.BeanConfig;
import com.harku.service.user.UserAccService;

public class SignInInterceptor extends HandlerInterceptorAdapter {
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		
		final String signInRoute = "/javaDbPrac/sign_in/page";
		UserAccService UAS = ctx.getBean(UserAccService.class);
		
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
