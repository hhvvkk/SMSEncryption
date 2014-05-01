package com.cos730.user;

import java.util.List;

import android.content.Context;

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.database.User;

public class LoginHandler {
	
	/**
	 * Tries to login to the application
	 * @param user The user who wishes to login
	 */
	public static boolean tryLogin(User user, String userPasswordHashed){
		if(user.getPasswordHash().equals(userPasswordHashed)){
			return true;
		}
		return false;
	}
	
	/**
	 * Generate a hash value for a character array
	 * @param password The entered password
	 * @return Returns a hash value of password entered in by user
	 */
	public static String hashValue(char []password){
		
		return new String(password);
	}
	
	/**
	 * Gets a user by name
	 * @param name The name of the user to find
	 * @param activityContext The context of the current activity using this class
	 * @return Returns a user by name or null if not found
	 */
	public static User getUserByName(String name, Context activityContext){
	
		DatabaseHandler dbHandler = new DatabaseHandler(activityContext);
		
		List<User> userList = dbHandler.getAllUsers();
		
		//loop through to find user
		for(int i = 0; i < userList.size(); i++){
			//return user if it is found
			if(userList.get(i).getName().equals(name)){
				return userList.get(i);
			}
		}
		
		
		return null;
	}
}
