package com.harku.test.controller.sign;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.config.ConstantConfig;
import com.harku.config.WebConfig;
import com.harku.controller.sign.SignPageController;
import com.harku.service.UserAccountService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class TestSignOut {
	private MockMvc mockMvc;
	
	@Mock
	private UserAccountService UAS;
	
	@InjectMocks
	private SignPageController SPController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(SPController)
				.build();
	}
	
	@Test
	public void signOut() throws Exception {
		Cookie cookie = new Cookie(ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, RandomData.genStr(100, 100));
		mockMvc.perform(get("/sign_out")
						.cookie(cookie))
				.andExpect(redirectedUrl("/sign_in/page"))
				.andExpect(status().isFound())
				.andExpect(MockMvcResultMatchers.cookie().value(ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, ""))
				.andExpect(MockMvcResultMatchers.cookie().maxAge(ConstantConfig.LOGIN_TOKEN_COOKIE_NAME, 0));
	}
}
