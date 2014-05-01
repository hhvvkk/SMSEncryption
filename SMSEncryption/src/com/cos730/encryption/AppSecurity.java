package com.cos730.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AppSecurity {

    static private String mPassword = null;
    static Cipher mEcipher = null;
    static Cipher mDecipher = null;
    static private String mUsername;

    public AppSecurity() {
    }

    public void setPassword(String password) {

        //password must be 16 bytes

        if (password.length() > 16) {
            mPassword = password.substring(0, 16);
        } else {
            mPassword = password;

            while (mPassword.length() < 16) {
                mPassword = mPassword + "a";
            }
        }

    }

    public void setUsername(String username) {


        if (username.length() > 16) {
            mUsername = username.substring(0, 16);
        } else {
            mUsername = username;

            while (mUsername.length() < 16) {
                mUsername = mUsername + "a";
            }
        }
    }

    public void EncryptSetup() {

        String key1 = mUsername;
        String key2 = mPassword;

        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
            mEcipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            mEcipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);


        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DecryptSetup() {

        String key1 = mUsername;
        String key2 = mPassword;


        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
            mDecipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            mDecipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException | NoSuchPaddingException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String Encrypt(String in) {

    	System.out.println(in);
    	System.out.println(mUsername);
    	System.out.println(mPassword);
    	System.out.println(mEcipher);

        byte[] temp = null;
        try {
            byte[] update = mEcipher.doFinal(in.getBytes());
            temp = update;

        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ByteConverter(temp);
    }

    public String Decrypt(String in) {
    	
    	System.out.println(in);
    	System.out.println(mUsername);
    	System.out.println(mPassword);
    	System.out.println(mEcipher);

        byte[] temp = null;

        try {
            byte[] update = mDecipher.doFinal(this.StringConverter(in));

            temp = update;


        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.ByteToChar(temp);
    }

    public String ByteConverter(byte[] arr) {
        String temp = "";
        for (int i = 0; i < arr.length; i++) {
            byte tempb = arr[i];
            int tempint = (int) tempb;
            if (i == 0) {
                temp = temp + tempint;
            } else {
                temp = temp + "," + tempint;
            }
        }

        return temp;
    }

    public String ByteToChar(byte[] arr) {
        String temp = "";
        for (int i = 0; i < arr.length; i++) {
            byte tempb = arr[i];
            int tempint = (int) tempb;
            temp = temp + (char) tempint;
        }

        return temp;
    }

    public byte[] StringConverter(String in) {
        StringTokenizer token = new StringTokenizer(in, ",");
        byte[] temp = new byte[token.countTokens()];

        for (int i = 0; i < temp.length; i++) {
            String t = token.nextToken();
            temp[i] = Byte.valueOf(t);
        }

        return temp;
    }

    public String generateHash(String in) {
        String encryptedString = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(in.getBytes());
            byte[] arr = messageDigest.digest();

            for (int i = 0; i < arr.length; i++) {

                int temp = (int) arr[i];

                temp = temp * (i + 1);

                //apparently good with has functions
                temp = temp * 31;

                //some splash of crazy
                temp = temp % 161;

                encryptedString = encryptedString + temp;
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedString;
    }
}
