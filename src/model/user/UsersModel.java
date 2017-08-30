package model.user;

import java.sql.Date;

public class UsersModel{
	private String id;
	private String name;
	private int age;
	private Date birth;
	private String photoName;
	
	public UsersModel() {
		id = null;
		name = null;
		age = 0;
		birth = null;
		photoName = null;
	}
	
	public UsersModel(String _id, String _name, int _age, Date _birth, String _photoName){
		id = _id;
		name = _name;
		age = _age;
		birth = _birth;
		photoName = _photoName;
	}
	
	public void setValue(String _id, String _name, int _age, Date _birth, String _photoName) {
		id = _id;
		name = _name;
		age = _age;
		birth = _birth;
		photoName = _photoName;
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
}
