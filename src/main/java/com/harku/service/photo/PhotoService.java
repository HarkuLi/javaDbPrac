package com.harku.service.photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class PhotoService {
	private static final String STORE_PATH = Paths.get(System.getProperty("user.home"), "upload").toString();
	private static final List<String> acceptType = Arrays.asList("jpg", "jpeg" ,"png", "gif");
	
	public static final String DEFAULT_PHOTO_NAME = "default.png";
	
	/**
	 * 
	 * @param photo name
	 * @return null if the photo dosen't exist
	 * @throws IOException
	 */
	public static byte[] read(String photoName) throws IOException {
		if(photoName == null) return null;
		
		Path path = Paths.get(STORE_PATH, photoName);
		
		if(!Files.exists(path)) return null;
		
		byte[] photo = Files.readAllBytes(path);
		return photo;
	}
	
	/**
	 * 
	 * @param photo
	 * @param photoType
	 * @return photo name, null if not successfully
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String write(MultipartFile photo, String photoType) {
		
		if(photo.getSize() == 0) return null;
		if(!acceptType.contains(photoType))	return null;
		
		String photoName = UUID.randomUUID().toString();
		photoName += "." + photoType;	//filename extension
		String path = Paths.get(STORE_PATH, photoName).toString();
		
		//check whether the destination folder exists
		File dir = new File(STORE_PATH);
		if(!dir.exists()) dir.mkdir();
		
		try {
			dir = new File(path);
			photo.transferTo(dir);
		}
		catch(Exception e) {
			return null;
		}
		
		return photoName;
	}
	
	public static void delete(String photoName) {
		
		if(photoName == null) return;
		
		String path = Paths.get(STORE_PATH, photoName).toString();
		File file = new File(path);
		if(file.exists()) file.delete();
	}
	
	/**
	 * 
	 * @param photoName
	 * @return MediaType of the file name. Return null if there is no matched type.
	 */
	public static MediaType parseType(String photoName) {
		String[] fileNameParts = photoName.split("\\.");
		String type = fileNameParts[fileNameParts.length - 1];
		
		if(type.equals("png")) return MediaType.IMAGE_PNG;
		else if(type.equals("gif")) return MediaType.IMAGE_GIF;
		else if(type.equals("jpeg")) return MediaType.IMAGE_JPEG;
		else if(type.equals("jpg")) return MediaType.IMAGE_JPEG;
		
		return null;
	}
}
