package com.harku.controller.interest;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/interest")
public class InterestPageController {
	
	@Resource(name = "statusOption")
	private Map<String, String> statusOption;
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ShowPage(ModelMap model) {
		model.addAttribute("statusOption", statusOption);
		return "views/interest";
	}
}
