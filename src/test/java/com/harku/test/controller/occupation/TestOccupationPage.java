package com.harku.test.controller.occupation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.occupation.OccupationPageController;

public class TestOccupationPage {
	private MockMvc mockMvc;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(new OccupationPageController())
				.build();
	}
	
	@Test
	public void showPage() throws Exception {
		mockMvc.perform(get("/occupation/page"))
				.andExpect(status().isOk());
	}
}
