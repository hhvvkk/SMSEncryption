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


/**
 *  This class handles all security related matters to the application
 *  itself such as the enctryption and decryption of the database along with
 *  password hashing
 */
public class AppSecurity {

    static private String mPassword = null;
    static Cipher mEcipher = null;
    static Cipher mDecipher = null;
    static private String mUsername;

    /**
     * empty constructor
     */
    public AppSecurity() {
    }
   
    /**
     * 
     * @param password the password to be used.
     * Must be 16 bytes long to ensure correct use of database encryption.
     */

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

    /**
     * 
     * @param username the username to be used.
     * Must be 16 bytes long to ensure correct use of database encryption. 
     */
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
    
    /**
     * This function initializes the encryption with the correct parameters
     */

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

    /**
     * This function initializes the decryption with the correct parameters
     */
    
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

    /**
     * 
     * @param in The string to be encrypted
     * @return the encrypted string
     */
    public String Encrypt(String in) {

        byte[] temp = null;
        try {
            byte[] update = mEcipher.doFinal(in.getBytes());
            temp = update;

        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ByteConverter(temp);
    }

    /**
     * 
     * @param in The string to be decrypted
     * @return The plaintext String
     */
    public String Decrypt(String in) {
    	
        byte[] temp = null;

        try {
            byte[] update = mDecipher.doFinal(this.StringConverter(in));

            temp = update;


        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AppSecurity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.ByteToChar(temp);
    }

    /**
     * This function converts a byte array into a comma seperated string representation.
     * @param arr in the input byte array
     * @return The comma seperated representation.
     */
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

    /**
     * This function converts a byte array into a string representation.
     * @param arr input byte array
     * @return String reprentation
     */
    
    public String ByteToChar(byte[] arr) {
        String temp = "";
        for (int i = 0; i < arr.length; i++) {
            byte tempb = arr[i];
            int tempint = (int) tempb;
            temp = temp + (char) tempint;
        }

        return temp;
    }

    /**
     * This function converts a comma seperated string to a byte array
     * @param in the input comma seperated string
     * @return the byte array representation
     */
    public byte[] StringConverter(String in) {
        StringTokenizer token = new StringTokenizer(in, ",");
        byte[] temp = new byte[token.countTokens()];

        for (int i = 0; i < temp.length; i++) {
            String t = token.nextToken();
            temp[i] = Byte.valueOf(t);
        }

        return temp;
    }
    
    /**
     * This function generates the password hash using SHA-256
     * @param in The password
     * @return The hased string
     */

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
