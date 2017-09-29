package controller.user;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import config.BeanConfig;
import model.user.UsersModel;
import service.user.UsersService;

@RestController
@RequestMapping("/user")
public class GetUserPage{
	private static final String datePattern = "yyyy-MM-dd";
	private static Logger log = LoggerFactory.getLogger(GetUserPage.class);
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	
	/**
	 * response format:
	 * {
	 *   list: Array<Object>,
	 *   totalPage: Number
	 * }
	 */
	@RequestMapping(value = "/get_page", method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, Object> post(
		@RequestParam int page,
		@RequestParam(required=false) String name,
		@RequestParam(required=false) String birthFrom,
		@RequestParam(required=false) String birthTo,
		@RequestParam(required=false) String occ,
		@RequestParam(required=false) String state,
		@RequestParam(value = "interest[]", required=false) String[] interest) {
		
		UsersService dbService = ctx.getBean(UsersService.class);
		int totalPage;
		ArrayList<UsersModel> tableList;
		HashMap<String, Object> filter = new HashMap<String, Object>();
		HashMap<String, Object> rstMap = new HashMap<String, Object>();
		
		//set filter
    	filter.put("name", name);
    	try {
			DateFormat sdf = new SimpleDateFormat(datePattern);
			//note: the type Date here is java.sql.date
			//      but sdf.parse(String) returns java.util.date
			if(birthFrom != null) {
		    	Date birthFromDate = new Date(sdf.parse(birthFrom).getTime());
		    	filter.put("birthFrom", birthFromDate);
			}
			if(birthTo != null) {
				Date birthToDate = new Date(sdf.parse(birthTo).getTime());
		    	filter.put("birthTo", birthToDate);
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
    	filter.put("occ", occ);
    	if(state != null)
    		filter.put("state", state.equals("1"));
    	filter.put("interest", interest);
    	
    	//check page range
    	totalPage = dbService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		//set result
		if(page > 0) {
			tableList = dbService.getPage(page, filter);
			rstMap.put("list", tableList);
		}
		rstMap.put("totalPage", totalPage);
		
		return rstMap;
	}
}