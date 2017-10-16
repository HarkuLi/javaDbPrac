package com.harku.controller.publicAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.harku.model.interest.IntModel;
import com.harku.model.occ.OccModel;
import com.harku.service.interest.IntService;
import com.harku.service.occ.OccService;

@RestController
@RequestMapping("/public")
public class PublicRestController {
	@Autowired
	private OccService occService;
	@Autowired
	private IntService intService;
	@Autowired
	private LocaleResolver localeResolver;
	
	/**
	 * response: {list: Array<Object>} occupation list
	 */
	@RequestMapping(value = "/get_occ_list", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> GetOccList() {
		
		ArrayList<OccModel> occList = occService.getList();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("list", occList);
    	
    	return rstMap;
	}
	
	/**
	 * response: {list: Array<Object>} interest list
	 */
	@RequestMapping(value = "/get_interest_list", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> GetIntList() {
		
		ArrayList<IntModel> interestList = intService.getList();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("list", interestList);
    	
    	return rstMap;
	}
	
	@RequestMapping(value = "/set_language", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> SetLanguage(@RequestParam String language, HttpServletRequest req, HttpServletResponse res) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		
		String[] languageToken = language.split("_");
		if(languageToken.length != 2) {
			rstMap.put("errMsg", "Wrong format.");
			return rstMap;
		}
		
		Locale locale = new Locale(languageToken[0], languageToken[1]);
		
		localeResolver.setLocale(req, res, locale);
		rstMap.put("msg", "Success.");
		
    	return rstMap;
	}
	
	@RequestMapping(value = "/get_current_language", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> GetCurrentLanguage(HttpServletRequest req) {
		
		Locale locale = localeResolver.resolveLocale(req);
		String language = locale.getLanguage();
		String country = locale.getCountry();
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("language", language + "_" + country);
		return rstMap;
	}
	
	@RequestMapping(value = "/e_test", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> ExceptionTest() throws Exception {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("msg", "success");
		
		if(!rstMap.isEmpty())	throw new Exception("Error test.");
    	
    	return rstMap;
	}
}
