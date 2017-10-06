package com.harku.controller.publicAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harku.config.BeanConfig;
import com.harku.model.interest.IntModel;
import com.harku.model.occ.OccModel;
import com.harku.service.interest.IntService;
import com.harku.service.occ.OccService;

@RestController
@RequestMapping("/public")
public class PublicRestController {
	
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	private static final OccService occService = ctx.getBean(OccService.class);
	private static final IntService intService = ctx.getBean(IntService.class);
	
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
}
