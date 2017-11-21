package com.harku.test.controller.sign;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.config.ConstantConfig;
import com.harku.controller.sign.SignPageController;
import com.harku.model.User;
import com.harku.service.UserAccountService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestSignUpPage {
	private MockMvc mockMvc;
	private User userTestData;
	private final String invalidToken = "invalidToken";
	
	@Mock
	private UserAccountService userAccService;
	
	@InjectMocks
	private SignPageController SPController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(SPController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	@Test
	public void withoutToken() throws Exception {
		mockMvc.perform(get("/sign_up/page"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void invalidToken() throws Exception {
		Cookie cookie = new Cookie(ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, invalidToken);
		mockMvc.perform(get("/sign_up/page")
						.cookie(cookie))
				.andExpect(status().isOk());
	}
	
	@Test
	public void validToken() throws Exception {
		Cookie cookie = new Cookie(ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, userTestData.getToken());
		mockMvc.perform(get("/sign_up/page")
						.cookie(cookie))
				.andExpect(redirectedUrl("/user/page"))
				.andExpect(status().isFound());
	}
	
	private void setTestData() {
		userTestData = RandomData.genUser();
	}
	
	private void setStubs() {
		when(userAccService.checkToken(invalidToken)).thenReturn(false);
		when(userAccService.checkToken(userTestData.getToken())).thenReturn(true);
	}
}
