package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;

import model.user.UsersModel;
import service.user.UsersService;

public class GetUserPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String datePattern = "yyyy-MM-dd";

	/**
	 * response format:
	 * {
	 *   list: Array<Object>,
	 *   totalPage: Number
	 * }
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		UsersService dbService = new UsersService();
		int totalPage;
		ArrayList<UsersModel> tableList;
		HashMap<String, Object> filter = new HashMap<String, Object>();
    	HashMap<String, Object> rstMap = new HashMap<String, Object>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	int page = Integer.parseInt(req.getParameter("page"));
    	String name = req.getParameter("name");
    	String birthFrom = req.getParameter("birthFrom");
    	String birthTo = req.getParameter("birthTo");
    	String occ = req.getParameter("occ");
    	String state = req.getParameter("state");
    	String[] interest = req.getParameterValues("interest[]");
    	
    	//set filter
    	filter.put("name", name);
    	try {
			DateFormat sdf = new SimpleDateFormat(datePattern);
			//note: the type Date here is java.sql.date
			//      but sdf.parse(String) returns java.util.date
			if(birthFrom != null) {
		    	Date birthFromDate = new Date(sdf.parse(birthFrom).getTime());
		    	filter.put("birthFrom", birthFromDate);
			}
			if(birthTo != null) {
				Date birthToDate = new Date(sdf.parse(birthTo).getTime());
		    	filter.put("birthTo", birthToDate);
			}
		} catch (Exception e) {
			System.out.println("Exception in GetUserPage: " + e.toString());
		}
    	filter.put("occ", occ);
    	if(state != null)
    		filter.put("state", state.equals("1"));
    	filter.put("interest", interest);    	
    	
    	//check page range
    	totalPage = dbService.getTotalPage(filter);
		if(page < 1) page = 1;
		else if(page > totalPage) page = totalPage;
		
		if(page > 0) {
			tableList = dbService.getPage(page, filter);
			rstMap.put("list", tableList);
		}
		
    	rstMap.put("totalPage", totalPage);
    	rstObj = new JSONObject(rstMap);
    	
    	res.setContentType("application/json");
    	out.println(rstObj);
    }
}