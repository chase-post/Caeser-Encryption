/*
* Name: Chase Post
* Pennkey: cpost
* General Execution: java Caesar intent fileName (shiftAmount OR english file)
* Example Execution: java Caesar crack message.txt english.txt
*
* Function of program: Encrypts, decrypts, or cracks a string using
* the technique of shifting each letter along the alphabet by a certain
* amount or "key".
*/

public class Caesar {
    
    /*
    * Description: converts a string to a symbol array,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  the message text to be converted
    * Output: integer encoding of the message
    */
    public static int[] stringToSymbolArray(String str) {
        //convert to uppercase for consistent integer conversion
        str = str.toUpperCase();
        
        int[] symbolArray = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char letter = str.charAt(i);
            //subtract A to shift scale from 65-90 to 0-25
            int symbolRepresentation = (int) letter - 'A';
            symbolArray[i] = symbolRepresentation;
        }
        return symbolArray;
    }
    
    /*
    * Description: converts an array of symbols to a string,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  integer encoding of the message
    * Output: the message text
    */
    public static String symbolArrayToString(int[] symbols) {
        String stringFromSymbols = "";
        for (int i = 0; i < symbols.length; i++) {
            char letter = (char) (symbols[i] + 'A');
            stringFromSymbols += letter;
        }
        return stringFromSymbols;
    }
    
    /*
    * Description: shifts all characters of a string down the alphabet by an amount
    *              designated by the inputed offset integer.
    * Inputs: the symbol (integer) to be shifted and an offset integer 
    *         between 0 and 25.
    * Output: the shifted symbol.
    */
    public static int shift(int symbol, int offset) {
        if (symbol >= 0 && symbol <= 25) {
            symbol += offset;
            //wrap around from Z back to A when needed
            if (symbol > 25) {
                symbol -= 26;
                return symbol;
            } else {
                return symbol;
            }
        //if symbol is a space or punctuation, leave it unchanged
        } else {
            return symbol;
        }
    }
    
    /*
    * Description: reverses the shift function by shifting 
    *              the characters back up the alphabet by the amount "offset".
    * Inputs: the symbol (integer) to be unshifted and 
    *         an offset integer between 0 and 25.
    * Output: the unshifted/original symbol.
    */
    public static int unshift(int symbol, int offset) {
        if (symbol >= 0 && symbol <= 25) {
            symbol -= offset;
            //wrap around from A back to Z when needed
            if (symbol < 0) {
                symbol += 26;
                return symbol;
            } else {
                return symbol;
            }
        //if symbol is a space or punctuation, leave it unchanged
        } else {
           return symbol;
        }
    }
    
    /*
    * Description: encrypts a string message by converting it to an array of
    *              integers and shifting everything down the alphabet by a 
    *              designated amount.
    * Inputs: message text to be encrypted and integer amount to shift each letter.
    * Output: encrypted message as a string.
    */
    public static String encrypt(String message, int key) {
        int[] symbolMessage = stringToSymbolArray(message);
        for (int i = 0; i < symbolMessage.length; i++) {
            symbolMessage[i] = shift(symbolMessage[i], key);
        }
        return symbolArrayToString(symbolMessage);
    }
    
    /*
    * Description: decrypts a string message by converting it to an array of
    *              integers and shifting everything back up the alphabet by a 
    *              designated amount.
    * Inputs: encrypted message text and the key to unshift each letter.
    * Output: decrypted message as a string.
    */
    public static String decrypt(String cipher, int key) {
        int[] symbolMessage = stringToSymbolArray(cipher);
        for (int i = 0; i < symbolMessage.length; i++) {
            symbolMessage[i] = unshift(symbolMessage[i], key);
        }
        return symbolArrayToString(symbolMessage);
    }
    
    /*
    * Description: Import the frequencies of each letter in the English language
    *              and store as an array of doubles.
    *Input: fileName location where letter frequencies are held.
    *Output: double array of letter frequencies.
    */
    public static double[] getDictionaryFrequencies(String filename) {
        double[] letterFrequencies = new double[26];
        In inStream = new In(filename);
        for (int i = 0; i < letterFrequencies.length; i++) {
            letterFrequencies[i] = inStream.readDouble();
        }
        return letterFrequencies;
    }
    
    /*
    * Description: determines the frequencies of each integer in an array.
    *              integers represent characters of a text file.
    * Input:  integer of symbols.
    * Output: frequencies of different integers/chars as a 
    *         double array of length 26. 
    */
    public static double[] findFrequencies(int[] symbols) {
        double[] encryptedFrequencies = new double[26];
        int totalLetters = 0;
        //only add letters to letter count, not punctuation or spaces
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] >= 0 && symbols[i] <= 25) {
                totalLetters++;
            }
        }
        //determines frequency of each letter i in alphabet
        for (int i = 0; i < encryptedFrequencies.length; i++) {
            for (int k = 0; k < symbols.length; k++) {
                if (symbols[k] == i) {
                    encryptedFrequencies[i]++;
                }
            }
            encryptedFrequencies[i] /= totalLetters;
        }
        return encryptedFrequencies;
    }
    
    /*
     * Description: returns a score representing how close 
     *              the double array's letter frequencies are 
     *              to those of english.
     * Inputs: double array of english frequencies and
     *         double array of text frequencies.
     * Output: score for the frequencies of the text... closer to 0
     *         is closer to english.
     */
    public static double scoreFrequencies(double[] english, double[] currentFreqs) {
        double totalScore = 0.0;
        for (int i = 0; i < english.length; i++) { 
            double increment = Math.abs(currentFreqs[i] - english[i]);
            totalScore += increment;
        }
        return totalScore;
    }
    
    /*
     * Description: cracks the code of an ecrypted string and 
     *              outputs english message.
     * Inputs:  a double array with english letter frequencies and
     *         an encrypted message as a string. 
     * Output: decrypted message.
     */ 
    public static String crack(double[] english, String encryptedMessage) {
        double minScore = 10000;
        int key = 0;
        for (int i = 0; i < english.length; i++) {
            int[] possible = stringToSymbolArray(decrypt(encryptedMessage, i));
            double[] freqOfPossible = findFrequencies(possible);
            double score = scoreFrequencies(english, freqOfPossible);
            if (score < minScore) {
                minScore = score;
                key = i;
            }
        }
        return decrypt(encryptedMessage, key);
    }
    
    public static void main(String[] args) {
        //command line arguments
        String intent = args[0];
        String fileName = args[1];
        
        //read message from file
        In messageStream = new In(fileName);
        String message = messageStream.readAll();
  
                
        //what to do to with the string from file?
        if (intent.equals("encrypt") || intent.equals("decrypt")) {
            String shiftAmount = args[2];
            
            //ensure input string/char is uppercase
            shiftAmount = shiftAmount.toUpperCase();
        
            //convert input string for shift amount to an int from 0 to 25
            char shiftAsChar = shiftAmount.charAt(0);
            int shiftAsInt = (int) shiftAsChar - 'A';
            
            if (intent.equals("encrypt")) {
                String encryptedMessage = encrypt(message, shiftAsInt);
                System.out.println(encryptedMessage);
            
            } else if (intent.equals("decrypt")) {
                String decryptedMessage = decrypt(message, shiftAsInt);
                System.out.println(decryptedMessage);
            }
            
        } else if (intent.equals("crack")) {
            //specify file for englishFrequencies in command line
            String englishFrequencies = args[2];
                
            //read english frequencies from file
            In englishStream = new In(englishFrequencies);
            double[] english = new double[26];
            for (int i = 0; i < 26; i++) {
                english[i] = englishStream.readDouble();
                }
            String crackedMessage = crack(english, message);
            System.out.println(crackedMessage);
                
        } else {
            //if intent isn't encrypt, decrypt, or crack, there was a typo
            System.out.println("Command Error: check spelling");
        }
       
    }        
}
