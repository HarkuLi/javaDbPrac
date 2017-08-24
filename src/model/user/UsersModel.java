package model.user;

import java.sql.Date;

public class UsersModel{
	private int id;
	private String name;
	private int age;
	private Date birth;
	
	public UsersModel() {
		id = 0;
		name = null;
		age = 0;
		birth = null;
	}
	
	public UsersModel(int _id, String _name, int _age, Date _birth){
		id = _id;
		name = _name;
		age = _age;
		birth = _birth;
	}
	
	public void setValue(int _id, String _name, int _age, Date _birth) {
		id = _id;
		name = _name;
		age = _age;
		birth = _birth;
	}
	
	public int getId() {
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
}
