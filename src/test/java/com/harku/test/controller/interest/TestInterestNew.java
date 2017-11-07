package com.harku.test.controller.interest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.interest.IntRestController;
import com.harku.model.interest.IntModel;
import com.harku.service.interest.IntService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestInterestNew {
	private MockMvc mockMvc;
	private IntModel randomInterest;
	
	@Mock
	private IntService interestService;
	
	@InjectMocks
	private IntRestController interestRestController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(interestRestController)
				.build();
		
		setTestData();
	}
	
	@Test
	public void basic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/new")
						.param("id"    , randomInterest.getId())
						.param("name"  , randomInterest.getName())
						.param("state" , randomInterest.getState()?"1":"0"))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void invalidState() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/new")
						.param("id"    , randomInterest.getId())
						.param("name"  , randomInterest.getName())
						.param("state" , "invalidState"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	private void setTestData() {
		randomInterest = RandomData.genInterest();
	}
}
