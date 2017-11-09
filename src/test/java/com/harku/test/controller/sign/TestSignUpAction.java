package com.harku.test.controller.sign;

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

import com.harku.controller.sign.SignRestController;
import com.harku.model.user.UsersModel;
import com.harku.service.photo.PhotoService;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestSignUpAction {
	private static byte[] defaultPhoto;
	private MockMvc mockMvc;
	private UsersModel randomUser;
	private final String existingAccountName = "existingAccountName";
	
	@Mock
	private UsersService usersService;
	
	@Mock
	private UserAccService userAccountService;
	
	@Autowired
	@InjectMocks
	private SignRestController signRestController;
	
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
				.standaloneSetup(signRestController)
				.build();
		
		setTestData();
		setStubs();
	}
	
	/**
	 * create user without photo and interest
	 * @throws Exception 
	 */
	@Test
	public void createUserBasic() throws Exception {
		MvcResult result 
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()?"1":"0"))
					 .andExpect(status().isCreated())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
	}
	
	@Test
	public void createUserWithPhoto() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.file("photo", defaultPhoto)
							.param("photoType", "png")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()?"1":"0"))
					 .andExpect(status().isCreated())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		
		//delete the created test photo
		ArgumentCaptor<UsersModel> captor = ArgumentCaptor.forClass(UsersModel.class);
		verify(usersService).createUser(captor.capture());
		UsersModel capturedData = captor.getValue();
		PhotoService.delete(capturedData.getPhotoName());
	}
	
	@Test
	public void createUserWithInterest() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()?"1":"0")
							.param("interest[]"		, randomUser.getInterest()))
					 .andExpect(status().isCreated())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
	}
	
	@Test
	public void createUserPhotoWithoutPhotoType() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.file("photo", defaultPhoto)		
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()?"1":"0"))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void createUserWithInvalidAge() throws Exception {
		String invalidAge = RandomData.genStr(3, 5);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, invalidAge)
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()?"1":"0"))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void createUserWithExistingAccountName() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, existingAccountName)
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()?"1":"0"))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingAccountName, res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void createUserWithWrongPasswordCheck() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, "wrongPasswordCheck")
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()?"1":"0"))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	private void setTestData() {
		randomUser = RandomData.genUser();
	}
	
	private void setStubs() {
		when(userAccountService.isAccExist(randomUser.getAccount())).thenReturn(false);
		when(userAccountService.isAccExist(existingAccountName)).thenReturn(true);
	}
}
