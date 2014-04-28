package com.cos730.encryption;


public class PadEncryption {

    public String padKey;
    Charset CharacterSet;

    public PadEncryption(Charset cs) {
        CharacterSet = cs;
    }

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
