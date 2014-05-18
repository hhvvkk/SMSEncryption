package com.cos730.encryption;


public class PadEncryption {

	//represents the one time pad to be used
    public String padKey;
    
    //the current character set
    Charset CharacterSet;

    public PadEncryption(Charset cs) {
        CharacterSet = cs;
    }

    /**
     * This function encrypts a string using a One-Time-Pad
     * @param in The String to be encrypted
     * @return The encrypted String
     */
    public String Encrypt(String in) {
        String temp = "";

        for (int i = 0; i < in.length(); i++) {

            int Sipherval = CharacterSet.getIntegerValue(in.charAt(i));

            int keyval = CharacterSet.getIntegerValue(padKey.charAt(i));

            int EncryptedVal = Sipherval + keyval;

            EncryptedVal = EncryptedVal % CharacterSet.SetLength;

            temp = temp + CharacterSet.getCharacterValue(EncryptedVal);

        }
        return temp;
    }

    /**
     * This function decrypts a string using One-Time-Pad
     * @param in the encrypted String
     * @return the plaintext
     */
    public String Decrypt(String in) {
        String temp = "";

        for (int i = 0; i < in.length(); i++) {

            int Sipherval = CharacterSet.getIntegerValue(in.charAt(i));

            int keyval = CharacterSet.getIntegerValue(padKey.charAt(i));

            int DecryptedVal = Sipherval - keyval;

            DecryptedVal = DecryptedVal % CharacterSet.SetLength;

            while (DecryptedVal < 0) {
                DecryptedVal = DecryptedVal + CharacterSet.SetLength;
            }

            DecryptedVal = DecryptedVal % CharacterSet.SetLength;

            temp = temp + CharacterSet.getCharacterValue(DecryptedVal);

        }
        return temp;
    }
}
