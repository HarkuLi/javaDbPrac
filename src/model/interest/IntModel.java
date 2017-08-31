package model.interest;

public class IntModel {
	private String id;
	private String name;
	private boolean state;
	
	public IntModel() {
		id = null;
		name = null;
		state = true;
	}
	
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
	
	public boolean getState() {
		return state;
	}
}