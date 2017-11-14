package com.harku.test.controller.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.user.UserRestController;
import com.harku.model.UserFilterModel;
import com.harku.model.UsersModel;
import com.harku.service.UserAccService;
import com.harku.service.UsersService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserGetPage {
	private final int totalPage = 2;
	private ArrayList<UsersModel> page1List;
	private ArrayList<UsersModel> page2List;
	private MockMvc mockMvc;
	
	@Mock
	private UsersService usersService;
	
	@Mock
	private UserAccService userAccService;
	
	@Autowired
	@InjectMocks
	private UserRestController URController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(URController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	@Test
	public void getPageOne() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/get_page")
							.param("page", "1"))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		JSONArray resList = (JSONArray) res.get("list");
		JSONObject resUser = (JSONObject)resList.get(0);
		assertEquals(page1List.get(0).getId(), resUser.get("id"));
		assertEquals(totalPage, res.get("totalPage"));
	}
	
	@Test
	public void getPageZero() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/get_page")
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
	public void GetNegativePage() throws Exception {
		int page = -(int)(Math.random()*10 + 1);
		
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/get_page")
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
	public void GetPageLargerThanTotalPage() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/get_page")
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
	
	private void setTestData() {
		page1List = new ArrayList<UsersModel>();
		page1List.add(RandomData.genUser());
		page2List = new ArrayList<UsersModel>();
		page2List.add(RandomData.genUser());
	}
	
	private void setStubs() {
		when(usersService.getTotalPage(any(UserFilterModel.class))).thenReturn(totalPage);
		when(usersService.getPage(eq(1), any(UserFilterModel.class))).thenReturn(page1List);
		when(usersService.getPage(eq(2), any(UserFilterModel.class))).thenReturn(page2List);
	}
}








