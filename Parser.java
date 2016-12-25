package Icg;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * implements the main function that gets the file name and calls the scanner and parser 
 */

/**
 * @author Danny Reinheimer
 *
 */

public class Parser {

   public static String newFileName;
   
	
	/**
	 * starting point for the program
	 * @param args The file name to read in and parse
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// checks to see if we are given any arguments
		if(args.length < 1) {
			System.out.println("Please provide an input file to process");
			System.exit(0);
		}
		Vector<TokenNames> scannedTokens = new Vector<TokenNames>();
		// run initialize and run the scanner
		Scanner scanner = new Scanner(args[0]);
		scannedTokens = scanner.runScanner();
		// initialize and run the parser
                
                Vector<String> keyval=new Vector<String>();
                keyval=Scanner.keyval;
		RecursiveParsing RP = new RecursiveParsing(scannedTokens,keyval,args[0]);
		RP.parse();
                
                			
        

	}
	
	
	
	

}
