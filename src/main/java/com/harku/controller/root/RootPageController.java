package com.harku.controller.root;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RootPageController {
	@RequestMapping(value = "/**", method = RequestMethod.GET)
	public String ShowDefault() {
		return "views/not_found";
	}
	
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String ShowSettingPage() {
		return "views/setting";
	}
}
