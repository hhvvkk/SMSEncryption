package com.cos730.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.ArrayAdapter;


public class ContactContent {

    private static Context activityContext = null;
    
    private static List<ContactItem> ITEMS = new ArrayList<ContactItem>();

    private static Map<String, ContactItem> ITEM_MAP = new HashMap<String, ContactItem>();
    
    private static DatabaseHandler dbHandler = null;
    
    public static  ArrayAdapter<ContactContent.ContactItem> adap=null;

    public ContactContent(Context theContext){
    	
    	if(activityContext == null){
    		activityContext = theContext;
    		
        	dbHandler = new DatabaseHandler(theContext);
        	
        	List<Contact> contactList = dbHandler.getAllContacts();
        	
        	//add them to the
        	for(int i = 0; i < contactList.size(); i++){
        		Contact contact = contactList.get(i);
        		ContactItem item = new ContactItem(Integer.toString(contact.getID()), contact.getName(),contact.getHisSeed(),contact.getMySeed());
        		
            	addItem(item);
        	}
    	} 

    	
    }
    
    public void UpdateChanges()
    {
    	ITEMS.clear();
    	ITEM_MAP.clear();
    	
    	List<Contact> contactList = dbHandler.getAllContacts();
    	
    	//add them to the
    	for(int i = 0; i < contactList.size(); i++){
    		Contact contact = contactList.get(i);
    		ContactItem item = new ContactItem(Integer.toString(contact.getID()), contact.getName(),contact.getHisSeed(),contact.getMySeed());
    		
        	addItem(item);
    	}
    	
    	adap.notifyDataSetChanged();
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
