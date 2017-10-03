package com.harku.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserPageServlet {
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ShowPage(ModelMap model) {
		return "user";
	}
}
