package controller.interest;

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

import bean.Config;
import model.interest.IntModel;
import service.interest.IntService;

public class GetIntList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

	/**
	 * response: {list: Array<Object>} interest list
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		IntService dbService = ctx.getBean(IntService.class);
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	ArrayList<IntModel> interestList = dbService.getList();
    	HashMap<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap.put("list", interestList);
    	
    	rstObj = new JSONObject(rstMap);
    	
    	res.setContentType("application/json");
    	out.println(rstObj);
    }
}