package com.cos730.database;

public class Contact {
	int contactID;
	
	String contactName;
	
	String phoneNumber;
	
	public Contact(){
		
	}
	
	// constructor
	public Contact(int id, String name, String _phone_number){
		this.contactID = id;
		this.contactName = name;
		this.phoneNumber = _phone_number;
	}
	
	public Contact( String name, String _phone_number){
		this.contactName = name;
		this.phoneNumber = _phone_number;
	}
	
	// getting ID
	public int getID(){
		return this.contactID;
	}

	// setting id
	public void setID(int id){
		this.contactID = id;
	}
		
	// getting name
	public String getName(){
		return this.contactName;
	}
		
	// setting name
	public void setName(String name){
		this.contactName = name;
	}
	
	// getting phone number
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	// setting phone number
	public void setPhoneNumber(String phone_number){
		this.phoneNumber = phone_number;
	}
}
