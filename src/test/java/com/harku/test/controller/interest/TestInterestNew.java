package com.harku.test.controller.interest;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
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

import com.harku.config.ConstantConfig;
import com.harku.controller.interest.InterestRestController;
import com.harku.model.Interest;
import com.harku.service.InterestService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestInterestNew {
	private MockMvc mockMvc;
	private Interest randomInterest;
	
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
	}
	
	@Test
	public void basic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/new")
						.param("name"  , randomInterest.getName())
						.param("state" , randomInterest.getState()))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void tooLongName() throws Exception {
		int invalidLength = ConstantConfig.MAX_NAME_LENGTH + 1;
		String tooLongName = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/new")
							.param("name"  , tooLongName)
							.param("state" , randomInterest.getState()))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void existingName() throws Exception {
		String existingName = "existingName";
		when(interestService.getTotalPage(argThat(filter -> filter.getName().equals(existingName))))
			.thenReturn(1);
		
		MvcResult result =
			mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/new")
							.param("name"  , existingName)
							.param("state" , randomInterest.getState()))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertNotNull(res.get("errMsg"));
	}
	
	private void setTestData() {
		randomInterest = RandomData.genInterest();
	}
}
