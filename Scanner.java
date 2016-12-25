package Icg;
import static Icg.RecursiveParsing.file;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class for the Scanner
 */

/**
 * @author Danny Reinheimer
 *
 */

public class Scanner {
     public static PrintWriter writer;
    public static PrintWriter writergen;
	
	private String fileName;
       static Vector<String> keyval=new Vector<String>();
	
	public Scanner(String fileName) {
		this.fileName = fileName;
	}

    public Scanner(InputStream in) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


	public Vector<TokenNames> runScanner() throws FileNotFoundException {
		// checks to see if we are given any arguments
//		if(args.length < 1) {
//			System.out.println("Please provide an input file to process");
//			System.exit(0);
//		}
		
		//String fileName = args[0];
		Scan scan = new Scan(fileName);
		Vector<TokenNames> outputTokens = new Vector<TokenNames>();
		Pair<TokenNames,String> tokenPair;
                
                 try {
                int pos = fileName.lastIndexOf(".");
			String newFileName = fileName.substring(0, pos) + "_extra.txt";
                        String newFile = fileName.substring(0, pos) + "_gen.c";
                 writer = new PrintWriter(newFileName,"UTF-8");
                  writergen = new PrintWriter(newFile,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
		
		
		// get the name of the file minus the dot 
//			int pos = fileName.lastIndexOf(".");
//			String newFileName = fileName.substring(0, pos) + "_gen.c";
//			PrintWriter writer = new PrintWriter(newFileName,"UTF-8");
		
		// keep getting the next token until we get a null
		while((tokenPair = scan.getNextToken()) != null) {
			if(tokenPair.getKey() != TokenNames.Space ) {
                            
                                if(tokenPair.getKey() == TokenNames.MetaStatements)
                                writergen.write(tokenPair.getValue()+" ");
                                else
                                {
				outputTokens.addElement(tokenPair.getKey());
                                keyval.add(tokenPair.getValue());
                                }
			}
                        
			
		}
		outputTokens.add(TokenNames.eof);
              //  keyval.add("eof");
                  keyval.add("eof");
                   
                  //   System.out.println(keyval.size());
                   //    System.out.println(outputTokens.size());
		
		return outputTokens;
		
		
		
		

	}

}
