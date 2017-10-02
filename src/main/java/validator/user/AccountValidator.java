package validator.user;

import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import config.BeanConfig;
import model.user.UsersModel;
import service.user.UserAccService;

public class AccountValidator implements Validator {
	private final ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
	private final UserAccService UAS = ctx.getBean(UserAccService.class);
	
	public boolean supports(Class<?> clazz) {
		return UsersModel.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		UsersModel user = (UsersModel) obj;
		String account = user.getAccount();
		String password = user.getPassword();
    	
    	//check whether the account exists
    	HashMap<String, Object> acc = UAS.getAcc(account);
    	if(acc == null)	e.rejectValue("account", "account.noMatch");
    	else if(!(Boolean)acc.get("state"))	e.rejectValue("account", "account.noMatch");
    	else if(!BCrypt.checkpw(password, (String)acc.get("password"))) e.rejectValue("password", "account.noMatch");
	}

}
