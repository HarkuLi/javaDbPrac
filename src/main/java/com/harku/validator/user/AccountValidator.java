package com.harku.validator.user;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;

@Component
public class AccountValidator implements Validator {
	@Autowired
	private UserAccService UAS;
	
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
