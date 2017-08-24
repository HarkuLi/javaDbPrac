package controller.user;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShowUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
    	RequestDispatcher rd = req.getRequestDispatcher("/showUser.jsp");
    	rd.forward(req, res);
    }
}