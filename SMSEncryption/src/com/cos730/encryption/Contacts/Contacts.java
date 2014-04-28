/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cos730.encryption.Contacts;

import com.cos730.database.Contact;
import com.cos730.encryption.Charset;
import com.cos730.encryption.Keys.KeyGenerator;

import java.util.ArrayList;


public class Contacts {
    
    public ArrayList<Contact> myContacts;
    Charset cs;
    
    public Contacts(Charset c)
    {
        myContacts=new ArrayList();
        cs=c;
    }
    
}
