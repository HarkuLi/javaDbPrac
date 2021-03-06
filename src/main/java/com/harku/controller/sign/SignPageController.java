package com.harku.controller.sign;

import java.util.Map;

import javax.annotation.Resource;
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
	
	@Resource(name = "statusOption")
	private Map<String, String> statusOption;
	
	@InitBinder
	private void initBinder(DataBinder binder) {
		binder.setValidator(accountValidator);
	}
		
	@RequestMapping(value = "/sign_in/page", method = RequestMethod.GET)
	public String showSingInPage(@CookieValue(value = ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, required = false) String LOGIN_INFO, ModelMap model) {
		
		//check token, and redirect to user page if signed in
		if(LOGIN_INFO != null && userAccountService.checkToken(LOGIN_INFO)) {
			return "redirect:/user/page";
		}
		
		User user = new User();
		model.addAttribute("account_form", user);
		
		return "views/signIn";
	}
	
	@RequestMapping(value = "/sign_in/page", method = RequestMethod.POST)
	public String singInAction(@Valid @ModelAttribute("account_form") User user, Errors errors, ModelMap model, HttpServletResponse res) {
		
		//error generated in the validator
		if(errors.hasErrors()) {
			model.addAttribute("account_form", user);
			return "views/signIn";
		}
		
		String account = user.getAccount();
		String password = user.getPassword();
		User acc = userAccountService.getAcc(account);
		
		if(acc == null)	errors.rejectValue("account", "account.noMatch");
		else if(!statusOption.get(acc.getState()).equals("enable"))	errors.rejectValue("account", "account.noMatch");
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
		Cookie cookie = new Cookie(ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, token);
		cookie.setMaxAge(ConstantConfig.EXPIRE_TIME_SEC);
		cookie.setPath(ConstantConfig.ROOT_ROUTE);
		res.addCookie(cookie);
		
		return "redirect:/user/page";
	}
	
	@RequestMapping(value = "/sign_up/page", method = RequestMethod.GET)
	public String showSingUpPage(@CookieValue(value = ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, required = false) String LOGIN_INFO) {
		
		//check token, and redirect to user page if signed in
		if(LOGIN_INFO != null && userAccountService.checkToken(LOGIN_INFO)) {
			return "redirect:/user/page";
		}
		
		return "views/signUp";
	}
	
	@RequestMapping(value = "/sign_out", method = RequestMethod.GET)
	public String signOut(HttpServletResponse res) {
		
		Cookie cookie = new Cookie(ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, "");
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
