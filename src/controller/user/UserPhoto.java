package controller.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserPhoto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		String path = System.getProperty("user.home") + "/upload/";
		String filename = req.getParameter("n");
		
		File file = new File(path + filename);
		if(!file.exists()) {
			file = new File(path + "default.png");
		}
		
		res.setHeader("Content-Type", "image/jpeg");
		res.setHeader("Content-Length", String.valueOf(file.length()));
		//set file name
		res.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
		Files.copy(file.toPath(), res.getOutputStream());
    }
}