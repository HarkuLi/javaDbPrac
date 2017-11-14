package com.harku.controller.publicAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.harku.config.ConstantConfig;
import com.harku.model.InterestModel;
import com.harku.model.OccupationModel;
import com.harku.service.InterestService;
import com.harku.service.OccupationService;

@RestController
@RequestMapping("/public")
public class PublicRestController {
	@Autowired
	private OccupationService occupationService;
	@Autowired
	private InterestService interestService;
	@Autowired
	private LocaleResolver localeResolver;
	
	/**
	 * 
	 * get occupation list
	 * response:
	 * 200:
	 * {
	 *   list: Array<Object>
	 * }
	 */
	@RequestMapping(value = "/get_occupation_list", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Map<String, Object>> GetOccList() {
		
		ArrayList<OccupationModel> occList = occupationService.getList();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("list", occList);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	/**
	 * 
	 * get interest list
	 * response:
	 * 200:
	 * {
	 *   list: Array<Object>
	 * }
	 */
	@RequestMapping(value = "/get_interest_list", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Map<String, Object>> GetIntList() {
		
		ArrayList<InterestModel> interestList = interestService.getList();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("list", interestList);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	/**
	 * 
	 * response:
	 * 200:
	 * 400:
	 * {
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/set_language", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> SetLanguage(@RequestParam String language, HttpServletRequest req, HttpServletResponse res) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		
		String[] languageToken = language.split("_");
		if(languageToken.length != 2) {
			rstMap.put("errMsg", "Wrong format.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
		}
		
		Locale locale = new Locale(languageToken[0], languageToken[1]);
		localeResolver.setLocale(req, res, locale);
		
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	/**
	 * 
	 * response:
	 * 200:
	 * {
	 *   language: String
	 * }
	 */
	@RequestMapping(value = "/get_current_language", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Map<String, Object>> GetCurrentLanguage(HttpServletRequest req) {
		
		Locale locale = localeResolver.resolveLocale(req);
		if(!Arrays.asList(ConstantConfig.SUPPORTED_LOCALES).contains(locale)) {
			locale = ConstantConfig.DEFAULT_LOCALE;
		}
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("language", locale.toString());
		return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
}
