package com.harku.test.controller.interest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.interest.IntRestController;
import com.harku.model.interest.IntModel;
import com.harku.service.interest.IntService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestInterestUpdate {
	private MockMvc mockMvc;
	private IntModel existingInterest;
	
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
		setStubs();
	}
	
	@Test
	public void basic() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/update")
							.param("id"    , existingInterest.getId())
							.param("name"  , existingInterest.getName())
							.param("state" , existingInterest.getState()?"1":"0"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingInterest.getId(), res.get("id"));
	}
	
	@Test
	public void invalidId() throws Exception {
		String invalidId = "invalidId";
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/update")
							.param("id"    , invalidId)
							.param("name"  , existingInterest.getName())
							.param("state" , existingInterest.getState()?"1":"0"))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(invalidId, res.get("id"));
	}
	
	@Test
	public void invalidState() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/update")
							.param("id"    , existingInterest.getId())
							.param("name"  , existingInterest.getName())
							.param("state" , "invalidState"))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingInterest.getId(), res.get("id"));
	}
	
	private void setTestData() {
		existingInterest = RandomData.genInterest();
	}
	
	private void setStubs() {
		when(interestService.getInterest(existingInterest.getId())).thenReturn(existingInterest);
	}
}