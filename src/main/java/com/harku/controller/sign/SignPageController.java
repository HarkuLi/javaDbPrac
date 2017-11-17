package com.harku.controller.sign;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.harku.config.ConstantConfig;
import com.harku.model.User;
import com.harku.service.UserAccountService;

@Controller
public class SignPageController {
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private AccountValidator accountValidator;
	
	@InitBinder
	private void initBinder(DataBinder binder) {
		binder.setValidator(accountValidator);
	}
		
	@RequestMapping(value = "/sign_in/page", method = RequestMethod.GET)
	public String ShowSingInPage(@CookieValue(value = "LOGIN_INFO", required = false) String LOGIN_INFO, ModelMap model) {
		
		//check token, and redirect to user page if signed in
		if(LOGIN_INFO != null && userAccountService.checkToken(LOGIN_INFO)) {
			return "redirect:/user/page";
		}
		
		User user = new User();
		model.addAttribute("account_form", user);
		
		return "views/signIn";
	}
	
	@RequestMapping(value = "/sign_in/page", method = RequestMethod.POST)
	public String SingInAction(@Valid @ModelAttribute("account_form") User user, Errors errors, ModelMap model, HttpServletResponse res) {
		
		//error generated in the validator
		if(errors.hasErrors()) {
			model.addAttribute("account_form", user);
			return "views/signIn";
		}
		
		String account = user.getAccount();
		String password = user.getPassword();
		User acc = userAccountService.getAcc(account);
		
		if(acc == null)	errors.rejectValue("account", "account.noMatch");
		else if(!acc.getState())	errors.rejectValue("account", "account.noMatch");
		else if(!BCrypt.checkpw(password, (String)acc.getPassword())) errors.rejectValue("password", "account.noMatch");
		
		if(errors.hasErrors()) {
			model.addAttribute("account_form", user);
			return "views/signIn";
		}
		
		long signInTime = System.currentTimeMillis();
		String token = genToken();
		
		//store the sign in info.
		acc.setSignInTime(signInTime);
		acc.setToken(token);
		userAccountService.updateAcc(acc);
		
		//set cookie
		Cookie cookie = new Cookie("LOGIN_INFO", token);
		cookie.setMaxAge(UserAccountService.EXPIRE_TIME_SEC);
		cookie.setPath(ConstantConfig.ROOT_ROUTE);
		res.addCookie(cookie);
		
		return "redirect:/user/page";
	}
	
	@RequestMapping(value = "/sign_up/page", method = RequestMethod.GET)
	public String ShowSingUpPage(@CookieValue(value = "LOGIN_INFO", required = false) String LOGIN_INFO) {
		
		//check token, and redirect to user page if signed in
		if(LOGIN_INFO != null && userAccountService.checkToken(LOGIN_INFO)) {
			return "redirect:/user/page";
		}
		
		return "views/signUp";
	}
	
	@RequestMapping(value = "/sign_out", method = RequestMethod.GET)
	public String SignOut(HttpServletResponse res) {
		
		Cookie cookie = new Cookie("LOGIN_INFO", "");
		cookie.setMaxAge(0);
		cookie.setPath(ConstantConfig.ROOT_ROUTE);
		res.addCookie(cookie);
		return "redirect:/sign_in/page";
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
