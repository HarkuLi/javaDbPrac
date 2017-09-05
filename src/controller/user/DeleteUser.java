package controller.user;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.user.UsersService;

public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		UsersService dbService = new UsersService();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	
    	//delete the photo
		String photoName = dbService.getUser(id).getPhotoName();
		if(photoName != null) {
			String path = STORE_PATH + photoName;
			File file = new File(path);
			if(file.exists()) file.delete();
		}
    	
    	dbService.delete(id);
    }
}