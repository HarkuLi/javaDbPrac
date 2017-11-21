package com.harku.test.controller.root;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.root.RootPageController;

public class TestShowSettingPage {
	private MockMvc mockMvc;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(new RootPageController())
				.build();
	}
	
	@Test
	public void showPage() throws Exception {
		mockMvc.perform(get("/setting"))
				.andExpect(status().isOk());
	}
}
