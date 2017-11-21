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
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.config.AppConfig;
import com.harku.config.ConstantConfig;
import com.harku.controller.user.UserRestController;
import com.harku.model.User;
import com.harku.service.OccupationService;
import com.harku.service.PhotoService;
import com.harku.service.UserAccountService;
import com.harku.service.UserService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserUpdate {
	private static byte[] defaultPhoto;
	private MockMvc mockMvc;
	private User userTestData;
	
	@Mock
	private UserService usersService;
	
	@Mock
	private UserAccountService userAccService;
	
	@Mock
	private OccupationService occupationService;
	
	@Spy
	private Map<String, String> statusOption = new AppConfig().statusOption();
	
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
	public void basic() throws Exception {
		MvcResult result 
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void withPhoto() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.file("photo", defaultPhoto)
							.param("photoType", "png")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		
		//delete the created test photo
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(usersService).update(captor.capture());
		User capturedData = captor.getValue();
		PhotoService.delete(capturedData.getPhotoName());
	}
	
	@Test
	public void withInterest() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState())
							.param("interest[]"	, userTestData.getInterest()))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void photoWithoutPhotoType() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.file("photo", defaultPhoto)		
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void notExistingUser() throws Exception {
		User notExistingUser = RandomData.genUser();
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, notExistingUser.getId())
							.param("name"		, notExistingUser.getName())
							.param("age"		, notExistingUser.getAge().toString())
							.param("birth"		, notExistingUser.getBirth())
							.param("occupation"	, notExistingUser.getOccupation())
							.param("state"		, notExistingUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(notExistingUser.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void notNumberAge() throws Exception {
		String notNumberAge = RandomData.genStr(3, 5);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, notNumberAge)
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void negativeAge() throws Exception {
		int negativeAge = (int)(Math.random()*20 - 21);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, Integer.toString(negativeAge))
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void invalidBirth() throws Exception {
		String invalidBirth = RandomData.genStr(11, 20);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, invalidBirth)
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void notExistingOccupation() throws Exception {
		String notExistingOccupation = UUID.randomUUID().toString();
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, notExistingOccupation)
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void otherOccupation() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, "other")
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isOk())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
	}
	
	@Test
	public void tooLongName() throws Exception {
		int invalidLength = ConstantConfig.MAX_NAME_LENGTH + 1;
		String tooLongName = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, tooLongName)
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, userTestData.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(userTestData.getId(), res.get("id"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void invalidState() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/user/update")
							.param("id"			, userTestData.getId())
							.param("name"		, userTestData.getName())
							.param("age"		, userTestData.getAge().toString())
							.param("birth"		, userTestData.getBirth())
							.param("occupation"	, userTestData.getOccupation())
							.param("state"		, "invalid state"))
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
		when(occupationService.getOcc(userTestData.getOccupation())).thenReturn(RandomData.genOcc());
	}
}










