package com.harku.test.controller.user;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.harku.controller.user.UserRestController;
import com.harku.service.photo.PhotoService;
import com.harku.service.user.UserAccService;
import com.harku.service.user.UsersService;
import com.harku.test.util.RandomData;

@RunWith(MockitoJUnitRunner.class)
public class TestUserPhoto {
	private MockMvc mockMvc;
	
	@Mock
	private UsersService US;
	
	@Mock
	private UserAccService UAS;
	
	@Autowired
	@InjectMocks
	private UserRestController URController;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		//check the test photo exists
		byte[] photo = PhotoService.read(PhotoService.DEFAULT_PHOTO_NAME);
		if(photo == null) {
			//read the photo in the project
			String projectPathStr = System.getProperty("user.dir");
			Path imagePath = Paths.get(projectPathStr, "src", "test", "resources", "image", "test.png");
			byte[] defaultPhoto = Files.readAllBytes(imagePath);
			
			//store in the destination folder
			Path desPath = Paths.get(PhotoService.STORE_PATH, PhotoService.DEFAULT_PHOTO_NAME);
			FileOutputStream FOStream = new FileOutputStream(desPath.toString());
			FOStream.write(defaultPhoto);
			FOStream.close();
		}
	}
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(URController)
				.build();
	}
	
	@Test
	public void getPhotoByDefault() throws Exception {
		MvcResult result = mockMvc.perform(get("/user/photo"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG))
				.andReturn();
		byte[] resContent = result.getResponse().getContentAsByteArray();
		
		byte[] expectedContent = PhotoService.read(PhotoService.DEFAULT_PHOTO_NAME);
		assertTrue(Arrays.equals(expectedContent, resContent));
	}
	
	@Test 
	public void getDefaultPhotoByParameter() throws Exception {
		MvcResult result = mockMvc.perform(get("/user/photo?n=" + PhotoService.DEFAULT_PHOTO_NAME))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG))
				.andReturn();
		byte[] resContent = result.getResponse().getContentAsByteArray();
		
		byte[] expectedContent = PhotoService.read(PhotoService.DEFAULT_PHOTO_NAME);
		assertTrue(Arrays.equals(expectedContent, resContent));
	}
	
	
	@Test
	public void getNotExistingPhoto() throws Exception {
		String notExistingPhotoName = UUID.randomUUID().toString() + "." + RandomData.genImageType();
		mockMvc.perform(get("/user/photo?n=" + notExistingPhotoName))
				.andExpect(status().isNotFound());
	};
}
