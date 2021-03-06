package com.harku.controller.occupation;

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
import com.harku.model.Occupation;
import com.harku.service.OccupationService;

@RestController
@RequestMapping("/occupation")
public class OccupationRestController {
	@Autowired
	private OccupationService occupationService;
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
	public ResponseEntity<Map<String, Object>> updateOcc(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String state) {
		
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("id", id);
    	StringBuffer errMsg = new StringBuffer();
    	
    	//check data
    	Occupation nameFilter = new Occupation();
    	nameFilter.setName(name);
			//id
		if(occupationService.getOcc(id) == null)
			errMsg.append("No occupation matches the id.");
			//name
		if(name.length() > ConstantConfig.MAX_NAME_LENGTH)
			errMsg.append("The name can't be longer than " + ConstantConfig.MAX_NAME_LENGTH + " characters.\n");
		else {
			List<Occupation> occupationList = occupationService.getPage(1, nameFilter);
			//There is an another occupation with the same name.
			//In other words, there is an occupation with the name,
			//and the occupation isn't the current updated one.
			if(occupationList.size() > 0 && !occupationList.get(0).getId().equals(id))
				errMsg.append("The name is already used.\n");
		}
			//state
		if(!statusOption.containsKey(state))
			errMsg.append("Invalid state");
		if(errMsg.length() != 0) {
    		rstMap.put("errMsg", errMsg.toString());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	occupationService.update(id, name, state);
    	
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
	public ResponseEntity<Map<String, Object>> newOcc(
		@RequestParam String name,
		@RequestParam String state) {
    	
		Map<String, Object> rstMap = new HashMap<String, Object>();
		
		StringBuffer errMsg = new StringBuffer();
    	
    	//check data
			//name
		Occupation nameFilter = new Occupation();
    	nameFilter.setName(name);
		if(name.length() > ConstantConfig.MAX_NAME_LENGTH) {
			errMsg.append("The name can't be longer than " + ConstantConfig.MAX_NAME_LENGTH + " characters.\n");
		}
		else if(occupationService.getTotalPage(nameFilter) > 0) {
			errMsg.append("The name is already used.\n");
		}
			//state
		if(!statusOption.containsKey(state))
			errMsg.append("Invalid state");
    	if(errMsg.length() != 0) {
    		rstMap.put("errMsg", errMsg.toString());
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
    	}
    	
    	occupationService.createOcc(name, state);
    	
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
	public ResponseEntity<Map<String, Object>> getPage(
		@RequestParam int page,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String state) {
    	
		int totalPage;
		ArrayList<Occupation> tableList;
		Occupation filter = new Occupation();
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	
    	//set filter
    	filter.setName(name);
    	filter.setState(state);
    	
    	//check page range
    	totalPage = occupationService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		//set result
		tableList = occupationService.getPage(page, filter);
		rstMap.put("list", tableList);
    	rstMap.put("totalPage", totalPage);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
    }
	
	/**
	 * response:
	 * 200:
	 *   occupation data
	 * 404: (no occupation matches the id)
	 */
	@RequestMapping(value = "/get_one", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Occupation> getOcc(@RequestParam String id) {
		
    	Occupation occupation = occupationService.getOcc(id);
    	if(occupation == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(occupation);
	}
	
	/**
	 * response:
	 * 200:
	 *   {
	 *   	id: String
	 *   }
	 * 400: (no occupation matches the id)
	 *   {
	 *   	id: String,
	 *   	errMsg: String
	 *   }
	 */
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Map<String, Object>> deleteOcc(@RequestParam String id) {
		
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("id", id);
		
		if(occupationService.getOcc(id) == null) {
			rstMap.put("errMsg", "No occupation matches the id.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rstMap);
		}
		
    	occupationService.delete(id);
    	return ResponseEntity.status(HttpStatus.OK).body(rstMap);
	}
}











