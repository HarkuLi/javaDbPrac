package com.harku.test.controller.occ;

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

import com.harku.controller.occ.OccRestController;
import com.harku.model.occ.OccModel;
import com.harku.service.occ.OccService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccNew {
	private MockMvc mockMvc;
	private OccModel randomOccupation;
	
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
	}
	
	@Test
	public void basic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occ/new")
						.param("id"    , randomOccupation.getId())
						.param("name"  , randomOccupation.getName())
						.param("state" , randomOccupation.getState()?"1":"0"))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void invalidState() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occ/new")
						.param("id"    , randomOccupation.getId())
						.param("name"  , randomOccupation.getName())
						.param("state" , "invalidState"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	private void setTestData() {
		randomOccupation = RandomData.genOcc();
	}
}
