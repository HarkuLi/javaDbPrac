package model.user;

import java.sql.Date;
import java.util.ArrayList;

public class UsersModel{
	private String id;
	private String name;
	private int age;
	private Date birth;
	private String photoName;
	//a list of interest id
	private ArrayList<String> interest;
	private String occupation;
	private boolean state;
	
	public UsersModel() {
		id = null;
		name = null;
		age = 0;
		birth = null;
		photoName= null;
		interest = null;
		occupation = null;
		state = true;
	}
	
	public void setId(String _id) {
		id = _id;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public void setAge(int _age) {
		age = _age;
	}
	
	public void setBirth(Date _birth) {
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
	
	public void setState(boolean _state) {
		state = _state;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAge() {
		return age;
	}
	
	public Date getBirth() {
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
	
	public boolean getState() {
		return state;
	}
}
