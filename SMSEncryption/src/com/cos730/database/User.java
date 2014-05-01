package com.cos730.database;

public class User {
	private int userID;
	
	private String name;
	
	private String passwordHash;
	
	public User(){
		
	}
	
	public User(String _name, String _passHash){
		passwordHash = _passHash;
		name = _name;
	}

	public User(int _userID, String _name, String _passHash){
		userID = _userID;
		name = _name;
		passwordHash = _passHash;
	}
	
	public void setID(int newID){
		userID = newID;
	}
	
	public void setName(String newName){
		name = newName;
	}
	
	public void setPasswordHash(String newPasswordHash){
		passwordHash = newPasswordHash;
	}

	public int getID(){
		return userID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPasswordHash(){
		return passwordHash;
	}
}
