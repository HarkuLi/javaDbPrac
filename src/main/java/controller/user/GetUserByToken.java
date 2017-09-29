package controller.user;

import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import config.BeanConfig;
import model.user.UsersModel;
import service.user.UserAccService;
import service.user.UsersService;

/**
 * 
 *	response user info of the token
 */
@RestController
@RequestMapping("/user")
public class GetUserByToken{
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	
	@RequestMapping(value = "/get_by_token", method = RequestMethod.POST, produces = "application/json")
	public UsersModel post(@CookieValue("LOGIN_INFO") String LOGIN_INFO) {
		
		UsersService US = ctx.getBean(UsersService.class);
		UserAccService UAS = ctx.getBean(UserAccService.class);
		
		if(LOGIN_INFO == null) return null;
		
		//get id by token in the cookie
		HashMap<String, Object> acc = UAS.getAccByToken(LOGIN_INFO);
		String id = (String) acc.get("userId");
		
		//get user by id
		UsersModel user = US.getUser(id);
		user.eraseSecretInfo();
		
		//response
		return user;
	}
}
