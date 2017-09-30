package controller.sign;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import config.BeanConfig;
import service.user.UserAccService;

@Controller
public class SignPageServlet {
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	private static final UserAccService UAS = ctx.getBean(UserAccService.class);
	
	
	@RequestMapping(value = "/sign_in/page", method = RequestMethod.GET)
	public String ShowSingInPage(@CookieValue(value = "LOGIN_INFO", required = false) String LOGIN_INFO) {
		
		//check token, and redirect to user page if signed in
		if(LOGIN_INFO != null && UAS.checkToken(LOGIN_INFO)) {
			return "redirect:/user/page";
		}
		
		return "sign_in";
	}
	
	@RequestMapping(value = "/sign_up/page", method = RequestMethod.GET)
	public String ShowSingUpPage(@CookieValue(value = "LOGIN_INFO", required = false) String LOGIN_INFO) {
		
		//check token, and redirect to user page if signed in
		if(LOGIN_INFO != null && UAS.checkToken(LOGIN_INFO)) {
			return "redirect:/user/page";
		}
		
		return "sign_up";
	}
	
	@RequestMapping(value = "/sign_out", method = RequestMethod.GET)
	public String SignOut(HttpServletResponse res) {
		
		Cookie cookie = new Cookie("LOGIN_INFO", "");
		cookie.setMaxAge(0);
		cookie.setPath("/javaDbPrac");
		res.addCookie(cookie);
		return "redirect:/sign_in/page";
	}
	
	
}
