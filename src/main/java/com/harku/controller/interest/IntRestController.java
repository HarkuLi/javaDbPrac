package com.harku.controller.interest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private IntService interestService;
	
	/**
	 * 
	 * response:
	 * 200: (success)
	 * {
	 *   id: String
	 * }
	 * 400: (wrong input)
	 * {
	 *   id: String,
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> UpdateInt(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String state) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("id", id);
		
    	//check data
		if(interestService.getInterest(id) == null) {
			rstMap.put("errMsg", "No interest matches the id.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
		}
    	if(!state.equals("0") && !state.equals("1")) {
	    	rstMap.put("errMsg", "Wrong input for state.");
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	interestService.update(id, name, state.equals("1"));
    	
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	/**
	 * response:
	 * 201:
	 * 400: (wrong input)
	 * {
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> NewInt(
		@RequestParam String name,
		@RequestParam String state) {
    	
		Map<String, Object> rstMap = new HashMap<String, Object>();
    	
    	//check data
    	if(!state.equals("0") && !state.equals("1")) {
	    	rstMap.put("errMsg", "Wrong input for state.");
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	interestService.createInt(name, state.equals("1"));
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(rstMap);
    }
	
	/**
	 * response:
	 * 200:
	 * {
	 *   list: Array<Object>,
	 *   totalPage: Number
	 * }
	 */
	@RequestMapping(value = "/get_page", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> GetIntPage(
		@RequestParam int page,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String state) {
		
		int totalPage;
		ArrayList<IntModel> tableList;
		IntModel filter = new IntModel();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	
    	//set filter
    	filter.setName(name);
    	if(state != null) filter.setState(state.equals("1"));
    	
    	//check page range
    	totalPage = interestService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		//set result
		tableList = interestService.getPage(page, filter);
		rstMap.put("list", tableList);
    	rstMap.put("totalPage", totalPage);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
	
	/**
	 * response:
	 * 200:
	 *   interest data
	 * 404: (no interest matches the id)
	 */
	@RequestMapping(value = "/get_one", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<IntModel> GetInterest(@RequestParam String id) {
		
		IntModel interest = interestService.getInterest(id);
    	if(interest == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(interest);
	}
	
	/**
	 * response:
	 * 200:
	 *   {
	 *   	id: String
	 *   }
	 * 400: (no interest matches the id)
	 *   {
	 *   	id: String,
	 *   	errMsg: String
	 *   }
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> DeleteInterest(@RequestParam String id) {
    	
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("id", id);
		
		if(interestService.getInterest(id) == null) {
			rstMap.put("errMsg", "No interest matches the id.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
		}
		
    	interestService.delete(id);
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
}








