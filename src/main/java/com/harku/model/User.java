package com.harku.model;

import java.util.Arrays;

public class User{
	private String id;
	private String account;
	//the hashed password
	private String password;
	private String name;
	private Integer age;
	private String birth;
	private String photoName;
	//a list of interest id
	private String[] interest;
	private String occupation;
	private String state;
	private Long signInTime;
	private String token;
	
	public User() {}
	
	public User(User user) {
		id = user.getId();
		account = user.getAccount();
		password = user.getPassword();
		name = user.getName();
		age = user.getAge();
		birth = user.getBirth();
		photoName = user.getPhotoName();
		
		if(user.getInterest() != null)
			interest = Arrays.asList(user.getInterest()).toArray(new String[0]);
		
		occupation = user.getOccupation();
		state = user.getState();
		signInTime = user.getSignInTime();
		token = user.getToken();
	}
	
	public void setId(String _id) {
		id = _id;
	}
	
	public void setAccount(String _account) {
		account = _account;
	}
	
	public void setPassword(String _password) {
		password = _password;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public void setAge(Integer _age) {
		age = _age;
	}
	
	public void setBirth(String _birth) {
		birth = _birth;
	}
	
	public void setPhotoName(String _photoName) {
		photoName = _photoName;
	}
	
	public void setInterest(String[] _interest) {
		interest = _interest;
	}
	
	public void setOccupation(String _occupation) {
		occupation = _occupation;
	}
	
	public void setState(String _state) {
		state = _state;
	}
	
	public void setSignInTime(Long _signInTime) {
		signInTime = _signInTime;
	}
	
	public void setToken(String _token) {
		token = _token;
	}
	
	public String getId() {
		return id;
	}
	
	public String getAccount() {
		return account;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public String getBirth() {
		return birth;
	}
	
	public String getPhotoName() {
		return photoName;
	}
	
	public String[] getInterest() {
		return interest;
	}
	
	public String getOccupation() {
		return occupation;
	}
	
	public String getState() {
		return state;
	}
	
	public Long getSignInTime() {
		return signInTime;
	}
	
	public String getToken() {
		return token;
	}
	
	public void eraseSecretInfo() {
		password = null;
		signInTime = null;
		token = null;
	}
}
