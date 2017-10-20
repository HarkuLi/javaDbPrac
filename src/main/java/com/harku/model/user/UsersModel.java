package com.harku.model.user;

import java.util.ArrayList;

public class UsersModel{
	private String id;
	private String account;
	//the hashed password
	private String password;
	private String name;
	private Integer age;
	private String birth;
	private String photoName;
	//a list of interest id
	private ArrayList<String> interest;
	private String occupation;
	private Boolean state;
	
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
	
	public void setInterest(ArrayList<String> _interest) {
		if(_interest.size() != 0) interest = _interest;
	}
	
	public void setOccupation(String _occupation) {
		occupation = _occupation;
	}
	
	public void setState(Boolean _state) {
		state = _state;
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
	
	public ArrayList<String> getInterest() {
		return interest;
	}
	
	public String getOccupation() {
		return occupation;
	}
	
	public Boolean getState() {
		return state;
	}
	
	public void eraseSecretInfo() {
		password = null;
	}
}
