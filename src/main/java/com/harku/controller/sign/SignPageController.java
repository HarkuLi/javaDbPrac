package com.harku.controller.sign;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;
import com.harku.validator.user.AccountValidator;

@Controller
public class SignPageController {
	@Autowired
	private UserAccService UAS;
	
	@InitBinder
	private void initBinder(DataBinder binder) {
		binder.setValidator(new AccountValidator());
	}
		
	@RequestMapping(value = "/sign_in/page", method = RequestMethod.GET)
	public String ShowSingInPage(@CookieValue(value = "LOGIN_INFO", required = false) String LOGIN_INFO, ModelMap model) {
		
		//check token, and redirect to user page if signed in
		if(LOGIN_INFO != null && UAS.checkToken(LOGIN_INFO)) {
			return "redirect:/user/page";
		}
		
		UsersModel user = new UsersModel();
		model.addAttribute("account_form", user);
		
		return "sign_in";
	}
	
	@RequestMapping(value = "/sign_in/page", method = RequestMethod.POST)
	public String SingInAction(@Valid @ModelAttribute("account_form") UsersModel user, Errors errors, ModelMap model, HttpServletResponse res) {
		
		if(errors.hasErrors()) {
			model.addAttribute("account_form", user);
			return "sign_in";
		}
		
		String account = user.getAccount();
		HashMap<String, Object> acc = UAS.getAcc(account);
		long signInTime = System.currentTimeMillis();
		String token = genToken();
		
		//store the sign in info.
		HashMap<String, Object> setData = new HashMap<String, Object>();
		setData.put("userId", (String)acc.get("userId"));
		setData.put("signInTime", signInTime);
		setData.put("token", token);
		UAS.updateAcc(setData);
		
		//set cookie
		Cookie cookie = new Cookie("LOGIN_INFO", token);
		cookie.setMaxAge(UserAccService.EXPIRE_TIME_SEC);
		cookie.setPath("/javaDbPrac");
		res.addCookie(cookie);
		
		return "redirect:/user/page";
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