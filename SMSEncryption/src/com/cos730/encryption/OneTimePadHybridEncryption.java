/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cos730.encryption;

import com.cos730.database.Contact;
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

    public String Encrypt(String in, Contact con) {
        //get Contact
        Contact reciever = con;

        //Generate pad from both keys
        String padkey = pgen.PadKey(conv.getNumberRep(reciever.getHisSeed()).longValue(), conv.getNumberRep(reciever.getMySeed()).longValue());

        //pad  message to 150 chars cause with key it will be 160
        while (in.length() < 150) {
            in = in + " ";
        }

        //encrypt message
        pen.padKey = padkey;
        String enc1 = pen.Encrypt(in);

        //generate next seed
        String genKey = kgen.genKey();

        //add to message
        String phase1 = enc1 + genKey;

        //gen pad again keys inverted
        String padkey2 = pgen.PadKey(conv.getNumberRep(reciever.getMySeed()).longValue(), conv.getNumberRep(reciever.getHisSeed()).longValue());

        //encrypt message
        pen.padKey = padkey2;
        String enc2 = pen.Encrypt(phase1);

        //function seed with current seed        
        String Combined = kgen.KeyCombiner(reciever.getHisSeed(), genKey);

        //change his key to new seed los vir nu
        //reciever.setHisSeed(Combined);

        return enc2;

    }

    public String Decrypt(String in, Contact con) {

        //get Contact
        Contact sender = con;

        //generate pad       
        String padkey = pgen.PadKey(conv.getNumberRep(sender.getHisSeed()).longValue(), conv.getNumberRep(sender.getMySeed()).longValue());

        //decrypt
        pen.padKey = padkey;
        String dec1 = pen.Decrypt(in);

        //get next key
        String phase1 = dec1.substring(dec1.length() - 10);
        System.out.println(dec1);

        //function seed with current seed
        String Combined = kgen.KeyCombiner(sender.getMySeed(), phase1);

        //get message
        String phase2 = dec1.substring(0, dec1.length() - 10);

        //generate phase 2 pad
        String padkey2 = pgen.PadKey(conv.getNumberRep(sender.getMySeed()).longValue(), conv.getNumberRep(sender.getHisSeed()).longValue());

        
        //decrypt
        pen.padKey = padkey2;
        String dec2 = pen.Decrypt(phase2);

        //update key los vir nu
     //   sender.setMySeed(Combined);

        return dec2;
    }
}
