package controller.occ;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import service.occ.OccService;

public class UpdateOcc extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		OccService dbService = new OccService();
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