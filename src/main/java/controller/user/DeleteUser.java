package controller.user;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import config.BeanConfig;
import service.user.UsersService;

@RestController
@RequestMapping("/user")
public class DeleteUser {
	private static final String STORE_PATH = System.getProperty("user.home") + "/upload/";
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	
	@RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json")
	public void post(@RequestParam String id) {
		System.out.println("spring controller");
		
		UsersService dbService = ctx.getBean(UsersService.class);
		
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