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

import model.interest.IntModel;
import service.interest.IntService;

public class GetIntList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * response: {list: Array<String>} interest list
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		IntService dbService = new IntService();
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