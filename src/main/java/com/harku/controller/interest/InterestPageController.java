package com.harku.controller.interest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/interest")
public class InterestPageController {
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ShowPage() {
		return "views/interest";
	}
}
