package controller.occ;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import service.occ.OccService;

public class GetOcc extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * response: user data
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		OccService dbService = new OccService();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	
    	rstObj = new JSONObject(dbService.getOcc(id));
    	
    	res.setContentType("application/json");
    	out.println(rstObj);
    }
}