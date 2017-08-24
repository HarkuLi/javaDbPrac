package controller.user;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import model.user.UsersModel;
import service.user.UsersService;

import java.util.ArrayList;

public class ShowUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
    	UsersService dbService = new UsersService();
    	ArrayList<UsersModel> tableList = dbService.getPage(1);
    	req.setAttribute("tableList", tableList);
    	
    	RequestDispatcher rd = req.getRequestDispatcher("/showUser.jsp");
    	rd.forward(req, res);
    }
}