package Icg;
/**
 * This is the token class used to check the input against the RE and identify the correct token
 */

/**
 * @author Danny Reinheimer
 *
 */
public class Token {
	
	private RegularExpressions re;
	
	public Token() {
		this.re = new RegularExpressions();
	}
	
	/**
	 * Takes in the parsed input and checks it againts all the regular expressions with priority
	 * @param str the input string to test
	 * @return the name of the token
	 */
	public TokenNames getToken(String str) {
				
		// First check to see if it is meta statement
		if(re.isMetaStatement(str)) {
			return TokenNames.MetaStatements;
		}
		
		// check if input is one of the reserve words
		String word = null;
		if((word = re.isReservedWord(str)) != null) {
			if(word.equals("int"))	
				return TokenNames.Int;
			if(word.equals("void"))
				return TokenNames.Void;
			if(word.equals("binary"))
				return TokenNames.binary;
			if(word.equals("decimal"))
				return TokenNames.decimal;
			if(word.equals("read"))
				return TokenNames.read;
			if(word.equals("write"))
				return TokenNames.write;
			if(word.equals("print"))
				return TokenNames.print;
			if(word.equals("if"))
				return TokenNames.If;
			if(word.equals("while"))
				return TokenNames.While;
			if(word.equals("return"))
				return TokenNames.Return;
			if(word.equals("break"))
				return TokenNames.Break;
			if(word.equals("continue"))
				return TokenNames.Continue;
		}
		
				
		if(re.isIdentifier(str)) {
			return TokenNames.ID;
		}
		
		// checks to see if it matches one of the symbols
		String symbol = null;
		if((symbol = re.isSymbol(str)) != null) {
			if(symbol.equals(";"))
				return TokenNames.semicolon;
			if(symbol.equals("{"))
				return TokenNames.left_brace;
			if(symbol.equals("}"))
				return TokenNames.right_brace;
			if(symbol.equals("("))
				return TokenNames.left_parenthesis;
			if(symbol.equals(")"))
				return TokenNames.right_parenthesis;
			if(symbol.equals("]"))
				return TokenNames.right_bracket;
			if(symbol.equals("["))
				return TokenNames.left_bracket;
			if(symbol.equals("="))
				return TokenNames.equal_sign;
			if(symbol.equals("&&"))
				return TokenNames.double_and_sign;
			if(symbol.equals("||"))
				return TokenNames.double_or_sign;
			if(symbol.equals("=="))
				return TokenNames.doubleEqualSign;
			if(symbol.equals("!="))
				return TokenNames.notEqualSign;
			if(symbol.equals(">"))
				return TokenNames.greaterThenSign;
			if(symbol.equals(">="))
				return TokenNames.greaterThenOrEqualSign;
			if(symbol.equals("<"))
				return TokenNames.lessThenSign;
			if(symbol.equals("<="))
				return TokenNames.lessThenOrEqualSign;
			if(symbol.equals("+"))
				return TokenNames.plus_sign;
			if(symbol.equals("-"))
				return TokenNames.minus_sign;
			if(symbol.equals("*"))
				return TokenNames.star_sign;
			if(symbol.equals("/"))
				return TokenNames.forward_slash;
			if(symbol.equals(","))
				return TokenNames.comma;
		}
		
		
		if(re.isNumber(str)) {
			return TokenNames.NUMBER;
		}
		
		if(re.isString(str)) {
			return TokenNames.STRING;
		}
		
		if(re.isSpace(str)) {
			return TokenNames.Space;
		}
		
		return TokenNames.None;
	}

}
