/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cos730.encryption.Keys;

import com.cos730.encryption.Charset;
import java.math.BigInteger;

/**
 *
 * @author Fiyah
 */
public class Converter {

    Charset characterSet;

    public Converter(Charset cs) {
        characterSet = cs;
    }

    public String getStringRep(BigInteger seed) {
        byte[] toByteArray = seed.toByteArray();

        String temp = "";

        //10 char key
        for (int i = 0; i < 10; i++) {
            int value = toByteArray[i];

            // 57 for a simple version so its easy to share
            value = value % 57;

            if (value < 0) {
                value = value + 57;
            }

            value = value % 57;

            temp = temp + characterSet.getCharacterValue(value);

        }

        return temp;
    }
    
    public BigInteger getNumberRep(String in)
    {
        BigInteger temp=new BigInteger("1");
        for (int i = 0; i < in.length(); i++) {
            
            char c=in.charAt(i);
            int value=characterSet.getIntegerValue(c);
            if(value==0)
                value++;
            temp=temp.multiply(new BigInteger(""+value));
        }
        
        return temp;
    }
}
