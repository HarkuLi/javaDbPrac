package com.harku.controller.interest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harku.model.interest.IntModel;
import com.harku.service.interest.IntService;

@RestController
@RequestMapping("/interest")
public class IntRestController {
	@Autowired
	private IntService dbService;
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public Map<String, String> UpdateInt(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String state) {
		
		Map<String, String> rstMap = new HashMap<String, String>();
    	
    	//check data
    	if(!state.equals("0") && !state.equals("1")) {
	    	rstMap.put("errMsg", "Wrong input for state.");
	    	return rstMap;
    	}
    	
    	dbService.update(id, name, state.equals("1"));
    	
    	return null;
	}
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = "application/json")
	public Map<String, String> NewInt(
		@RequestParam String name,
		@RequestParam String state) {
    	
		Map<String, String> rstMap = new HashMap<String, String>();
    	
    	//check data
    	if(!state.equals("0") && !state.equals("1")) {
	    	rstMap.put("errMsg", "Wrong input for state.");
	    	return rstMap;
    	}
    	
    	dbService.createInt(name, state.equals("1"));
    	
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
	public Map<String, Object> GetIntPage(
		@RequestParam int page,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String state) {
		
		int totalPage;
		ArrayList<IntModel> tableList;
		IntModel filter = new IntModel();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	
    	//set filter
    	filter.setName(name);
    	if(state != null)
    		filter.setState(state.equals("1"));
    	
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
	 * response: interest data
	 */
	@RequestMapping(value = "/get_one", method = RequestMethod.POST, produces = "application/json")
	public IntModel GetInterest(@RequestParam String id) {
		
    	return dbService.getInterest(id);
	}
	
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
	public void DeleteInterest(@RequestParam String id) {
    	
    	dbService.delete(id);
	}
	
}








