package com.harku.validator.user;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.harku.config.AppConfig;
import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;

public class AccountValidator implements Validator {
	private final ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
	private final UserAccService UAS = ctx.getBean(UserAccService.class);
	
	public boolean supports(Class<?> clazz) {
		return UsersModel.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		UsersModel user = (UsersModel) obj;
		String account = user.getAccount();
		String password = user.getPassword();
    	
    	//check whether the account exists
    	UsersModel acc = UAS.getAcc(account);
    	if(acc == null)	e.rejectValue("account", "account.noMatch");
    	else if(!acc.getState())	e.rejectValue("account", "account.noMatch");
    	else if(!BCrypt.checkpw(password, (String)acc.getPassword())) e.rejectValue("password", "account.noMatch");
	}

}
