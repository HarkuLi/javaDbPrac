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

import model.occ.OccModel;
import service.occ.OccService;

public class GetOccPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		OccService dbService = new OccService();
		int totalPage;
		ArrayList<OccModel> tableList;
		HashMap<String, String> filter = new HashMap<String, String>();
    	HashMap<String, Object> rstMap = new HashMap<String, Object>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	int page = Integer.parseInt(req.getParameter("page"));
    	filter.put("name", req.getParameter("name"));
    	filter.put("state", req.getParameter("state"));
    	
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
