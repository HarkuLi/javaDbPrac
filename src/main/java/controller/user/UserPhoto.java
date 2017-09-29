package controller.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserPhoto{	
	@RequestMapping(value = "/photo", method = RequestMethod.GET, produces = {"image/jpeg", "image/gif", "image/png"})
	public ResponseEntity<byte[]> get(
		@RequestParam(value = "n", required = false, defaultValue = "default.png") String filename) throws IOException {
		
		String homePathStr = System.getProperty("user.home");
		Path path = Paths.get(homePathStr, "upload", filename);
		HttpHeaders headers = new HttpHeaders();
		
		//read photo
		if(!Files.exists(path)) path = Paths.get(homePathStr, "upload", "default.png");
		byte[] photo = Files.readAllBytes(path);
		
		//set the content type of header
		String[] pathStrParts = path.toString().split("\\.");
		String type = pathStrParts[pathStrParts.length - 1];
		if(type.equals("png")) headers.setContentType(MediaType.IMAGE_PNG);
		else if(type.equals("gif")) headers.setContentType(MediaType.IMAGE_GIF);
		else headers.setContentType(MediaType.IMAGE_JPEG);
		
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(photo, headers, HttpStatus.OK);
		return responseEntity;
	}
}