package com.harku.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.harku.service.PhotoService;
import com.harku.test.util.RandomData;

public class TestPhotoService {
	private static String imageDirPathStr;
	
	@BeforeClass
	public static void beforeClass() {
		String projectPathStr = System.getProperty("user.dir");
		imageDirPathStr = Paths.get(projectPathStr, "src", "test", "resources", "image").toString();
	}
	
	@Test
	public void testJPG() throws IOException, InterruptedException {
		testServiceByImageType("jpeg");
	}
	
	@Test
	public void testGIF() throws IOException, InterruptedException {
		testServiceByImageType("gif");
	}
	
	@Test
	public void testPNG() throws IOException, InterruptedException {
		testServiceByImageType("png");
	}
	
	@Test
	public void testUnacceptableType() throws IOException {
		String type = RandomData.genStr(3, 5);
		while(PhotoService.acceptType.contains(type)) type = RandomData.genStr(3, 5);
		
		String imageName = UUID.randomUUID().toString() + "." + type;
		String originalName = imageName;
		String contentType = "image/" + type;
		MultipartFile photoMPFile = new MockMultipartFile(imageName, originalName, contentType, (byte[])null);
		
		String storedName = PhotoService.write(photoMPFile, type);
		//the returned name of a file with unacceptable type should be null
		assertNull(storedName);
		
		byte[] readContent = PhotoService.read(storedName);
		//the read content should be null
		assertNull(readContent);
	}
	
	@Test
	public void testEmptyImageWithValidType() throws IOException {
		String type = "jpeg";
		
		String imageName = UUID.randomUUID().toString() + "." + type;
		String originalName = imageName;
		String contentType = "image/" + type;
		MultipartFile photoMPFile = new MockMultipartFile(imageName, originalName, contentType, (byte[])null);
		
		String storedName = PhotoService.write(photoMPFile, type);
		//the returned name of a empty file should be null
		assertNull(storedName);
		
		byte[] readContent = PhotoService.read(storedName);
		//the read content should be null
		assertNull(readContent);
	}
	
	@Test
	public void testParseType_acceptable() {
		String baseName = UUID.randomUUID().toString();
		MediaType returnedType;
		
		returnedType = PhotoService.parseType(baseName + ".jpg");
		assertEquals(MediaType.IMAGE_JPEG, returnedType);
		
		returnedType = PhotoService.parseType(baseName + ".jpeg");
		assertEquals(MediaType.IMAGE_JPEG, returnedType);
		
		returnedType = PhotoService.parseType(baseName + ".gif");
		assertEquals(MediaType.IMAGE_GIF, returnedType);
		
		returnedType = PhotoService.parseType(baseName + ".png");
		assertEquals(MediaType.IMAGE_PNG, returnedType);
	}
	
	@Test
	public void testParseType_unacceptable() {
		String baseName = UUID.randomUUID().toString();
		String unacceptableType = RandomData.genStr(5, 10);
		MediaType returnedType = PhotoService.parseType(baseName + "." + unacceptableType);
		
		assertNull(returnedType);
	}
	
	private void testServiceByImageType(String type) throws IOException, InterruptedException {
		String imageName = UUID.randomUUID().toString() + "." + type;
		String originalName = imageName;
		String contentType = "image/" + type;
		Path imagePath = Paths.get(imageDirPathStr, "test." + type);
		byte[] content = Files.readAllBytes(imagePath);
		MultipartFile photoMPFile = new MockMultipartFile(imageName, originalName, contentType, content);
		
		String storedName = PhotoService.write(photoMPFile, type);
		//the created name in the file system should not null
		assertNotNull(storedName);
		
		byte[] readContent = PhotoService.read(storedName);
		//read of the created photo shouldn't return null
		assertNotNull(readContent);
		//the read content should be equal to the written one
		assertTrue(Arrays.equals(content, readContent));
		
		PhotoService.delete(storedName);
		//read of the deleted photo should return null
		assertNull(PhotoService.read(storedName));
	}
}
