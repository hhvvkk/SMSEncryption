/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cos730.encryption.Keys;

import com.cos730.encryption.Charset;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * This class generates keys to be used for encryption
 */
public class KeyGenerator {

	// a secure random to be used to generate BigIntegers
	Random srand = new Random();
	
	//converts numbers to string and vice versa
    Converter con;

    public KeyGenerator(Charset cs) {
        con = new Converter(cs);
    }

    /**
     * Generates a random BigInteger and returns its String representation
     * @return The string representation of the BigInteger.
     */
    public String genKey() {
        //8 bits 1 char
        //10 chars is 80 bits
        BigInteger temp = new BigInteger(80, srand);
        return con.getStringRep(temp);
    }

    /**
     * This function combines two keys to create one
     * @param s1 The first key
     * @param s2 The second key
     * @return A random BigInteger using the two keys as a random seed
     */
    public String KeyCombiner(String s1, String s2) {

        Random srand2 = new Random();
        
        BigInteger b1 = con.getNumberRep(s1);
        BigInteger b2 = con.getNumberRep(s2);

        BigInteger com=b1.add(b2);
        
        srand2.setSeed(com.longValue());


        BigInteger temp = new BigInteger(80, srand2);

        return con.getStringRep(temp);
    }
}
