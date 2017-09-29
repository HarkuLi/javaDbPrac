package controller.occ;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.BeanConfig;
import model.occ.OccModel;
import service.occ.OccService;

public class GetOccList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);

	/**
	 * response: {list: Array<Object>} occupation list
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		OccService dbService = ctx.getBean(OccService.class);
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	ArrayList<OccModel> occList = dbService.getList();
    	HashMap<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("list", occList);
    	
    	rstObj = new JSONObject(rstMap);
    	
    	res.setContentType("application/json");
    	out.println(rstObj);
    }
}
