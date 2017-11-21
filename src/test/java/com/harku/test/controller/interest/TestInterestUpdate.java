package com.harku.test.controller.interest;

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
import com.harku.controller.interest.InterestRestController;
import com.harku.model.Interest;
import com.harku.service.InterestService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestInterestUpdate {
	private MockMvc mockMvc;
	private Interest existingInterest;
	
	@Mock
	private InterestService interestService;
	
	@InjectMocks
	private InterestRestController interestRestController;
	
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
							.param("state" , existingInterest.getState()))
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
							.param("state" , existingInterest.getState()))
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
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/update")
							.param("id"    , existingInterest.getId())
							.param("name"  , tooLongName)
							.param("state" , existingInterest.getState()))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingInterest.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void existingName() throws Exception {
		String existingName = "existingName";
		
		//set Stubs
		ArrayList<Interest> list = new ArrayList<Interest>();
		Interest interest = RandomData.genInterest();
		interest.setName(existingName);
		list.add(interest);
		when(interestService.getPage(eq(1), argThat(filter -> filter.getName().equals(existingName))))
			.thenReturn(list);
		
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/update")
							.param("id"    , existingInterest.getId())
							.param("name"  , existingName)
							.param("state" , existingInterest.getState()))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingInterest.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	private void setTestData() {
		existingInterest = RandomData.genInterest();
	}
	
	private void setStubs() {
		when(interestService.getInterest(existingInterest.getId())).thenReturn(existingInterest);
	}
}
