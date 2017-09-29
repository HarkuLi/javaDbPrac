package controller.user;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import config.BeanConfig;
import service.user.UsersService;

@RestController
@RequestMapping("/user")
public class UpdateUser {
	private static final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	private static final String datePattern = "yyyy-MM-dd";
	private static Logger log = LoggerFactory.getLogger(UpdateUser.class);
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	
	/**
	 * response format:
	 * {
	 *   errMsg: String
	 * }
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, String> post(
		@RequestParam String id,
		@RequestParam String name,
		@RequestParam String age,
		@RequestParam String birth,
		@RequestParam(required = false) MultipartFile photo,
		@RequestParam(required = false) String photoType,
		@RequestParam(required = false) String photoName,
		@RequestParam(value = "interest[]", required = false) String[] interest,
		@RequestParam String occupation,
		@RequestParam String state) {
		
		System.out.println("update user controller");
		
		UsersService dbService = ctx.getBean(UsersService.class);
    	HashMap<String, String> rstMap = new HashMap<String, String>();
    	
    	//check data
    	String pattern = "^\\d+$";
    	Pattern r = Pattern.compile(pattern);
    	Matcher m = r.matcher(age);
    	if(!m.find()) {
	    	rstMap.put("errMsg", "Wrong input for age.");
	    	return rstMap;
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
				dir = new File(path);
				photo.transferTo(dir);
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
			return null;
		}
    	
    	if(photo.getSize() != 0) {
    		newData.put("photoName", photoName);
    	}
    	newData.put("interest", interest);
    	newData.put("occupation", occupation);
    	newData.put("state", state.equals("1"));
    	dbService.update(newData);
    	
    	return null;
	}
}