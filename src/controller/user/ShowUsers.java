package controller.user;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;


public class ShowUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		//for breadcrumbs
		ArrayList<String> pathList = new ArrayList<String>();
		pathList.add("javaDbPrac");
		pathList.add("user");
		req.setAttribute("pathList", pathList);
		
    	RequestDispatcher rd = req.getRequestDispatcher("/showUser.jsp");
    	rd.forward(req, res);
    }
}