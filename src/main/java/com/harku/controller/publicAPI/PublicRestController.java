package com.harku.controller.publicAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	@RequestMapping(value = "/e_test", method = RequestMethod.GET, produces = "application/json")
	public Map<String, Object> ExceptionTest() throws Exception {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("msg", "success");
		
		if(!rstMap.isEmpty())	throw new Exception("Error test.");
    	
    	return rstMap;
	}
}
