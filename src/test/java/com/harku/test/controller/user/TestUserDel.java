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

import com.harku.controller.user.UserRestController;
import com.harku.model.UsersModel;
import com.harku.service.UserAccService;
import com.harku.service.UsersService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserDel {
	private MockMvc mockMvc;
	private UsersModel userTestData;
	
	@Mock
	private UsersService usersService;
	
	@Mock
	private UserAccService userAccService;
	
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
	public void existingId() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/del")
											.param("id", userTestData.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void notExistingId() throws Exception {
		String notExistingId = UUID.randomUUID().toString();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/del")
											.param("id", notExistingId))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
				
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(notExistingId, res.get("id"));
	}
	
	private void setTestData() {
		userTestData = RandomData.genUser();
	}
	
	private void setStubs() {
		when(usersService.getUser(userTestData.getId())).thenReturn(userTestData);
	}
}
