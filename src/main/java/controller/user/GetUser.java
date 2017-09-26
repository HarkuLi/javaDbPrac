package controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bean.Config;
import model.user.UsersModel;
import service.user.UsersService;

public class GetUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

	/**
	 * response: user data
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		UsersService dbService = ctx.getBean(UsersService.class);
    	//HashMap<String, Object> rstMap = new HashMap<String, Object>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	
    	UsersModel user = dbService.getUser(id);
    	user.eraseSecretInfo();
    	
    	rstObj = new JSONObject(user);
    	
    	res.setContentType("application/json");
    	out.println(rstObj);
    }
}