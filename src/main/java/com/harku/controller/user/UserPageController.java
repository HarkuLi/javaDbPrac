package com.harku.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserPageController {
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String showPage(ModelMap model) {
		return "views/user";
	}
	@RequestMapping(value = "/new_form", method = RequestMethod.GET)
	public String getNewForm(ModelMap model) {
		return "partial/user/detailNew";
	}
	@RequestMapping(value = "/edit_form", method = RequestMethod.GET)
	public String getEditForm(ModelMap model) {
		return "partial/user/detailEdit";
	}
	@RequestMapping(value = "/change_password_form", method = RequestMethod.GET)
	public String getChangePasswordForm(ModelMap model) {
		return "partial/user/changePassword";
	}
}
