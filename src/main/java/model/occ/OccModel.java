package model.occ;

import java.util.HashMap;

public class OccModel {
	private String id;
	private String name;
	private Boolean state;
	
	public OccModel() {
		id = null;
		name = null;
		state = null;
	}
	
	public OccModel(HashMap<String, Object> data) {
		id = (String) data.get("id");
		name = (String) data.get("name");
		state = (Boolean) data.get("state");
	}
	
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
	
	public boolean getState() {
		return state;
	}
}
