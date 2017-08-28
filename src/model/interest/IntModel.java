package model.interest;

import java.util.HashMap;

public class IntModel {
	private String id;
	private String name;
	private boolean state;
	
	public IntModel(HashMap<String, Object> data) {
		id = (String) data.get("id");
		name = (String) data.get("name");
		state = (boolean) data.get("state");
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getState() {
		return state;
	}
}