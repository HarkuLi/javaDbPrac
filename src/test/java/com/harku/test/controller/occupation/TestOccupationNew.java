package com.harku.test.controller.occupation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.config.AppConfig;
import com.harku.config.ConstantConfig;
import com.harku.controller.occupation.OccupationRestController;
import com.harku.model.Occupation;
import com.harku.service.OccupationService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccupationNew {
	private MockMvc mockMvc;
	private Occupation randomOccupation;
	
	@Mock
	private OccupationService occupationService;
	
	@Spy
	private Map<String, String> statusOption = new AppConfig().statusOption();
	
	@InjectMocks
	private OccupationRestController occupationRestController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(occupationRestController)
				.build();
		
		setTestData();
	}
	
	@Test
	public void basic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/new")
						.param("name"  , randomOccupation.getName())
						.param("state" , randomOccupation.getState()))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void tooLongName() throws Exception {
		int invalidLength = ConstantConfig.MAX_NAME_LENGTH + 1;
		String tooLongName = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/new")
							.param("name"  , tooLongName)
							.param("state" , randomOccupation.getState()))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void existingName() throws Exception {
		String existingName = "existingName";
		when(occupationService.getTotalPage(argThat(filter -> filter.getName().equals(existingName))))
			.thenReturn(1);
		
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/new")
							.param("name"  , existingName)
							.param("state" , randomOccupation.getState()))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void invalidState() throws Exception {
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/new")
						.param("name"  , randomOccupation.getName())
						.param("state" , "invalid state"))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertNotNull(res.get("errMsg"));
	}
	
	private void setTestData() {
		randomOccupation = RandomData.genOcc();
	}
}
