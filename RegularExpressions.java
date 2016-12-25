package Icg;
/**
 * This class implements the regular expressions used to parse the input
 */

/**
 * @author Danny Reinheimer
 *
 */
public class RegularExpressions {
	
	
	// Array of reserved words
	private String [] reservedWords = {"int","void","if","while","return","read","write","print","continue","break","binary",
			"decimal"};
	
	// Array of symbols
	private String [] symbols = {"(",")","{","}","[","]",",",";","+","-","*","/","==","!=",">",">=","<","<=",
			"=","&&","||"};
	
	/**
	 * Checks to see if the input matches a space.  A space is defined as tab, spacebar, newline
	 * @param str The String to be checked
	 * @return boolean indicating if it is a space
	 */
	public boolean isSpace(String str) {
		char ch = str.charAt(0);
		//if(ch == ' ') {
			for (int i = 0; i < str.length(); i++) {
				ch = str.charAt(i);
				if(ch != ' ' && ch != '\n' && ch != '\r' && ch != '\u001a' && ch != '\t') {
					// this is not space
					return false;
				}
			}
			return true;
		//}
		//return false;
	}
	
	/***
	 * Checks to see if a character is a digit
	 * @param c the character to check 
	 * @return boolean indicating if it is a digit
	 */
	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}
	
	/**
	 * Checks to see if the given input matches the pattern for an identifier.
	 * The pattern is: Letter(Letter|digit)*
	 * @param str The input string that is being pasrsed which will be read one charecter at a time
	 * @return boolean indicating if it is an identifier
	 */
	public boolean isIdentifier(String str) {		
		char ch = str.charAt(0);
		
		// check to see if ch is a letter, if so then see if the following charecters are leters or numbers
		if(Character.isLetter(ch) || ch == '_') {
			for(int i = 1; i < str.length(); i++) {
				ch = str.charAt(i);
				if(!Character.isLetter(ch) && ch != '_' && !isDigit(ch)) {
					// This is not an identifier 
					return false;
				}				
			}
			return true;
		}		
		return false;
	}
		
	/***
	 * Checks to see if the given input matches the pattern for a number.
	 * The pattern for number is: digit+
	 * @param str The input string that is being parsed which will be read one character at a time
	 * @return boolean indicating if it is a number
	 */
	public boolean isNumber(String str) {
		char ch = str.charAt(0);
		
		// if the first character is a digit 
		if(isDigit(ch)) {
			for(int i = 1; i < str.length(); i++) {
				ch = str.charAt(i);
				if(!isDigit(ch)) {
					// this is not a number
					return false;
				}
			}
			return true;
		}		
		return false;
	}
	
	/***
	 * Checks to see if the given input matches the pattern for a reserved word.
	 * reserved words are: int, void, if, while, return, read, write, print, continue, break, binary, decimal
	 * @param str The input string that is being parsed and compaired against the list of reserved words 
	 * @return String The reserve word if found or null 
	 */
	public String isReservedWord(String str) {
		// Iterate through the list of reserve words to see if the string equals any of them
		for(String word: reservedWords) {
			if(str.equals(word)) {
				// This is a reserved word 
				return word;
			}
		}
		// If no matches then not a reserved word
		return null;
	}
	
		
	/***
	 * Checks to see if the given input matches the pattern for a symbol
	 * @param str The input string that is being parsed and compaired against the list of symbols
	 * @return String returns the symbol or null
	 */
	public String isSymbol(String str) {
		// Iterate through the list of symbols to see if the string equals any of them
		for(String symbol: symbols) {
			if(str.equals(symbol)) {
				// This is a symbol
				return symbol;
			}
		}
		// if no matches then not a symbol
		return null;
	}
	
	/**
	 * Checks to see if the given input matches the pattern for a String
	 * The pattern for a string is that it starts and ends with quotations 
	 * @param str The input string that is being parsed
	 * @return boolean indicating if it is a string
	 */
	public boolean isString(String str) {
		char ch = str.charAt(0);
		char chEnd = str.charAt(str.length() -1);  // gets the last character in the string
		// Check to see if it starts with a quotation mark
		if(ch == '"' && str.length() != 1 && chEnd == '"') {
			// this is a string
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if the given input matches the pattern for a meta statement
	 * @param str The input string that is being parsed
	 * @return boolean indicating if it is a meta statement
	 */
	public boolean isMetaStatement(String str) {
		char ch = str.charAt(0);
		// Check to see if we are starting with a # and there is something after it
		if(ch == '#' && str.length() > 1) {
			// Check to see if we end with a newline
			if(str.charAt(str.length() -1) == '\n') {
				// this is a meta statement
				return true;
			}
				return false;
		}
		
		// Check to see if we start with //
		if(ch == '/' && str.length() > 1) {
			// if the second character is / and we end with newline
			if(str.charAt(1) == '/' && str.charAt(str.length() -1) == '\n') {
				return true;
			}
		}
		return false;
	}

}
