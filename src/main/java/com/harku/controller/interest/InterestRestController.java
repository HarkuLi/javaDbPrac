package com.harku.controller.interest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harku.config.ConstantConfig;
import com.harku.model.Interest;
import com.harku.service.InterestService;

@RestController
@RequestMapping("/interest")
public class InterestRestController {
	@Autowired
	private InterestService interestService;
	@Resource(name = "statusOption")
	private Map<String, String> statusOption;
	
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
    	StringBuffer errMsg = new StringBuffer();
		
    	//check data
    	Interest nameFilter = new Interest();
    	nameFilter.setName(name);
			//id
		if(interestService.getInterest(id) == null)
			errMsg.append("No interest matches the id.");
			//name
		if(name.length() > ConstantConfig.MAX_NAME_LENGTH)
			errMsg.append("The name can't be longer than " + ConstantConfig.MAX_NAME_LENGTH + " characters.\n");
		else {
			List<Interest> interestList = interestService.getPage(1, nameFilter);
			//There is an another interest with the same name.
			//In other words, there is an interest with the name,
			//and the interest isn't the current updated one.
			if(interestList.size() > 0 && !interestList.get(0).getId().equals(id))
				errMsg.append("The name is already used.\n");
		}
			//state
		if(!statusOption.containsKey(state))
			errMsg.append("Invalid state");
		if(errMsg.length() != 0) {
    		rstMap.put("errMsg", errMsg.toString());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	interestService.update(id, name, state);
    	
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
    	
		StringBuffer errMsg = new StringBuffer();
		
    	//check data
			//name
		Interest nameFilter = new Interest();
    	nameFilter.setName(name);
		if(name.length() > ConstantConfig.MAX_NAME_LENGTH) {
			errMsg.append("The name can't be longer than " + ConstantConfig.MAX_NAME_LENGTH + " characters.\n");
		}
		else if(interestService.getTotalPage(nameFilter) > 0) {
			errMsg.append("The name is already used.\n");
		}
			//state
		if(!statusOption.containsKey(state))
			errMsg.append("Invalid state");
    	if(errMsg.length() != 0) {
    		rstMap.put("errMsg", errMsg.toString());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	interestService.createInt(name, state);
    	
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
		ArrayList<Interest> tableList;
		Interest filter = new Interest();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	
    	//set filter
    	filter.setName(name);
    	if(state != null) filter.setState(state);
    	
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
	public ResponseEntity<Interest> GetInterest(@RequestParam String id) {
		
		Interest interest = interestService.getInterest(id);
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








