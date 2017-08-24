package controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import service.user.UsersService;

public class GetUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * response: user data
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		UsersService dbService = new UsersService();
    	//HashMap<String, Object> rstMap = new HashMap<String, Object>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	
    	rstObj = new JSONObject(dbService.getUser(id));
    	
    	res.setContentType("application/json");
    	out.println(rstObj);
    }
}