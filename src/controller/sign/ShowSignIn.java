package controller.sign;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowSignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/sign_in.jsp");
		rd.forward(req, res);
	}
}
