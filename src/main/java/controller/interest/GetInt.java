package controller.interest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.BeanConfig;
import service.interest.IntService;

public class GetInt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);

	/**
	 * response: user data
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		IntService dbService = ctx.getBean(IntService.class);
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	
    	rstObj = new JSONObject(dbService.getInterest(id));
    	
    	res.setContentType("application/json");
    	out.println(rstObj);
    }
}