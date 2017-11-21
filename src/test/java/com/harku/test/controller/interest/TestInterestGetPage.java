package com.harku.test.controller.interest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.interest.InterestRestController;
import com.harku.model.Interest;
import com.harku.service.InterestService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestInterestGetPage {
	private MockMvc mockMvc;
	private final int totalPage = 2;
	private ArrayList<Interest> page1List;
	private ArrayList<Interest> page2List;
	private Interest filterMatchNothing;
	
	@Mock
	private InterestService interestService;
	
	@InjectMocks
	private InterestRestController intRestController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(intRestController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	@Test
	public void getPageOne() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/get_page")
							.param("page", "1"))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		JSONObject resInterest = (JSONObject)resList.get(0);
		assertEquals(page1List.get(0).getId(), resInterest.get("id"));
		assertEquals(totalPage, res.get("totalPage"));
	}
	
	@Test
	public void getPageZero() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/get_page")
							.param("page", "0"))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
	
		//expect page 1
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		JSONObject resUser = (JSONObject)resList.get(0);
		assertEquals(page1List.get(0).getId(), resUser.get("id"));
		assertEquals(totalPage, res.get("totalPage"));
	}
	
	@Test
	public void getNegativePage() throws Exception {
		int page = -(int)(Math.random()*10 + 1);
		
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/get_page")
							.param("page", Integer.toString(page)))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
	
		//expect page 1
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		JSONObject resUser = (JSONObject)resList.get(0);
		assertEquals(page1List.get(0).getId(), resUser.get("id"));
		assertEquals(totalPage, res.get("totalPage"));
	}
	
	@Test
	public void getPageLargerThanTotalPage() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/get_page")
							.param("page", Integer.toString(totalPage+5)))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
	
		//expect page 2(equal to totalPage)
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		JSONObject resUser = (JSONObject)resList.get(0);
		assertEquals(page2List.get(0).getId(), resUser.get("id"));
		assertEquals(totalPage, res.get("totalPage"));
	}
	
	@Test
	public void noOneMatchFilter() throws Exception {
		when(interestService.getTotalPage(argThat(filter -> filter.getName().equals(filterMatchNothing.getName())
													&& filter.getState() == filterMatchNothing.getState())))
			.thenReturn(0);
		
		when(interestService.getPage(anyInt(), argThat(filter -> filter.getName().equals(filterMatchNothing.getName())
													&& filter.getState() == filterMatchNothing.getState())))
			.thenReturn(new ArrayList<Interest>());
		
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/interest/get_page")
							.param("page", "1")
							.param("name", filterMatchNothing.getName())
							.param("state", filterMatchNothing.getState()))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();

		//expect an empty list
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		assertTrue(resList.length() == 0);
	}
	
	private void setTestData() {
		page1List = new ArrayList<Interest>();
		page1List.add(RandomData.genInterest());
		page2List = new ArrayList<Interest>();
		page2List.add(RandomData.genInterest());
		filterMatchNothing = RandomData.genInterest();
	}
	
	private void setStubs() {
		when(interestService.getTotalPage(any(Interest.class))).thenReturn(totalPage);
		when(interestService.getPage(eq(1), any(Interest.class))).thenReturn(page1List);
		when(interestService.getPage(eq(2), any(Interest.class))).thenReturn(page2List);
	}
}
