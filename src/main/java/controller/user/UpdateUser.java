package controller.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bean.Config;
import service.user.UsersService;

@MultipartConfig

public class UpdateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	private static final String datePattern = "yyyy-MM-dd";
	private static Logger log = LoggerFactory.getLogger(UpdateUser.class);
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {
    	
		UsersService dbService = ctx.getBean(UsersService.class);
    	HashMap<String, String> rstMap = new HashMap<String, String>();
		JSONObject rstObj;
    	PrintWriter out = res.getWriter();
    	
    	//get passed parameters
    	String id = req.getParameter("id");
    	String name = req.getParameter("name");
    	String age = req.getParameter("age");
    	String birth = req.getParameter("birth");
    	Part photo = req.getPart("photo");
    	String photoType = req.getParameter("photoType");
    	String photoName = req.getParameter("photoName");
    	String[] interest = req.getParameterValues("interest[]");
    	String occupation = req.getParameter("occupation");
    	String state = req.getParameter("state");
    	
    	//check data
    	String pattern = "^\\d+$";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(age);
    	if(!m.find()) {
	    	rstMap.put("errMsg", "Wrong input for age.");
	    	rstObj = new JSONObject(rstMap);
	    	res.setContentType("application/json");
	    	out.println(rstObj);
	    	return;
    	}
    	
    	//update photo
		if(photo.getSize() != 0) {
			//delete original photo
			if(photoName != null) {
				String path = STORE_PATH + "/" + photoName;
				File file = new File(path);
				if(file.exists()) file.delete();
			}
			
			//store new photo
			photoName = UUID.randomUUID().toString();
			photoName += "." + photoType;	//filename extension
			
			String path = STORE_PATH + photoName;
			
			File dir = new File(STORE_PATH);
			if(!dir.exists()) dir.mkdir();
			try {
				photo.write(path);
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
    	
    	//call service function
    	HashMap<String, Object> newData = new HashMap<String, Object>();
    	newData.put("id", id);
    	newData.put("name", name);
    	newData.put("age", Integer.parseInt(age));
    	
		try {
			DateFormat sdf = new SimpleDateFormat(datePattern);
			//note: the type Date here is java.sql.date
			//      but sdf.parse(String) returns java.util.date
	    	Date birthDate;
			birthDate = new Date(sdf.parse(birth).getTime());
			newData.put("birth", birthDate);
		} catch (Exception e) {
			log.error(e.toString());
			return;
		}
    	
    	if(photo.getSize() != 0) {
    		newData.put("photoName", photoName);
    	}
    	newData.put("interest", interest);
    	newData.put("occupation", occupation);
    	newData.put("state", state.equals("1"));
    	dbService.update(newData);
    }
}