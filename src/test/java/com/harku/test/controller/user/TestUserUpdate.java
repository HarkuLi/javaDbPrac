package com.harku.test.controller.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import com.harku.model.user.UsersModel;
import com.harku.service.photo.PhotoService;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserUpdate {
	private static byte[] defaultPhoto;
	private MockMvc mockMvc;
	private UsersModel userTestData;
	
	@Mock
	private UsersService usersService;
	
	@Mock
	private UserAccService userAccService;
	
	@Autowired
	@InjectMocks
	private UserRestController URController;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		//read the photo in the project
		String projectPathStr = System.getProperty("user.dir");
		Path imagePath = Paths.get(projectPathStr, "src", "test", "resources", "image", "test.png");
		defaultPhoto = Files.readAllBytes(imagePath);
	}
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(URController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	/**
	 * update user without photo and interest
	 * @throws Exception 
	 */
	@Test
	public void updateUserBasic() throws Exception {
		MvcResult result 
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()?"1":"0"))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void updateUserWithPhoto() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.file("photo", defaultPhoto)
							.param("photoType", "png")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()?"1":"0"))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		
		//delete the created test photo
		ArgumentCaptor<UsersModel> captor = ArgumentCaptor.forClass(UsersModel.class);
		verify(usersService).update(captor.capture());
		UsersModel capturedData = captor.getValue();
		PhotoService.delete(capturedData.getPhotoName());
	}
	
	@Test
	public void updateUserWithInterest() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()?"1":"0")
							.param("interest[]"	, userTestData.getInterest()))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void updateUserPhotoWithoutPhotoType() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.file("photo", defaultPhoto)		
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()?"1":"0"))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void updateNotExistingUser() throws Exception {
		UsersModel notExistingUser = RandomData.genUser();
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, notExistingUser.getId())
							.param("name"		, notExistingUser.getName())
							.param("age"		, notExistingUser.getAge().toString())
							.param("birth"		, notExistingUser.getBirth())
							.param("occupation"	, notExistingUser.getOccupation())
							.param("state"		, notExistingUser.getState()?"1":"0"))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(notExistingUser.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void updateUserWithInvalidAge() throws Exception {
		String invalidAge = RandomData.genStr(3, 5);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, invalidAge)
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()?"1":"0"))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	private void setTestData() {
		userTestData = RandomData.genUser();
	}
	
	private void setStubs() {
		when(usersService.getUser(userTestData.getId())).thenReturn(userTestData);
	}
}










