package com.harku.controller.user;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserPageController {
	
	@Resource(name = "statusOption")
	Map<String, String> statusOption;
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String showPage(ModelMap model) {
		model.addAttribute("statusOption", statusOption);
		return "views/user";
	}
	@RequestMapping(value = "/new_form", method = RequestMethod.GET)
	public String getNewForm(ModelMap model) {
		model.addAttribute("statusOption", statusOption);
		return "partial/user/detailNew";
	}
	@RequestMapping(value = "/edit_form", method = RequestMethod.GET)
	public String getEditForm(ModelMap model) {
		model.addAttribute("statusOption", statusOption);
		return "partial/user/detailEdit";
	}
	@RequestMapping(value = "/change_password_form", method = RequestMethod.GET)
	public String getChangePasswordForm(ModelMap model) {
		return "partial/user/changePassword";
	}
}
