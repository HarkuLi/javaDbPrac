package com.harku.test.controller.sign;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.config.WebConfig;
import com.harku.controller.sign.SignPageController;
import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;
import com.harku.test.util.RandomData;
import com.harku.validator.user.AccountValidator;

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class TestSignInPage {
	private MockMvc mockMvc;
	private UsersModel userTestData;
	private UsersModel disabledUser;
	private UsersModel enabledUser;
	private String disabledUserPassword;
	private String enabledUserPassword;
	private final String invalidToken = "invalidToken";
	private final String notExistingAccount = "notExistingAccount";
	
	@Mock
	private UserAccService UAS;
	
	@Spy
	private AccountValidator accountValidator = new AccountValidator();
	
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
	
	/////////////////
	// get methods //
	/////////////////
	
	@Test
	public void get_withoutToken() throws Exception {
		mockMvc.perform(get("/sign_in/page"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void get_invalidToken() throws Exception {
		Cookie cookie = new Cookie("LOGIN_INFO", invalidToken);
		mockMvc.perform(get("/sign_in/page")
						.cookie(cookie))
				.andExpect(status().isOk());
	}
	
	@Test
	public void get_validToken() throws Exception {
		Cookie cookie = new Cookie("LOGIN_INFO", userTestData.getToken());
		mockMvc.perform(get("/sign_in/page")
						.cookie(cookie))
				.andExpect(redirectedUrl("/user/page"))
				.andExpect(status().isFound());
	}
	
	//////////////////
	// post methods //
	//////////////////
	
	@Test
	public void post_noAccountAndPassword() throws Exception {
		mockMvc.perform(post("/sign_in/page"))
				.andExpect(forwardedUrl("sign_in"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void post_signInSuccess() throws Exception {
		mockMvc.perform(post("/sign_in/page")
						.param("account", enabledUser.getAccount())
						.param("password", enabledUserPassword))
				.andExpect(redirectedUrl("/user/page"))
				.andExpect(status().isFound());
	}
	
	@Test
	public void post_notExistingAccount() throws Exception {
		mockMvc.perform(post("/sign_in/page")
						.param("account", notExistingAccount)
						.param("password", "password"))
				.andExpect(forwardedUrl("sign_in"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void post_disabledAccount() throws Exception {
		mockMvc.perform(post("/sign_in/page")
						.param("account", disabledUser.getAccount())
						.param("password", disabledUserPassword))
				.andExpect(forwardedUrl("sign_in"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void post_wrongPassword() throws Exception {
		mockMvc.perform(post("/sign_in/page")
						.param("account", enabledUser.getAccount())
						.param("password", "wrongPassword"))
				.andExpect(forwardedUrl("sign_in"))
				.andExpect(status().isOk());
	}
	
	private void setTestData() {
		userTestData = RandomData.genUser();
		
		disabledUser = RandomData.genUser();
		disabledUser.setState(false);
		//reset the password, because RandomData.genUser() generate a hashed password
		//and we can't know the plain text
		disabledUserPassword = RandomData.genStr(20, 20);
		disabledUser.setPassword(BCrypt.hashpw(disabledUserPassword, BCrypt.gensalt(RandomData.workload)));
		
		enabledUser = RandomData.genUser();
		enabledUser.setState(true);
		enabledUserPassword = RandomData.genStr(20, 20);
		enabledUser.setPassword(BCrypt.hashpw(enabledUserPassword, BCrypt.gensalt(RandomData.workload)));
	}
	
	private void setStubs() {
		when(UAS.checkToken(userTestData.getToken())).thenReturn(true);
		when(UAS.checkToken(invalidToken)).thenReturn(false);
		when(UAS.getAcc(notExistingAccount)).thenReturn(null);
		when(UAS.getAcc(disabledUser.getAccount())).thenReturn(disabledUser);
		when(UAS.getAcc(enabledUser.getAccount())).thenReturn(enabledUser);
	}
}
