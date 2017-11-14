package com.harku.controller.sign;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.harku.model.UserModel;

@Component
public class AccountValidator implements Validator {	
	public boolean supports(Class<?> clazz) {
		return UserModel.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		UserModel user = (UserModel) obj;
		String account = user.getAccount();
		String password = user.getPassword();
    	
    	//check whether the account exists
    	if(account == null || account.length() == 0)	e.rejectValue("account", "account.notFilled");
    	else if(password == null || password.length() == 0)	e.rejectValue("password", "password.notFilled");
	}

}
