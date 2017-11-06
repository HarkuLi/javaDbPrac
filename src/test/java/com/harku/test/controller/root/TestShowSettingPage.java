package com.harku.test.controller.root;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.config.WebConfig;
import com.harku.controller.root.RootPageController;
import com.harku.test.config.ViewResolverTest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
public class TestShowSettingPage {
	private MockMvc mockMvc;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(new RootPageController())
				.setViewResolvers(ViewResolverTest.genResolver())
				.build();
	}
	
	@Test
	public void showPage() throws Exception {
		mockMvc.perform(get("/setting"))
				.andExpect(status().isOk());
	}
}
