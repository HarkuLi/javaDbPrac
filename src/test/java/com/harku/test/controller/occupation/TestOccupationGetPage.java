package com.harku.test.controller.occupation;

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

import com.harku.controller.occupation.OccupationRestController;
import com.harku.model.Occupation;
import com.harku.service.OccupationService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestOccupationGetPage {
	private MockMvc mockMvc;
	private final int totalPage = 2;
	private ArrayList<Occupation> page1List;
	private ArrayList<Occupation> page2List;
	private Occupation filterMatchNothing;
	
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
	public void getPageOne() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/get_page")
							.param("page", "1"))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		JSONObject resOccupation = (JSONObject)resList.get(0);
		assertEquals(page1List.get(0).getId(), resOccupation.get("id"));
		assertEquals(totalPage, res.get("totalPage"));
	}
	
	@Test
	public void getPageZero() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/get_page")
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
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/get_page")
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
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/get_page")
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
		when(occupationService.getTotalPage(argThat(filter -> filter.getName().equals(filterMatchNothing.getName())
													&& filter.getState() == filterMatchNothing.getState())))
			.thenReturn(0);
		
		when(occupationService.getPage(anyInt(), argThat(filter -> filter.getName().equals(filterMatchNothing.getName())
													&& filter.getState() == filterMatchNothing.getState())))
			.thenReturn(new ArrayList<Occupation>());
		
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/occupation/get_page")
							.param("page", "1")
							.param("name", filterMatchNothing.getName())
							.param("state", filterMatchNothing.getState()?"1":"0"))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();

		//expect an empty list
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		assertTrue(resList.length() == 0);
	}
	
	private void setTestData() {
		page1List = new ArrayList<Occupation>();
		page1List.add(RandomData.genOcc());
		page2List = new ArrayList<Occupation>();
		page2List.add(RandomData.genOcc());
		filterMatchNothing = RandomData.genOcc();
	}
	
	private void setStubs() {
		when(occupationService.getTotalPage(any(Occupation.class))).thenReturn(totalPage);
		when(occupationService.getPage(eq(1), any(Occupation.class))).thenReturn(page1List);
		when(occupationService.getPage(eq(2), any(Occupation.class))).thenReturn(page2List);
	}
}
