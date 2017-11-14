package com.harku.controller.occupation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/occupation")
public class OccupationPageController {
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ShowPage(){
		return "views/occupation";
	}
}
