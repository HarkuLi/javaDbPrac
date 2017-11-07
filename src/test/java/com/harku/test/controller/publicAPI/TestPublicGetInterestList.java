package com.harku.test.controller.publicAPI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.json.JSONArray;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.publicAPI.PublicRestController;
import com.harku.model.interest.IntModel;
import com.harku.service.interest.IntService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestPublicGetInterestList {
	private MockMvc mockMvc;
	private ArrayList<IntModel> interestList;
	private int interestNum;
	
	@Mock
	private IntService interestService;
	
	@InjectMocks
	private PublicRestController PRController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(PRController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	@Test
	public void getList() throws Exception {
		MvcResult result = mockMvc.perform(get("/public/get_interest_list"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		assertEquals(interestNum, resList.length());
	}
	
	private void setTestData() {
		interestList = new ArrayList<IntModel>();
		interestNum = (int)(Math.random()*20);
		for(int i=0; i<interestNum; ++i) {
			interestList.add(RandomData.genInterest());
		}
	}
	
	private void setStubs() {
		when(interestService.getList()).thenReturn(interestList);
	}
}
