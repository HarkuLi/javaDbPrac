package controller.occ;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowOcc extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		//for breadcrumbs
		ArrayList<String> pathList = new ArrayList<String>();
		pathList.add("javaDbPrac");
		pathList.add("occ");
		req.setAttribute("pathList", pathList);
		
		RequestDispatcher rd = req.getRequestDispatcher("/occupation.jsp");
		rd.forward(req, res);
	}
}
