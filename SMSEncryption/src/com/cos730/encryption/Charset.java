package com.cos730.encryption;

public class Charset {

    /**
     * The character set allowed on SMS
     */
    public char[] characterSet = {
        
        '!',
        '"',
        '#',
        '$',
        '%',
        '&',
        '\'',
        '(',
        ')',
        '*',
        '+',
        ',',
        '-',
        '.',
        '/',
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        ':',
        ';',
        '<',
        '=',
        '>',
        '?',
        '@',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        '_',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z',
        /*
        '¡',
        '£',
        '¤',
        '¥',
        '§',
        '¿',
        'Ä',
        'Å',
        'Æ',
        'Ç',
        'É',
        'Ñ',
        'Ö',
        'Ø',
        'Ü',
        'ß',
        'à',
        'ä',
        'å',
        'æ',
        'è',
        'é',
        'ì',
        'ñ',
        'ò',
        'ö',
        'ø',
        'ù',
        'ü',
        */
        ' '};
    
    public int SetLength = characterSet.length;

    /**
     * Gets the single character that will replace the other value
     *
     * @param value
     * @return Returns a single character which is similar to the other
     * character
     */
    public String getCharacterValue(int value) {
        if (value >= characterSet.length) {
            return "";
        } else {
            
            while(value<0)
            {
                value=value+characterSet.length;
            }
            
            value=value % characterSet.length;
            
            return "" + characterSet[value];
        }
    }

    public int getIntegerValue(char theCharacter) {
        for (int i = 0; i < characterSet.length; i++) {
            if (theCharacter == characterSet[i]) {
                return i;
            }
        }

        return 0;
    }
}
