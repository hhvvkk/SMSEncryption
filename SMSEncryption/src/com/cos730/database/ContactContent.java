package com.cos730.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;


public class ContactContent {

    private static Context activityContext = null;
    
    private static List<ContactItem> ITEMS = new ArrayList<ContactItem>();

    private static Map<String, ContactItem> ITEM_MAP = new HashMap<String, ContactItem>();

    public ContactContent(Context theContext){
    	
    	if(activityContext == null){
    		activityContext = theContext;
    		
        	DatabaseHandler dbHandler = new DatabaseHandler(theContext);
        	
        	List<Contact> contactList = dbHandler.getAllContacts();
        	
        	//add them to the
        	for(int i = 0; i < contactList.size(); i++){
        		Contact contact = contactList.get(i);
        		ContactItem item = new ContactItem(Integer.toString(contact.getID()), contact.getName(),contact.getHisSeed(),contact.getMySeed());
        		
            	addItem(item);
        	}
    	}
    }
    

    private void addItem(ContactItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class ContactItem {
        public String id;
        public String name;
        public String hisSeed;
        public String mySeed;

        public ContactItem(String id, String content,String _hisSeed,String _mySeed) {
            this.id = id;
            this.name = content;
            this.hisSeed=_hisSeed;
            this.mySeed=_mySeed;
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
