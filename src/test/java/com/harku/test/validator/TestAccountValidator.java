package com.harku.test.validator;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;
import com.harku.test.util.RandomData;
import com.harku.validator.user.AccountValidator;

@RunWith(MockitoJUnitRunner.class)
public class TestAccountValidator {
	private final String accountField = "account";
	private final String passwordField = "password";
	private final String errorMsgName = "account.noMatch";
	
	@Mock
	private Errors e;
	
	@Mock
	private UserAccService UAS;
	
	@Autowired
	@InjectMocks
	private AccountValidator accountValidator;
	
	@Test
	public void validAccount() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		String plainPassword = RandomData.genStr(8, 20);
		inputAccount.setPassword(plainPassword);
		String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(RandomData.workload));
		
		//generate account read from db
		UsersModel DBAccount = new UsersModel();
		DBAccount.setAccount(inputAccount.getAccount());
		DBAccount.setPassword(hashedPassword);
		DBAccount.setState(true);
		
		//set Stub
		when(UAS.getAcc(inputAccount.getAccount())).thenReturn(DBAccount);
		
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e, never()).rejectValue(accountField, errorMsgName);
	}
	
	@Test
	public void notExistingAccount() {
		UsersModel inputAccount = RandomData.genUser();
		String plainPassword = RandomData.genStr(8, 20);
		inputAccount.setPassword(plainPassword);
		
		//set Stub
		when(UAS.getAcc(inputAccount.getAccount())).thenReturn(null);
		
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e, times(1)).rejectValue(accountField, errorMsgName);
	}
	
	@Test
	public void disabledAccount() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		String plainPassword = RandomData.genStr(8, 20);
		inputAccount.setPassword(plainPassword);
		
		//generate account read from db
		UsersModel DBAccount = new UsersModel();
		DBAccount.setAccount(inputAccount.getAccount());
		DBAccount.setState(false);
		
		//set Stub
		when(UAS.getAcc(inputAccount.getAccount())).thenReturn(DBAccount);
		
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e, times(1)).rejectValue(accountField, errorMsgName);
	}
	
	@Test
	public void onlyWrongPassword() {
		//generate the account that user inputs
		UsersModel inputAccount = RandomData.genUser();
		String plainPassword = RandomData.genStr(8, 20);
		inputAccount.setPassword(plainPassword);
		
		//generate account read from db
		UsersModel DBAccount = new UsersModel();
		DBAccount.setAccount(inputAccount.getAccount());
		String hashedPassword = BCrypt.hashpw(plainPassword + "123", BCrypt.gensalt(RandomData.workload));
		DBAccount.setPassword(hashedPassword);
		DBAccount.setState(true);
		
		//set Stub
		when(UAS.getAcc(inputAccount.getAccount())).thenReturn(DBAccount);
		
		//call function
		accountValidator.validate(inputAccount, e);
		
		//verify
		verify(e, times(1)).rejectValue(passwordField, errorMsgName);
	}
}
