package com.harku.test.controller.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.config.ConstantConfig;
import com.harku.controller.user.UserRestController;
import com.harku.model.User;
import com.harku.service.UserAccountService;
import com.harku.service.UserService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserChangePassword {
	private MockMvc mockMvc;
	private User userTestData;
	private User userWithoutAccount;
	
	@Mock
	private UserService usersService;
	
	@Mock
	private UserAccountService userAccService;
	
	@Autowired
	@InjectMocks
	private UserRestController URController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(URController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	@Test
	public void changeSuccessfully() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/change_password")
											.param("id", userTestData.getId())
											.param("account", userTestData.getAccount())
											.param("password", "newPassword")
											.param("passwordCheck", "newPassword"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void notExistingId() throws Exception {
		String notExistingId = UUID.randomUUID().toString();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/change_password")
											.param("id", notExistingId)
											.param("account", "account")
											.param("password", "newPassword")
											.param("passwordCheck", "newPassword"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
				
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(notExistingId, res.get("id"));
	}
	
	@Test
	public void withoutAccountName() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/change_password")
											.param("id", userWithoutAccount.getId())
											.param("password", "newPassword")
											.param("passwordCheck", "newPassword"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void createAccountWithExistingAccountName() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/change_password")
											.param("id", userWithoutAccount.getId())
											.param("account", userTestData.getAccount())
											.param("password", "newPassword")
											.param("passwordCheck", "newPassword"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userWithoutAccount.getId(), res.get("id"));
	}
	
	@Test
	public void checkPasswordNotMatched() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/change_password")
											.param("id", userTestData.getId())
											.param("account", userTestData.getAccount())
											.param("password", "newPassword")
											.param("passwordCheck", "wrongPasswordCheck"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void tooLongAccount() throws Exception {
		int invalidLength = ConstantConfig.MAX_ACCOUNT_LENGTH + 1;
		String tooLongAccount = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/change_password")
											.param("id", userTestData.getId())
											.param("account", tooLongAccount)
											.param("password", "newPassword")
											.param("passwordCheck", "newPassword"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
				
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void tooLongPassword() throws Exception {
		int invalidLength = ConstantConfig.MAX_PASSWORD_LENGTH + 1;
		String tooLongPassword = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/change_password")
											.param("id", userTestData.getId())
											.param("account", userTestData.getAccount())
											.param("password", tooLongPassword)
											.param("passwordCheck", tooLongPassword))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
				
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	private void setTestData() {
		userTestData = RandomData.genUser();
		
		userWithoutAccount = RandomData.genUser();
		userWithoutAccount.setAccount(null);
		userWithoutAccount.setPassword(null);
	}
	
	private void setStubs() {
		when(userAccService.getAccById(userTestData.getId())).thenReturn(userTestData);
		when(userAccService.isAccExist(userTestData.getAccount())).thenReturn(true);
		
		when(userAccService.getAccById(userWithoutAccount.getId())).thenReturn(userWithoutAccount);
	}
}
