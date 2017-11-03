package com.harku.test.validator;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.harku.model.user.UsersModel;
import com.harku.test.util.RandomData;
import com.harku.validator.user.AccountValidator;

@RunWith(MockitoJUnitRunner.class)
public class TestAccountValidator {
	private final String accountField = "account";
	private final String passwordField = "password";
	private final String accountErrorMsgName = "account.notFilled";
	private final String passwordErrorMsgName = "password.notFilled";
	
	@Mock
	private Errors e;
	
	private AccountValidator accountValidator = new AccountValidator();
	
	@Test
	public void validAccount() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		String plainPassword = RandomData.genStr(8, 20);
		inputAccount.setPassword(plainPassword);
				
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e, never()).rejectValue(accountField, accountErrorMsgName);
		verify(e, never()).rejectValue(passwordField, passwordErrorMsgName);
	}
	
	@Test
	public void nullAccount() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		String plainPassword = RandomData.genStr(8, 20);
		inputAccount.setAccount(null);
		inputAccount.setPassword(plainPassword);
				
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e).rejectValue(accountField, accountErrorMsgName);
	}
	
	@Test
	public void zeroLengthAccount() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		String plainPassword = RandomData.genStr(8, 20);
		inputAccount.setAccount("");
		inputAccount.setPassword(plainPassword);
				
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e).rejectValue(accountField, accountErrorMsgName);
	}
	
	@Test
	public void nullPassword() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		inputAccount.setPassword(null);
				
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e).rejectValue(passwordField, passwordErrorMsgName);
	}
	
	@Test
	public void zeroLengthPassword() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		inputAccount.setPassword("");
				
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e).rejectValue(passwordField, passwordErrorMsgName);
	}
}
