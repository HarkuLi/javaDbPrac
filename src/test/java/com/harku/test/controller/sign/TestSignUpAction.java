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
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import com.harku.controller.sign.SignRestController;
import com.harku.model.User;
import com.harku.service.OccupationService;
import com.harku.service.PhotoService;
import com.harku.service.UserAccountService;
import com.harku.service.UserService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestSignUpAction {
	private static byte[] defaultPhoto;
	private MockMvc mockMvc;
	private User randomUser;
	private final String existingAccountName = "existingAccountName";
	
	@Mock
	private UserService usersService;
	
	@Mock
	private UserAccountService userAccountService;
	
	@Mock
	private OccupationService occupationService;
	
	@Spy
	private Map<String, String> statusOption = new AppConfig().statusOption();
	
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
	public void basic() throws Exception {
		MvcResult result 
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isCreated())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
	}
	
	@Test
	public void withPhoto() throws Exception {
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
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isCreated())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		
		//delete the created test photo
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(usersService).createUser(captor.capture());
		User capturedData = captor.getValue();
		PhotoService.delete(capturedData.getPhotoName());
	}
	
	@Test
	public void withInterest() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState())
							.param("interest[]"		, randomUser.getInterest()))
					 .andExpect(status().isCreated())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
	}
	
	@Test
	public void photoWithoutPhotoType() throws Exception {
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
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	//ignored because converting age type in the request parameter from String to Integer
	@Ignore
	@Test
	public void notNumberAge() throws Exception {
		String notNumberAge = RandomData.genStr(3, 5);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, notNumberAge)
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void negativeAge() throws Exception {
		int negativeAge = (int)(Math.random()*20 - 21);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, Integer.toString(negativeAge))
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void tooLongAccount() throws Exception {
		int invalidLength = ConstantConfig.MAX_ACCOUNT_LENGTH + 1;
		String tooLongAccount = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, tooLongAccount)
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(tooLongAccount, res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void existingAccountName() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, existingAccountName)
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(existingAccountName, res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void tooLongPassword() throws Exception {
		int invalidLength = ConstantConfig.MAX_PASSWORD_LENGTH + 1;
		String tooLongPassword = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, tooLongPassword)
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void wrongPasswordCheck() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, "wrongPasswordCheck")
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void tooLongName() throws Exception {
		int invalidLength = ConstantConfig.MAX_NAME_LENGTH + 1;
		String tooLongName = RandomData.genStr(invalidLength, invalidLength);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, tooLongName)
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Ignore
	@Test
	public void invalidBirth() throws Exception {
		String invalidBirth = RandomData.genStr(11, 20);
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, invalidBirth)
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void notExistingOccupation() throws Exception {
		String notExistingOccupation = UUID.randomUUID().toString();
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, notExistingOccupation)
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isBadRequest())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
		assertNotNull(res.get("errMsg"));
	}
	
	@Test
	public void otherOccupation() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, "other")
							.param("state"			, randomUser.getState()))
					 .andExpect(status().isCreated())
					 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
					 .andReturn();
		
		JSONObject res = new JSONObject(result.getResponse().getContentAsString());
		assertEquals(randomUser.getAccount(), res.get("account"));
	}
	
	@Test
	public void invalidState() throws Exception {
		MvcResult result
			= mockMvc.perform(MockMvcRequestBuilders.fileUpload("/sign_up/action")
							.param("account"		, randomUser.getAccount())
							.param("password"		, randomUser.getPassword())
							.param("passwordCheck"	, randomUser.getPassword())
							.param("name"			, randomUser.getName())
							.param("age"			, randomUser.getAge().toString())
							.param("birth"			, randomUser.getBirth())
							.param("occupation"		, randomUser.getOccupation())
							.param("state"			, "invalid state"))
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
		when(occupationService.getOcc(randomUser.getOccupation())).thenReturn(RandomData.genOcc());
	}
}



