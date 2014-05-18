/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cos730.encryption.Keys;

import com.cos730.encryption.Charset;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Generates the one time pad to be used
 */
public class PadGenerator {

	//The current character set
    Charset cs;

    public PadGenerator(Charset set) {

        cs = set;
    }

    /**
     * Generates OneTimePad entry based on 2 seeds
     * @param seed1 The first key
     * @param seed2 The second key
     * @return a One-Time-Pad generated with the keys
     */    
    
    public String PadKey(Long seed1, Long seed2) {
      
        Long firstSeed = seed1;
        Long SecondSeed = seed2;

        Random sran = new Random();
        Random sran2 = new Random();
        sran.setSeed(firstSeed);
        sran2.setSeed(SecondSeed);

        String temp = "";

        int[] values = new int[160];

        for (int i = 0; i < 160; i++) {

            int value = sran.nextInt();

            value = Math.abs(value);

            value = value % cs.SetLength;

            value = value + sran2.nextInt();

            value = value % cs.SetLength;

            while (value < 0) {
                value = value + cs.SetLength;
            }

            value = value % cs.SetLength;

            sran.setSeed(sran.nextInt());
            
            sran2.setSeed(sran2.nextInt());

            values[i] = value;
        }


        for (int i = 0; i < 160; i++) {
            temp = temp + cs.getCharacterValue(values[i]);
        }


        return temp;
    }  
    

}
