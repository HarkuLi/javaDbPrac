package com.harku.controller.occ;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/occ")
public class OccPageServlet {
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String ShowPage(){
		return "occupation";
	}
}
