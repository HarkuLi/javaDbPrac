package com.harku.test.controller.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

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

import com.harku.controller.user.UserRestController;
import com.harku.model.user.UsersModel;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserGetByToken {
	private MockMvc mockMvc;
	private UsersModel userTestData;
	
	@Mock
	private UsersService US;
	
	@Mock
	private UserAccService UAS;
	
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
	public void existingToken() throws Exception {
		Cookie cookie = new Cookie("LOGIN_INFO", userTestData.getToken());
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/get_by_token").cookie(cookie))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertTrue(res.isNull("password"));
		assertTrue(res.isNull("signInTime"));
		assertTrue(res.isNull("token"));
	}
	
	@Test
	public void notExistingToken() throws Exception {
		Cookie cookie = new Cookie("LOGIN_INFO", RandomData.genStr(0, 100));
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/get_by_token").cookie(cookie))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void noCookie() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/get_by_token"))
				.andExpect(status().isNotFound());
	}
	
	private void setTestData() {
		userTestData = RandomData.genUser();
	}
	
	private void setStubs() {
		when(UAS.getAccByToken(userTestData.getToken())).thenReturn(userTestData);
		when(US.getUser(userTestData.getId())).thenReturn(userTestData);
	}
}











