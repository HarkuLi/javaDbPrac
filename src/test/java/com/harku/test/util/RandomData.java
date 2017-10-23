package com.harku.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.harku.model.user.UsersModel;

public class RandomData {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
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
		String id = UUID.randomUUID().toString();
		String name = genStr(3, 10);
		int age = (int)(Math.random()*80 + 1);
		String birth = genBirth();
		String photoName = UUID.randomUUID().toString() + "." + genImageType();
		String occupation = genStr(5, 10);
		
		UsersModel newData = new UsersModel();
		newData.setId(id);
		newData.setName(name);
		newData.setAge(age);
		newData.setBirth(birth);
		newData.setPhotoName(photoName);
		newData.setOccupation(occupation);;
		return newData;
	}
	
	public static Set<String> genInterestSet(int minSize, int maxSize) {
		Set<String> interestSet = new HashSet<String>();
		int sizeRange = maxSize - minSize + 1;
		int setSize = (int)(Math.random()*sizeRange + minSize);
		
		for(int i=0; i<setSize; ++i) interestSet.add(genStr(40, 40));
		
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
}
