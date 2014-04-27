package com.cos730.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;


public class ContactContent {

    private static Context activityContext = null;
    
    private static List<ContactItem> ITEMS = new ArrayList<ContactItem>();

    private static Map<String, ContactItem> ITEM_MAP = new HashMap<String, ContactItem>();

    public ContactContent(Context theContext){
    	
    	if(activityContext == null){
    		activityContext = theContext;
    		
        	DatabaseHandler dbHandler = new DatabaseHandler(theContext);
        	/**
        	 * TODO: remove dummy contact adding
        	 */
        	if(dbHandler.getAllContacts().size() == 0){
	        	addDummyContact(dbHandler, "Name1");
	        	addDummyContact(dbHandler, "Name2");
	        	addDummyContact(dbHandler, "Name44");
        	}
        	
        	List<Contact> contactList = dbHandler.getAllContacts();
        	
        	//add them to the
        	for(int i = 0; i < contactList.size(); i++){
        		Contact contact = contactList.get(i);
        		ContactItem item = new ContactItem(Integer.toString(contact.getID()), contact.getName());
        		
            	addItem(item);
        	}
    	}
    }
    
    private void addDummyContact(DatabaseHandler dbHandler, String name){
    	Contact newContact = new Contact();
    	newContact.setName(name);
    	dbHandler.addContact(newContact);
    }
    

    private void addItem(ContactItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class ContactItem {
        public String id;
        public String name;

        public ContactItem(String id, String content) {
            this.id = id;
            this.name = content;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
    public Map<String, ContactItem> getItemMap(){
    	return ITEM_MAP;
    }
    
    public List<ContactItem> getItems(){
    	return ITEMS;
    }
    
}
