package com.harku.model;

public class OccModel {
	private String id;
	private String name;
	private Boolean state;
	
	public void setId(String _id) {
		id = _id;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public void setState(Boolean _state) {
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