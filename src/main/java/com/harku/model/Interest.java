package com.harku.model;

public class Interest {
	private String id;
	private String name;
	private Boolean state;
	
	public void setId(String _id) {
		id = _id;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public void setState(boolean _state) {
		state = _state;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Boolean getState() {
		return state;
	}
}