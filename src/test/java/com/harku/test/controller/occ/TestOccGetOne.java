package com.harku.test.controller.occ;

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

import com.harku.controller.occ.OccRestController;
import com.harku.model.OccModel;
import com.harku.service.OccService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccGetOne {
	private MockMvc mockMvc;
	private OccModel existingOccupation;
	
	@Mock
	private OccService occupationService;
	
	@InjectMocks
	private OccRestController occupationRestController;
	
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
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occ/get_one")
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
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occ/get_one")
						.param("id", notExistingId))
				.andExpect(status().isNotFound());
	}
	
	private void setTestData() {
		existingOccupation = RandomData.genOcc();
	}
	
	private void setStubs() {
		when(occupationService.getOcc(existingOccupation.getId())).thenReturn(existingOccupation);
	}
}
