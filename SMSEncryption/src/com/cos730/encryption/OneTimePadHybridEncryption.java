/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cos730.encryption;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.encryption.Keys.Converter;
import com.cos730.encryption.Keys.KeyGenerator;
import com.cos730.encryption.Keys.PadGenerator;

public class OneTimePadHybridEncryption {

    Charset cs;
    Converter conv;
    PadEncryption pen;
    PadGenerator pgen;
    KeyGenerator kgen;

    public OneTimePadHybridEncryption(Charset c) {
        cs = c;
        conv = new Converter(cs);
        pen = new PadEncryption(cs);
        pgen = new PadGenerator(cs);
        kgen = new KeyGenerator(cs);
    }

    public String Encrypt(String in, Contact con,Context cont) {
        //get Contact
        Contact reciever = con;

        //Generate pad from both keys
        String padkey = pgen.PadKey(conv.getNumberRep(reciever.getHisSeed()).longValue(), conv.getNumberRep(reciever.getMySeed()).longValue());
        System.out.println("padkey1 "+padkey);
        
        //pad  message to 144 chars cause with key it will be 154 and with failsave it will be 160
        while (in.length() < 144) {
            in = in + " ";
        }        

        //generate next seed
        String genKey = kgen.genKey();
        
        //add to message
        String phase1 = in + genKey;
        
        //add failsave
        phase1="cos730"+phase1;
        
        //encrypt message
        pen.padKey = padkey;
        String enc1 = pen.Encrypt(phase1); 
        
        //function seed with current seed        
        String Combined = kgen.KeyCombiner(reciever.getHisSeed(), genKey);

        //change his key to new seed
        DatabaseHandler dbHandler = new DatabaseHandler(cont);
        con.setHisSeed(Combined);
        System.out.println("com "+Combined);
        System.out.println("genKey "+genKey);
        dbHandler.updateContact(con);

        return enc1;

    }

    public String Decrypt(String in, Contact con,Context cont,Activity act) {

    	if(in.length()<160)
    	{
    		new AlertDialog.Builder(act)
    		.setTitle("Error")
    		.setMessage("Message to short please check again")
    	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            
    	        }
    	     })
    	     .show();
    		
    		return in;
    	}
    	
        //get Contact
        Contact sender = con;

        //generate pad       
        String padkey = pgen.PadKey(conv.getNumberRep(sender.getMySeed()).longValue(), conv.getNumberRep(sender.getHisSeed()).longValue());
        System.out.println("padkey1 "+padkey);
        
        //decrypt
        pen.padKey = padkey;
        String dec1 = pen.Decrypt(in);

        //get next key
        String phase1 = dec1.substring(dec1.length() - 10);
      
        //function seed with current seed
        String Combined = kgen.KeyCombiner(sender.getMySeed(), phase1);

        //get message
        String phase2 = dec1.substring(0, dec1.length() - 10);

        //update key
        DatabaseHandler dbHandler = new DatabaseHandler(cont);
        System.out.println("com "+Combined);
        System.out.println("genKey "+phase1);
        
        String failsave=phase2.substring(0,6);
        if(failsave.equals("cos730"))
        {
        
        con.setMySeed(Combined);
        dbHandler.updateContact(con);
        }
        else
        {
    		new AlertDialog.Builder(act)
    		.setTitle("Error")
    		.setMessage("Decryption failed, please resynchronize keys")
    	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	            
    	        }
    	     })
    	     .show();
        }
        
        phase2=phase2.substring(6);
        
        return phase2;
    }
}
