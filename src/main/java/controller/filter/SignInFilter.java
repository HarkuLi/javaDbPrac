package controller.filter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bean.Config;
import service.user.UserAccService;

public class SignInFilter implements Filter {
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpRes = (HttpServletResponse) res;
		
		final String signInRoute = "/javaDbPrac/sign_in";
		UserAccService UAS = ctx.getBean(UserAccService.class);
		
		
		//check token
		HashMap<String, String> cookie = cookieHandle(httpReq.getCookies());
		if(!(cookie != null && UAS.checkToken(cookie.get("LOGIN_INFO")))) {
			httpRes.sendRedirect(signInRoute);
			return;
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
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
