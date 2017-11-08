package com.harku.test.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.harku.config.ConstantConfig;
import com.harku.interceptor.SignInInterceptor;
import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestSignInInterceptor {
	
	private Object handler = new Object();
	
	@Mock
	private UserAccService userAccService;
	
	@Mock
	private HttpServletRequest req;
	
	@Mock
	private HttpServletResponse res;
	
	@InjectMocks
	private SignInInterceptor signInInterceptor;
	
	@Test
	public void signedIn() throws Exception {
		//set test data
		final UsersModel signedInUser = RandomData.genUser();
		final Cookie LOGIN_INFO = new Cookie("LOGIN_INFO", signedInUser.getToken());
		final Cookie[] cookies = {LOGIN_INFO};
		
		//set Stubs
		when(req.getCookies()).thenReturn(cookies);
		when(userAccService.checkToken(signedInUser.getToken())).thenReturn(true);
		
		//call the function and verify
		assertTrue(signInInterceptor.preHandle(req, res, handler));
		verify(res, never()).sendRedirect(anyString());
	}
	
	@Test
	public void noCookie() throws Exception {
		//set test data
		final Cookie[] cookies = null;
		
		//set Stubs
		when(req.getCookies()).thenReturn(cookies);
		
		//call the function and verify
		assertFalse(signInInterceptor.preHandle(req, res, handler));
		verify(res).sendRedirect(ConstantConfig.SIGN_IN_ROUTE);
	}
	
	@Test
	public void haveCookieWithoutLOGIN_INFO() throws Exception {
		//set test data
		final Cookie arbitraryCookie = new Cookie("arbitraryCookie", "arbitraryCookie");
		final Cookie[] cookies = {arbitraryCookie};
		
		//set Stubs
		when(req.getCookies()).thenReturn(cookies);
		
		//call the function and verify
		assertFalse(signInInterceptor.preHandle(req, res, handler));
		verify(res).sendRedirect(ConstantConfig.SIGN_IN_ROUTE);
	}
	
	@Test
	public void invalidToken() throws Exception {
		//set test data
		final String invalidToken = "invalidToken";
		final Cookie LOGIN_INFO = new Cookie("LOGIN_INFO", invalidToken);
		final Cookie[] cookies = {LOGIN_INFO};
		
		//set Stubs
		when(req.getCookies()).thenReturn(cookies);
		when(userAccService.checkToken(invalidToken)).thenReturn(false);
		
		//call the function and verify
		assertFalse(signInInterceptor.preHandle(req, res, handler));
		verify(res).sendRedirect(ConstantConfig.SIGN_IN_ROUTE);
	}	
}
