package com.harku.controller.occ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harku.model.occ.OccModel;
import com.harku.service.occ.OccService;

@RestController
@RequestMapping("/occ")
public class OccRestController {
	@Autowired
	private OccService dbService;
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public Map<String, String> UpdateOcc(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String state) {
		
    	Map<String, String> rstMap = new HashMap<String, String>();
    	
    	//check data
    	if(!state.equals("0") && !state.equals("1")) {
	    	rstMap.put("errMsg", "Wrong input for state.");
	    	return rstMap;
    	}
    	
    	dbService.update(id, name, state);
    	
    	return null;
	}
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = "application/json")
	public Map<String, String> NewOcc(
		@RequestParam String name,
		@RequestParam String state) {
    	
		Map<String, String> rstMap = new HashMap<String, String>();
    	
    	//check data
    	if(!state.equals("0") && !state.equals("1")) {
	    	rstMap.put("errMsg", "Wrong input for state.");
	    	return rstMap;
    	}
    	
    	dbService.createOcc(name, state);
    	
    	return null;
    }
	
	/**
	 * response format:
	 * {
	 *   list: Array<Object>,
	 *   totalPage: Number
	 * }
	 */
	@RequestMapping(value = "/get_page", method = RequestMethod.POST, produces = "application/json")
	public Map<String, Object> GetPage(
		@RequestParam int page,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String state) {
    	
		int totalPage;
		ArrayList<OccModel> tableList;
		HashMap<String, String> filter = new HashMap<String, String>();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	
    	//set filter
    	filter.put("name", name);
    	filter.put("state", state);
    	
    	//check page range
    	totalPage = dbService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		if(page > 0) {
			tableList = dbService.getPage(page, filter);
			rstMap.put("list", tableList);
		}
		
    	rstMap.put("totalPage", totalPage);
    	
    	return rstMap;
    }
	
	/**
	 * response: occupation data
	 */
	@RequestMapping(value = "/get_one", method = RequestMethod.POST, produces = "application/json")
	public OccModel GetOcc(@RequestParam String id) {
		
    	return dbService.getOcc(id);
	}
	
	
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
	public void DeleteOcc(@RequestParam String id) {
		
    	dbService.delete(id);
	}
}











