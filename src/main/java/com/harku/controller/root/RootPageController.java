package com.harku.controller.root;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
public class RootPageController {
	@RequestMapping(value = "/**", method = RequestMethod.GET)
	public String showDefault() throws NoHandlerFoundException {
		throw new NoHandlerFoundException(null, null, null);
	}
	
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String showSettingPage() {
		return "views/setting";
	}
}
