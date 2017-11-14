package com.harku.test.controller.occupation;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.occupation.OccupationRestController;
import com.harku.model.OccupationModel;
import com.harku.service.OccupationService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccupationDel {
	private MockMvc mockMvc;
	private OccupationModel existingOccupation;
	
	@Mock
	private OccupationService occupationService;
	
	@InjectMocks
	private OccupationRestController occupationRestController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(occupationRestController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	@Test
	public void existingId() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/del")
											.param("id", existingOccupation.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingOccupation.getId(), res.get("id"));
	}
	
	@Test
	public void notExistingId() throws Exception {
		String notExistingId = UUID.randomUUID().toString();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/del")
											.param("id", notExistingId))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
				
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(notExistingId, res.get("id"));
	}
	
	private void setTestData() {
		existingOccupation = RandomData.genOcc();
	}
	
	private void setStubs() {
		when(occupationService.getOcc(existingOccupation.getId()))
			.thenReturn(existingOccupation);
	}
}
