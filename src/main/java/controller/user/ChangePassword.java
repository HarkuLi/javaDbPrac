package controller.user;

import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import config.BeanConfig;
import service.user.UserAccService;

/**
 * 
 * @response
 * 		{
 * 			errMsg: String		//if error
 * 		}
 */
@RestController
@RequestMapping("/user")
public class ChangePassword {
	//workload for bcrypt
	private static final int workload = 12;
	
	private static final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	private final UserAccService UAS = ctx.getBean(UserAccService.class);
	
	@RequestMapping(value = "/change_password", method = RequestMethod.POST, produces = "application/json")
	public HashMap<String, String> post(
		@RequestParam String id,
		@RequestParam(required = false) String account,
		@RequestParam String password,
		@RequestParam String passwordCheck) {
		
		System.out.println("change password controller");
		System.out.println("id: " + id);
		
		HashMap<String, String> rstMap = new HashMap<String, String>();
		
		//check data
    	HashMap<String, Object> originalAcc = UAS.getAccById(id);
    	String errMsg = checkData(originalAcc, account, password, passwordCheck);
    	if(errMsg != null) {
    		rstMap.put("errMsg", errMsg);
        	return rstMap;
    	}
    	
    	//hash the password
    	password = BCrypt.hashpw(password, BCrypt.gensalt(workload));
    	
    	//call service to update the password
    	HashMap<String, Object> setData = new HashMap<String, Object>();
    	setData.put("userId", id);
    	if(originalAcc.get("account") == null)
    		setData.put("account", account);
    	setData.put("password", password);
    	UAS.updateAcc(setData);
		
    	System.out.println("update a password successfully");
    	
		return null;
	}
	
	private String checkData(HashMap<String, Object> originalAcc, String account, String password, String passwordCheck) {
		//check account name
    	if(originalAcc.get("account") == null && UAS.isAccExist(account)) {
    		return "The account name already exists.";
    	}
    	
    	//check whether the password is equal to the checked password
    	if(!passwordCheck.equals(password)) {
    		return "The checked password doesn't match.";
    	}
    	
    	return null;
	}
}
