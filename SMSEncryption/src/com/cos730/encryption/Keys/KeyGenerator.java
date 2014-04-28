/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cos730.encryption.Keys;

import com.cos730.encryption.Charset;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Fiyah
 */
public class KeyGenerator {

    SecureRandom srand = new SecureRandom();
    Converter con;

    public KeyGenerator(Charset cs) {
        con = new Converter(cs);
    }

    public String genKey() {
        //8 bits 1 char
        //10 chars is 80 bits
        BigInteger temp = new BigInteger(80, srand);
        return con.getStringRep(temp);
    }

    public String KeyCombiner(String s1, String s2) {

        SecureRandom srand2 = new SecureRandom();
        
        BigInteger b1 = con.getNumberRep(s1);
        BigInteger b2 = con.getNumberRep(s2);

        BigInteger com=b1.add(b2);
        
        srand2.setSeed(com.longValue());


        BigInteger temp = new BigInteger(80, srand2);

        return con.getStringRep(temp);
    }
}
