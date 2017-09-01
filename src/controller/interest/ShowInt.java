package controller.interest;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowInt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		//for breadcrumbs
		ArrayList<String> pathList = new ArrayList<String>();
		pathList.add("javaDbPrac");
		pathList.add("interest");
		req.setAttribute("pathList", pathList);
		
		RequestDispatcher rd = req.getRequestDispatcher("/interest.jsp");
		rd.forward(req, res);
	}
}