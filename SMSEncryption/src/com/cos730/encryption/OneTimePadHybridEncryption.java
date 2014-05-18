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

	//current character set
    Charset cs;
    
    //used to convert numbers to strings and vice versa
    Converter conv;
    
    //the One-Time-Pad encryption
    PadEncryption pen;
    
    //Generates the One-Time-Pad
    PadGenerator pgen;
    
    //Generates the keys to be used
    KeyGenerator kgen;

    public OneTimePadHybridEncryption(Charset c) {
        cs = c;
        conv = new Converter(cs);
        pen = new PadEncryption(cs);
        pgen = new PadGenerator(cs);
        kgen = new KeyGenerator(cs);
    }

   /**
    * This function is the core of the encryption, it combines all the different
    * components to deliver the encrypted string
    * 
    * @param in The string to be encrypted
    * @param con The contact that the message is associated with
    * @param cont The current application context.
    * @return The encrypted String
    */
    public String Encrypt(String in, Contact con,Context cont) {
        //get Contact
        Contact reciever = con;

        //Generate pad from both keys
        String padkey = pgen.PadKey(conv.getNumberRep(reciever.getHisSeed()).longValue(), conv.getNumberRep(reciever.getMySeed()).longValue());
        
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
        dbHandler.updateContact(con);

        return enc1;

    }
    
    /**
     * This function is responsible for decrypting the encrypted text.
     * 
     * @param in The input encrypted String.
     * @param con The corresponding contact.
     * @param cont The current application context.
     * @param act The activity currently displayed, for error message use.
     * @return
     */

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
        
        //remove failsave
        phase2=phase2.substring(6);
        
        return phase2;
    }
}
