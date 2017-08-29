package controller.interest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.interest.IntService;

public class DeleteInt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		IntService dbService = new IntService();
    	
    	//get passed parameters
    	String id = req.getParameter("id");

    	dbService.delete(id);
    }
}