package com.harku.test.controller.occupation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

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

import com.harku.config.ConstantConfig;
import com.harku.controller.occupation.OccupationRestController;
import com.harku.model.Occupation;
import com.harku.service.OccupationService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccupationUpdate {
	private MockMvc mockMvc;
	private Occupation existingOccupation;
	
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
	public void basic() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/update")
							.param("id"    , existingOccupation.getId())
							.param("name"  , existingOccupation.getName())
							.param("state" , existingOccupation.getState()?"1":"0"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingOccupation.getId(), res.get("id"));
	}
	
	@Test
	public void invalidId() throws Exception {
		String invalidId = "invalidId";
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/update")
							.param("id"    , invalidId)
							.param("name"  , existingOccupation.getName())
							.param("state" , existingOccupation.getState()?"1":"0"))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(invalidId, res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void tooLongName() throws Exception {
		int invalidLength = ConstantConfig.MAX_NAME_LENGTH + 1;
		String tooLongName = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/update")
							.param("id"    , existingOccupation.getId())
							.param("name"  , tooLongName)
							.param("state" , existingOccupation.getState()?"1":"0"))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingOccupation.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void existingName() throws Exception {
		String existingName = "existingName";
		
		//set Stubs
		ArrayList<Occupation> list = new ArrayList<Occupation>();
		Occupation occupation = RandomData.genOcc();
		occupation.setName(existingName);
		list.add(occupation);
		when(occupationService.getPage(eq(1), argThat(filter -> filter.getName().equals(existingName))))
			.thenReturn(list);
		
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/update")
							.param("id"    , existingOccupation.getId())
							.param("name"  , existingName)
							.param("state" , existingOccupation.getState()?"1":"0"))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingOccupation.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	private void setTestData() {
		existingOccupation = RandomData.genOcc();
	}
	
	private void setStubs() {
		when(occupationService.getOcc(existingOccupation.getId())).thenReturn(existingOccupation);
	}
}
