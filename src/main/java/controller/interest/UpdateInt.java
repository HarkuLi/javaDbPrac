package controller.interest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bean.Config;
import service.interest.IntService;

public class UpdateInt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		IntService dbService = ctx.getBean(IntService.class);
    	HashMap<String, String> rstMap = new HashMap<String, String>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	String name = req.getParameter("name");
    	String state = req.getParameter("state");
    	
    	//check data
    	if(!state.equals("0") && !state.equals("1")) {
	    	rstMap.put("errMsg", "Wrong input for state.");
	    	rstObj = new JSONObject(rstMap);
	    	res.setContentType("application/json");
	    	out.println(rstObj);
	    	return;
    	}
    	
    	dbService.update(id, name, state);
    }
}