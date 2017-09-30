package controller.publicAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import config.BeanConfig;
import model.occ.OccModel;
import service.occ.OccService;

@RestController
@RequestMapping("/public")
public class PublicRestServlet {
	
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	private static final OccService occService = ctx.getBean(OccService.class);
	
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
}
