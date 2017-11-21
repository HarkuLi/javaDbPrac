package com.harku.test.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.user.UserPageController;

public class TestUserNewForm {
	private MockMvc mockMvc;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(new UserPageController())
				.build();
	}
	
	@Test
	public void showPage() throws Exception {
		mockMvc.perform(get("/user/new_form"))
				.andExpect(status().isOk());
	}
}
