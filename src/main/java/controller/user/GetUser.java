package controller.user;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import config.BeanConfig;
import model.user.UsersModel;
import service.user.UsersService;

@RestController
@RequestMapping("/user")
public class GetUser {
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);

	/**
	 * response: user data
	 */
	@RequestMapping(value = "/get_one", method = RequestMethod.POST, produces = "application/json")
	public UsersModel post(@RequestParam String id) {
		System.out.println("spring controller");
		
		UsersService dbService = ctx.getBean(UsersService.class);
		
		UsersModel user = dbService.getUser(id);
    	user.eraseSecretInfo();
    	
    	return user;
	}
}