package controller.interest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bean.Config;
import service.interest.IntService;

public class DeleteInt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		IntService dbService = ctx.getBean(IntService.class);
    	
    	//get passed parameters
    	String id = req.getParameter("id");

    	dbService.delete(id);
    }
}