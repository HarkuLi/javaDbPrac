package com.harku.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import com.harku.model.interest.IntModel;
import com.harku.model.occ.OccModel;
import com.harku.model.user.UsersModel;

public class RandomData {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final int workload = 5;
	
	public static String genStr(int minLength, int maxLength) {
		int lengthRange = maxLength - minLength + 1;
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
		String rst = "";
		int contentLen = (int)(Math.random()*lengthRange + minLength);
		
		for(int i=0; i<contentLen; ++i) {
			int idx = (int)(Math.random()*charSet.length());
			rst += charSet.charAt(idx);
		}
		return rst;
	}
	
	public static UsersModel genUser() {
		String id         = UUID.randomUUID().toString();
		String account    = genStr(1, 32);
		String password   = BCrypt.hashpw(genStr(60, 60), BCrypt.gensalt(workload));
		String name       = genStr(1, 40);
		int age           = (int)(Math.random()*80 + 1);
		String birth      = genBirth();
		String photoName  = UUID.randomUUID().toString() + "." + genImageType();
		String[] interest = genInterestSet(1, 10).toArray(new String[0]);
		String occupation = UUID.randomUUID().toString();
		Boolean state     = new Random().nextBoolean();
		Long signInTime   = (long)(Math.random()*System.currentTimeMillis());
		String token      = genToken();
		
		UsersModel newData = new UsersModel();
		newData.setId(id);
		newData.setAccount(account);
		newData.setPassword(password);
		newData.setName(name);
		newData.setAge(age);
		newData.setBirth(birth);
		newData.setPhotoName(photoName);
		newData.setInterest(interest);
		newData.setOccupation(occupation);
		newData.setState(state);
		newData.setSignInTime(signInTime);
		newData.setToken(token);
		return newData;
	}
	
	public static OccModel genOcc() {
		String id     = UUID.randomUUID().toString();
		String name   = genStr(1, 40);
		Boolean state = new Random().nextBoolean();
		
		OccModel newData = new OccModel();
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		return newData;
	}
	
	public static IntModel genInterest() {
		String id     = UUID.randomUUID().toString();
		String name   = genStr(1, 40);
		Boolean state = new Random().nextBoolean();
		
		IntModel newData = new IntModel();
		newData.setId(id);
		newData.setName(name);
		newData.setState(state);
		return newData;
	}
	
	public static Set<String> genInterestSet(int minSize, int maxSize) {
		Set<String> interestSet = new HashSet<String>();
		int sizeRange = maxSize - minSize + 1;
		int setSize = (int)(Math.random()*sizeRange + minSize);
		
		for(int i=0; i<setSize; ++i) interestSet.add(UUID.randomUUID().toString());
		
		return interestSet;
	}
	
	private static String genBirth() {
		long currentTime = System.currentTimeMillis();
		long randomTime = (long)(Math.random()*currentTime);
		Date date = new Date(randomTime);
 
		return sdf.format(date).toString();
	}
 
	private static String genImageType() {
		String[] imageTypes = {"jpeg", "png", "gif"};
		int idx = (int)(Math.random()*3);
		return imageTypes[idx];
	}
	
	private static String genToken() {
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
		String rst = "";
		int contentLen = 100;
		for(int i=0; i<contentLen; ++i) {
			int idx = (int)(Math.random()*64);
			rst += charSet.charAt(idx);
		}
		return rst;
	}
}
