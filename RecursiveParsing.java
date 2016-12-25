package Icg;

import static Icg.Scanner.writer;
import static Icg.Scanner.writergen;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import Node.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import threeaddcode.*;

/**
 * Implements the recursive decent parser
 */
/**
 * @author Danny Reinheimer
 *
 */
public class RecursiveParsing {

    public static Node yo;
    static String b = "C";
    static int block = 0;
    private static int numVariables;  // Keeps track of the number of variables 
    private static int numFunctions;  // Keeps track of the number of functions
    private static int numStatements; // Keeps track of the number of statements
    private static Vector<TokenNames> inputTokens; // Stores the set of input tokens 
    private static TokenNames currentToken;  // shows what the current token removed from the stack was for debug purposes 
    private static Vector<String> keyval1;
    private static Vector<String> output = new Vector<String>();
    private static Vector<String> func = new Vector<String>();
    // ArrayList<Node<T>> hey = new ArrayList<Node<T>>();
    ArrayList<String> expression_1 = new ArrayList<String>();
    static String exp = "";
    static String file;
   
    /**
     * Constructor initializes the fields and get the list of input tokens
     *
     * @param inputTokens1 @param keyval
     */
    public RecursiveParsing(Vector<TokenNames> inputTokens1, Vector<String> keyval, String filename) {
        numFunctions = 0;
        numVariables = 0;
        numStatements = 0;
        inputTokens = inputTokens1;
        currentToken = TokenNames.None;
        keyval1 = keyval;
        file=filename;
    }

    /**
     * initialized the parsing and prints out the results when finished
     */
    public void parse() throws FileNotFoundException {
        
        
        program();
        if (inputTokens.firstElement() == TokenNames.eof) {

            System.out.println("Pass variable " + numVariables + " function " + numFunctions + " statement " + numStatements);
        } else {
            System.out.println("error");
        }
        
        Enumeration vEnum = output.elements();
       // System.out.println("\nElements in vector:");
        while (vEnum.hasMoreElements()) {
            Object temp=vEnum.nextElement();
            writergen.write(temp + " ");
           // System.out.print(temp + "\n");
        }

        
        // System.out.println(exp);
       
        Node n = new Node("dummy");
           
         n.getPreOrderTraversal();
        //n.getPostOrderTraversal();

        writer.close();
        writergen.close();
    }

    /**
     * <program> --> <type name> ID <data decls> <func list> | empty
     *
     * @return A boolean indicating pass or error
     */
    private boolean program() {
        // check if we are at the eof
        if (inputTokens.firstElement() == TokenNames.eof) {

            return true;
        } else if (type_name()) {

            if (inputTokens.firstElement() == TokenNames.ID) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0); // get the ID token
                if (data_decls() && func_list()) {
                    //check to see if the remaining token is eof is so this is a legal syntax
                    if (inputTokens.firstElement() == TokenNames.eof) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <func list> --> empty | left_parenthesis <parameter list>
     * right_parenthesis <func Z> <func list Z>
     *
     * @return A boolean indicating if the rule passed or failed
     */
    private boolean func_list() {
        if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (parameter_list()) {
                //output.add(keyval1.firstElement());
                // keyval1.remove(0);
                if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    if (func_Z()) {
                        return func_list_Z();
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return true;
    }

    /**
     * <func Z> --> semicolon | left_brace <data decls Z> <statements>
     * right_brace
     *
     * @return A boolean indicating if the rule passed or failed
     */
    private boolean func_Z() {
        // checks if the next token is a semicolon
        if (inputTokens.firstElement() == TokenNames.semicolon) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0); // remove the token from the stack
            return true;
        }

        if (inputTokens.firstElement() == TokenNames.left_brace) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (data_decls_Z()) {
                if (statements()) {
                    if (inputTokens.firstElement() == TokenNames.right_brace) {
                        output.add(keyval1.firstElement());
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        // Count the number of function definitions
                        numFunctions += 1;
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    /**
     * <func list Z> --> empty | <type name> ID left_parenthesis
     * <parameter list> right_parenthesis <func Z> <func list Z>
     *
     * @return a boolean
     */
    private boolean func_list_Z() {
        if (type_name()) {
            if (inputTokens.firstElement() == TokenNames.ID) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    if (parameter_list()) {
                        if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                            output.add(keyval1.firstElement());
                            keyval1.remove(0);
                            currentToken = inputTokens.remove(0);
                            if (func_Z()) {
                                return func_list_Z();
                            }
                        }
                    }
                }
            }
            return false;
        }
        // return true for the empty rule
        return true;
    }

    /**
     * <type name> --> int | void | binary | decimal
     *
     * @return A boolean indicating if the rule passed or failed
     */
    private boolean type_name() {
        if (inputTokens.firstElement() == TokenNames.Int || inputTokens.firstElement() == TokenNames.Void
                || inputTokens.firstElement() == TokenNames.binary || inputTokens.firstElement() == TokenNames.decimal) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return true;
        }
        return false;
    }

    /**
     * <parameter list> --> empty | void <parameter list Z> | <non-empty list>
     *
     * @return a boolean
     */
    private boolean parameter_list() {
        // void <parameter list Z>
        if (inputTokens.firstElement() == TokenNames.Void) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return parameter_list_Z();
        } // <non-empty list>
        else if (non_empty_list()) {
            return true;
        }
        // empty
        return true;
    }

    /**
     * <parameter list Z> --> empty | ID <non-empty list prime>
     *
     * @return a boolean
     */
    private boolean parameter_list_Z() {
        if (inputTokens.firstElement() == TokenNames.ID) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return non_empty_list_prime();
        }
        return true;
    }

    /**
     * <non-empty list> --> int ID <non-empty list prime> | binary ID
     * <non-empty list prime> | decimal ID <non-empty list prime>
     *
     * @return a boolean
     */
    private boolean non_empty_list() {
        // check for int, binary, decimal
        if (inputTokens.firstElement() == TokenNames.Int || inputTokens.firstElement() == TokenNames.binary
                || inputTokens.firstElement() == TokenNames.decimal) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.ID) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                return non_empty_list_prime();
            }
        }
        return false;
    }

    /**
     * <non-empty list prime> --> comma <type name> ID <non-empty list prime> |
     * empty
     *
     * @return a boolean
     */
    private boolean non_empty_list_prime() {
        if (inputTokens.firstElement() == TokenNames.comma) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (type_name()) {
                if (inputTokens.firstElement() == TokenNames.ID) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    return non_empty_list_prime();
                }
                return false;
            }
            return false;
        }
        return true;
    }

    /**
     * <data decls> --> empty | <id list Z> semicolon <program> |
     * <id list prime> semicolon <program>
     *
     * @return a boolean
     */
    private boolean data_decls() {
        if (id_list_Z()) {
            if (inputTokens.firstElement() == TokenNames.semicolon) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                // count variable 
                numVariables += 1;
                return program(); //data_decls_Z();
            }
            return false;
        }
        if (id_list_prime()) {
            if (inputTokens.firstElement() == TokenNames.semicolon) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                // since we consume the first id before we get here count this as a variable
                numVariables += 1;
                return program(); //data_decls_Z();
            }
            //return false;
        }
        return true;
    }

    /**
     * <data decls Z> --> empty | int <id list> semicolon <data decls Z> | void
     * <id list> semicolon <data decls Z> | binary <id list> semicolon
     * <data decls Z> | decimal <id list> semicolon <data decls Z>
     *
     * @return A boolean indicating if the rule passed or failed
     */
    private boolean data_decls_Z() {
        if (type_name()) {
            if (id_list()) {
                if (inputTokens.firstElement() == TokenNames.semicolon) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    return data_decls_Z();
                }
                return false;
            }
            return false;
        }
        return true;
    }

    /**
     * <id list> --> <id> <id list prime>
     *
     * @return a boolean
     */
    private boolean id_list() {
        if (id()) {
            return id_list_prime();
        }
        return false;
    }

    /**
     * <id list Z> --> left_bracket <expression> right_bracket <id list prime>
     *
     * @return a boolean indicating if the rule passed or failed
     */
    private boolean id_list_Z() {
        if (inputTokens.firstElement() == TokenNames.left_bracket) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (expression("").data != "") {
                String z = Trial.T(exp);
               // System.out.println("!!!!!!!!!!   " + z);
                exp = "";
                // System.out.println(exp+"haha");
                if (inputTokens.firstElement() == TokenNames.right_bracket) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    return id_list_prime();
                }
            }
        }
        return false;
    }

    /**
     * <id list prime> --> comma <id> <id list prime> | empty
     *
     * @return a boolean indicating if the rule passed or failed
     */
    private boolean id_list_prime() {
        if (inputTokens.firstElement() == TokenNames.comma) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (id()) {
                return id_list_prime();
            }
            return false;
        }
        return true;
    }

    /**
     * <id> --> ID <id Z>
     *
     * @return a boolean
     */
    private boolean id() {
        if (inputTokens.firstElement() == TokenNames.ID) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return id_Z();
        }
        return false;
    }

    /**
     * <id Z> --> left_bracket <expression> right_bracket | empty
     *
     * @return a boolean
     */
    private boolean id_Z() {
        if (inputTokens.firstElement() == TokenNames.left_bracket) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (expression("").data != "") {
                String z = Trial.T(exp);
               // System.out.println("!!!!!!!!!!   " + z);
                exp = "";
                // System.out.println(exp+"haha");
                if (inputTokens.firstElement() == TokenNames.right_bracket) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    // count the number of variables 
                    numVariables += 1;
                    return true;
                }
                return false;
            }
            return false;
        }
        // count the number of variables 
        numVariables += 1;
        return true;
    }

    /**
     * <block statements> --> left_brace <statements> right_brace
     *
     * @return a boolean
     */
    private boolean block_statements(int n) {
        if (inputTokens.firstElement() == TokenNames.left_brace) {
            /*
            if(n==1)
            {}
            else
            {
            output.add(keyval1.firstElement());
             keyval1.remove(0);
            }
             */
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (statements()) {
                if (inputTokens.firstElement() == TokenNames.right_brace) {

                    /*  if(n==1)
                    {}
                    else
                    {
                     output.add(keyval1.firstElement());
             keyval1.remove(0);}
                     */
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <statements> --> empty | <statement> <statements>
     *
     * @return a boolean
     */
    private boolean statements() {
        if (statement()) {
            numStatements += 1;
            return statements();
        }
        return true;
    }

    /**
     * <statement> --> ID <statement Z> | <if statement> | <while statement> |
     * <return statement> | <break statement> | <continue statement> | read
     * left_parenthesis ID right_parenthesis semicolon | write left_parenthesis
     * <expression> right_parenthesis semicolon | print left_parenthesis STRING
     * right_parenthesis semicolon
     *
     * @return a boolean indicating if the rule passed or failed
     */
    private boolean statement() {
        if (inputTokens.firstElement() == TokenNames.ID) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return statement_Z();
        }
        if (if_statement()) {
            return true;
        }
        if (while_statement()) {
            return true;
        }
        if (return_statement()) {
            return true;
        }
        if (break_statement()) {
            return true;
        }
        if (continue_statement()) {
            return true;
        }
        if (inputTokens.firstElement() == TokenNames.read) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                if (inputTokens.firstElement() == TokenNames.ID) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                        output.add(keyval1.firstElement());
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        if (inputTokens.firstElement() == TokenNames.semicolon) {
                            output.add(keyval1.firstElement());
                            keyval1.remove(0);
                            currentToken = inputTokens.remove(0);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        // write left_parenthesis <expression> right_parenthesis semicolon
        if (inputTokens.firstElement() == TokenNames.write) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                if (expression("").data != "") {
                    String z = Trial.T(exp);
                   // System.out.println("!!!!!!!!!!   " + z);
                    exp = "";

                    if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                        //  System.out.println(exp+"hahalolol");
                        output.add(keyval1.firstElement());
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        if (inputTokens.firstElement() == TokenNames.semicolon) {
                            output.add(keyval1.firstElement());
                            keyval1.remove(0);
                            currentToken = inputTokens.remove(0);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        // print left_parenthesis  STRING right_parenthesis semicolon
        if (inputTokens.firstElement() == TokenNames.print) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                if (inputTokens.firstElement() == TokenNames.STRING) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                        output.add(keyval1.firstElement());
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        if (inputTokens.firstElement() == TokenNames.semicolon) {
                            output.add(keyval1.firstElement());
                            keyval1.remove(0);
                            currentToken = inputTokens.remove(0);
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * <statement Z> --> <assignment Z> | <func call>
     *
     * @return a boolean indicating if the rule passed or failed
     */
    private boolean statement_Z() {
        if (assignment_Z()) {
            return true;
        } else if (func_call()) {
            return true;
        }
        return false;
    }

    /**
     * <assignment Z> --> equal_sign <expression> semicolon | left_bracket
     * <expression> right_bracket equal_sign <expression> semicolon
     *
     * @return a boolean
     */
    private boolean assignment_Z() {
        if (inputTokens.firstElement() == TokenNames.equal_sign) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (expression("").data != "") {
                String z = Trial.T(exp);
               // System.out.println("!!!!!!!!!!   " + z);
                exp = "";
                // System.out.println(exp+"haha");

                if (inputTokens.firstElement() == TokenNames.semicolon) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    return true;
                }
            }
            return false;
        }
        if (inputTokens.firstElement() == TokenNames.left_bracket) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (expression("").data != "") {
                String z = Trial.T(exp);
               // System.out.println("!!!!!!!!!!   " + z);
                exp = "";
                //System.out.println(exp+"haha");

                if (inputTokens.firstElement() == TokenNames.right_bracket) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    if (inputTokens.firstElement() == TokenNames.equal_sign) {
                        output.add(keyval1.firstElement());
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        if (expression("").data != "") {
                            z = Trial.T(exp);
                            //System.out.println("!!!!!!!!!!   " + z);
                            exp = "";
                            //System.out.println(exp+"haha");

                            if (inputTokens.firstElement() == TokenNames.semicolon) {
                                output.add(keyval1.firstElement());
                                keyval1.remove(0);
                                currentToken = inputTokens.remove(0);
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     * <func call> --> left_parenthesis <expr list> right_parenthesis semicolon
     *
     * @return a boolean
     */
    private boolean func_call() {
        if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (expr_list().data != "") {
                if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                    output.add(keyval1.firstElement());
                    keyval1.remove(0);
                    currentToken = inputTokens.remove(0);
                    if (inputTokens.firstElement() == TokenNames.semicolon) {
                        output.add(keyval1.firstElement());
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <expr list> --> empty | <non-empty expr list>
     *
     * @return a boolean
     */
    private Node expr_list() {
        Node array[] = new Node[2];
        Node expr_list = new Node("expr_list");
        Vector v = new Vector();
        v.add(expr_list);
        array[0] = expr_list;
        Node n = non_empty_expr_list();
        if (n.data != "") {
            v.add(n);
            array[1] = n;
            return expr_list.createNode(array);
        }
        return expr_list;
    }

    /**
     * <non-empty expr list> --> <expression> <non-empty expr list prime>
     *
     * @return a boolean
     */
    private Node non_empty_expr_list() {
        Node array[] = new Node[3];
        Vector v = new Vector();
        Node non_empty_expr_list = new Node("non_empty_expr_list");
        array[0] = non_empty_expr_list;
        Node n = expression("");
        if (n.data != "") {
            //System.out.println(exp+"haha");

            array[1] = n;
            v.add(n);
            Node n1 = non_empty_expr_list_prime();
            if (n1.data != "") {
                array[2] = n1;
                return non_empty_expr_list.createNode(array);
            }
        }
        non_empty_expr_list.data = "";
        return non_empty_expr_list;
    }

    /**
     * <non-empty expr list prime> --> comma <expression>
     * <non-empty expr list prime> | empty
     *
     * @return a boolean
     */
    private Node non_empty_expr_list_prime() {
        Node array[] = new Node[4];
        Vector v = new Vector();
        Node non_empty_expr_list_prime = new Node("non_empty_expr_list_prime");
        array[0] = non_empty_expr_list_prime;
        v.add(non_empty_expr_list_prime);
        if (inputTokens.firstElement() == TokenNames.comma) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            Node n = new Node(inputTokens.firstElement() + "");
            array[1] = n;
            v.add(n);
            currentToken = inputTokens.remove(0);
            Node n1 = expression("");
            if (n1.data != "") {
                // System.out.println(exp+"haha");

                v.add(n1);
                array[2] = n1;
                v.add(non_empty_expr_list_prime());
                array[3] = non_empty_expr_list_prime();
                return non_empty_expr_list_prime.createNode(array);
            }
            non_empty_expr_list_prime.data = "";
            return non_empty_expr_list_prime;
        }
        return non_empty_expr_list_prime;
    }

    /**
     * <if statement> --> if left_parenthesis <condition expression>
     * right_parenthesis <block statements>
     *
     * @return a boolean
     */
    private boolean if_statement() {
        if (inputTokens.firstElement() == TokenNames.If) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                if (condition_expression()) {
                    if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                        //  System.out.println(inputTokens.firstElement()+"zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
                        output.add(keyval1.firstElement() + "");
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        output.add("goto " + b + block + ";" + ""); //
                        block++;
                        output.add("goto " + b + block + ";"); // 
                        output.add("" + b + (block - 1) + ":;"); // 
                        if (block_statements(1)) {
                            output.add("" + b + (block) + ":;"); // 
                            block++;
                            return true;
                        } else {
                            return false;
                        }
                        //return block_statements();
                    }
                }
            }
        }
        return false;
    }

    /**
     * <condition expression> -->  <condition> <condition expression Z>
     *
     * @return a boolean
     */
    private boolean condition_expression() {
        if (condition()) {
            return condition_expression_Z();
        }
        return false;
    }

    /**
     * <condition expression Z> --> <condition op> <condition> | empty
     *
     * @return a boolean
     */
    private boolean condition_expression_Z() {
        if (condition_op()) {

            return condition();
        }
        return true;
    }

    /**
     * <condition op> --> double_end_sign | double_or_sign
     *
     * @return a boolean
     */
    private boolean condition_op() {
        if (inputTokens.firstElement() == TokenNames.double_and_sign || inputTokens.firstElement() == TokenNames.double_or_sign) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return true;
        }
        return false;
    }

    /**
     * <condition> --> <expression> <comparison op> <expression>
     *
     * @return a boolean
     */
    private boolean condition() {
        if (expression("").data != "") {
            String z = Trial.T(exp);
            //System.out.println("!!!!!!!!!!   " + z);
            exp = "";
            if (comparison_op()) {
                Node n = expression("");
                if (n.data != "") {
                    //System.out.println(exp+"haha");

                    return true;
                } else {
                    return false;
                }

            }
        }
        return false;
    }

    /**
     * <comparison op> --> == | != | > | >= | < | <=
     *
     * @return a boolean
     */
    private boolean comparison_op() {
        if (inputTokens.firstElement() == TokenNames.doubleEqualSign || inputTokens.firstElement() == TokenNames.notEqualSign
                || inputTokens.firstElement() == TokenNames.greaterThenSign || inputTokens.firstElement() == TokenNames.greaterThenOrEqualSign
                || inputTokens.firstElement() == TokenNames.lessThenSign || inputTokens.firstElement() == TokenNames.lessThenOrEqualSign) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return true;
        }
        return false;
    }

    /**
     * <while statement> --> while left_parenthesis <condition expression>
     * right_parenthesis <block statements>
     *
     * @return
     */
    private boolean while_statement() {
        if (inputTokens.firstElement() == TokenNames.While) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                if (condition_expression()) {
                    if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                        output.add(keyval1.firstElement());
                        keyval1.remove(0);
                        currentToken = inputTokens.remove(0);
                        return block_statements(1);
                    }
                }
            }
        }
        return false;
    }

    /**
     * <return statement> --> return <return statement Z>
     *
     * @return a boolean
     */
    private boolean return_statement() {
        if (inputTokens.firstElement() == TokenNames.Return) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return return_statement_Z();
        }
        return false;
    }

    /**
     * <return statement Z> --> <expression> semicolon | semicolon
     *
     * @return a boolean
     */
    private boolean return_statement_Z() {
        if (expression("").data != "") {
//            System.out.println(exp+"haha");
//            ThreeAddCode.codegen(exp);
            //System.out.println(exp+"temp");
// System.out.println(exp.length()+"!!!!!!!!");
            String z = Trial.T(exp);
            exp = "";
            System.out.println("!!!!!!!!!!   " + z);

            if (inputTokens.firstElement() == TokenNames.semicolon) {

                output.add(keyval1.firstElement());
                keyval1.remove(0);

                currentToken = inputTokens.remove(0);
                return true;
            }
            return false;
        }
        if (inputTokens.firstElement() == TokenNames.semicolon) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            return true;
        }
        return false;
    }

    /**
     * <break statement> ---> break semicolon
     *
     * @return a boolean
     */
    private boolean break_statement() {
        if (inputTokens.firstElement() == TokenNames.Break) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.semicolon) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                return true;
            }
        }
        return false;
    }

    /**
     * <continue statement> ---> continue semicolon
     *
     * @return a boolean
     */
    private boolean continue_statement() {
        if (inputTokens.firstElement() == TokenNames.Continue) {
            output.add(keyval1.firstElement());
            keyval1.remove(0);
            currentToken = inputTokens.remove(0);
            if (inputTokens.firstElement() == TokenNames.semicolon) {
                output.add(keyval1.firstElement());
                keyval1.remove(0);
                currentToken = inputTokens.remove(0);
                return true;
            }
        }
        return false;
    }

    /**
     * <expression> --> <term> <expression prime>
     *
     * @return a boolean
     */
    private Node expression(String s) {

        Node expression = new Node("expression");
        expression.setVal("expression");

        // System.out.println(expression.val+"zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        // String temp=s;
        Node term = term(s);
        if (term.data != "") {
            //System.out.println("term kda data"+term.data);
            // v.add(term);
            expression.addChild(term);
            //System.out.println("!!!!!----"+((Node)expression.getChildren().get(0)).data);

            Node expression_prime = expression_prime(s);

            if (expression_prime.data != "") {
                // System.out.println("exp_prime kda data"+expression_prime.data);
                expression.addChild(expression_prime);
                // ThreeAddCode.codegen(expression_1.toArray());
                /* if(!"(".equals(output.get(output.size()-2)))
                {
               System.out.println(output.get(output.size()-2)+"yolo");
              System.out.println("exp"+exp);
               exp="";
                       } */
                // temp=exp;
                // System.out.println(temp+"lol");
                // temp="";

                //System.out.println(exp+"baba");
                // temp=exp;
                // exp="";
                // exp="";
                yo = expression;
                return yo;

            }
        }
        expression.data = "";
        //System.out.println("exp"+exp);
        return expression;
    }

    /**
     * <expression prime> --> <addop> <term> <expression prime> | empty
     *
     * @return
     */
    private Node expression_prime(String s) {

        Node expression_prime = new Node("expression_prime");
        expression_prime.setVal("expression_prime");

        Node addop = addop(s);
        if (addop.data != "") {
            expression_prime.addChild(addop);

            Node term = term(s);
            if (term.data != "") {

                expression_prime.addChild(term);
                Node n = expression_prime(s);
                if (n.data != "") {
                    expression_prime.addChild(n);
                    return expression_prime;
                }
            }
            expression_prime.data = "";
            return expression_prime;
        }
        return expression_prime;
    }

    /**
     * <addop> --> plus_sign | minus_sign
     *
     * @return a boolean
     */
    private Node addop(String s) {

        Node addop = new Node("addop");
        addop.setVal("addop");

        if (inputTokens.firstElement() == TokenNames.plus_sign || inputTokens.firstElement() == TokenNames.minus_sign) {
            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            //System.out.println(keyval1.firstElement()+"<-----");
            Node n = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n.setVal(inputTokens.firstElement() + "");
            addop.addChild(n);
            currentToken = inputTokens.remove(0);

            return addop;
        }
        addop.data = "";
        return addop;
    }

    /**
     * <term> --> <factor> <term prime>
     *
     * @return a boolean
     */
    private Node term(String s) {

        Node term = new Node("term");
        term.setVal("term");

        Node n = factor(s);
        // n.setVal("factor");
        if (n.data != "") {
            term.addChild(n);

            Node n1 = term_prime(s);

            if (n1.data != "") {
                term.addChild(n1);

                return term;
            }
        }
        term.data = "";
        return term;
    }

    /**
     * <term prime> --> <mulop> <factor> <term prime> | empty
     *
     * @return
     */
    private Node term_prime(String s) {

        Node term_prime = new Node("term_prime");
        term_prime.setVal("term_prime");

        Node n = mulop(s);

        if (n.data != "") {
            term_prime.addChild(n);

            Node n1 = factor(s);

            if (n1.data != "") {
                term_prime.addChild(n1);
                Node n2 = term_prime(s);
                if (n2.data != "") {
                    term_prime.addChild(n2);
                    return term_prime;

                }
            }
            term_prime.data = "";
            return term_prime;
        }
        return term_prime;
    }

    /**
     * <mulop> --> star_sign | forward_slash
     *
     * @return a boolean
     */
    private Node mulop(String s) {

        Node mulop = new Node("mulop");
        mulop.setVal("mulop");

        if (inputTokens.firstElement() == TokenNames.star_sign || inputTokens.firstElement() == TokenNames.forward_slash) {
            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            Node n = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n.setVal(inputTokens.firstElement() + "");
            mulop.addChild(n);
            //   v.add(n);
            currentToken = inputTokens.remove(0);
            return mulop;
        }
        mulop.data = "";
        return mulop;
    }

    /**
     * <factor> --> ID <factor Z> | NUMBER | minus_sign NUMBER |
     * left_parenthesis <expression>right_parenthesis
     *
     * @return
     */
    private Node factor(String s) {

        Node factor = new Node("factor");
        factor.setVal("factor");

        if (inputTokens.firstElement() == TokenNames.ID) {
             // Node n1=new Node("ID");
             // factor.addChild(n1);
            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            Node n = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n.setVal(inputTokens.firstElement() + "");
            factor.addChild(n);
            currentToken = inputTokens.remove(0);

            Node n2 = factor_Z(s);
            if (n2.data != "") {
                factor.addChild(n2);
                return factor;
            }
        }
        // NUMBER
        if (inputTokens.firstElement() == TokenNames.NUMBER) {
            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            Node n = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n.setVal(inputTokens.firstElement() + "");
            factor.addChild(n);
            currentToken = inputTokens.remove(0);
            return factor;
        }

        // minus_sign NUMBER
        if (inputTokens.firstElement() == TokenNames.minus_sign) {
            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            Node n = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n.setVal(inputTokens.firstElement() + "");
            factor.addChild(n);
            currentToken = inputTokens.remove(0);

            if (inputTokens.firstElement() == TokenNames.NUMBER) {
                output.add(keyval1.firstElement());
                expression_1.add(keyval1.firstElement());
                exp = exp + keyval1.firstElement();

                Node n1 = new Node(keyval1.firstElement() + "");
                keyval1.remove(0);
                n1.setVal(inputTokens.firstElement() + "");
                factor.addChild(n1);
                currentToken = inputTokens.remove(0);
                return factor;
            }
            factor.data = "";
            return factor;
        }

        // left_parenthesis <expression>right_parenthesis
        if (inputTokens.firstElement() == TokenNames.left_parenthesis) {

            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            Node n1 = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n1.setVal(inputTokens.firstElement() + "");
            factor.addChild(n1);
            currentToken = inputTokens.remove(0);
            Node n2 = expression("");  //zhol
            if (n2.data != "") {

                //System.out.println(exp+"haha");
                factor.addChild(n2);
                if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                    output.add(keyval1.firstElement());
                    expression_1.add(keyval1.firstElement());
                    exp = exp + keyval1.firstElement();

                    Node n3 = new Node(keyval1.firstElement() + "");
                    keyval1.remove(0);
                    n3.setVal(inputTokens.firstElement() + "");
                    factor.addChild(n3);
                    currentToken = inputTokens.remove(0);
                    return factor;
                }
            }
            factor.data = "";
            return factor;
        }
        factor.data = "";
        return factor;
    }

    /**
     * <factor Z> --> left_bracket <expression> right_bracket | left_parenthesis
     * <expr list> right_parenthesis | empty
     *
     * @return
     */
    private Node factor_Z(String s) {
        // left_bracket <expression> right_bracket

        Node factor_Z = new Node("factor_Z");
        factor_Z.setVal("factor_Z");
        if (inputTokens.firstElement() == TokenNames.left_bracket) {
            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            Node n = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n.setVal(inputTokens.firstElement() + "");
            factor_Z.addChild(n);

            currentToken = inputTokens.remove(0);
            Node n1 = expression("");
            if (n1.data != "") {
                //System.out.println(exp+"haha");

                factor_Z.addChild(n1);

                if (inputTokens.firstElement() == TokenNames.right_bracket) {
                    output.add(keyval1.firstElement());
                    expression_1.add(keyval1.firstElement());
                    exp = exp + keyval1.firstElement();

                    Node n2 = new Node(keyval1.firstElement() + "");
                    keyval1.remove(0);
                    n2.setVal(inputTokens.firstElement() + "");
                    factor_Z.addChild(n2);

                    currentToken = inputTokens.remove(0);
                    return factor_Z;
                }
            }
            factor_Z.data = "";
            return factor_Z;
        }
        // left_parenthesis <expr list> right_parenthesis
        if (inputTokens.firstElement() == TokenNames.left_parenthesis) {
            output.add(keyval1.firstElement());
            expression_1.add(keyval1.firstElement());
            exp = exp + keyval1.firstElement();

            Node n3 = new Node(keyval1.firstElement() + "");
            keyval1.remove(0);
            n3.setVal(inputTokens.firstElement() + "");
            factor_Z.addChild(n3);

            currentToken = inputTokens.remove(0);
            Node n4 = expr_list();
            if (n4.data != "") {

                factor_Z.addChild(n4);
                if (inputTokens.firstElement() == TokenNames.right_parenthesis) {
                    output.add(keyval1.firstElement());
                    expression_1.add(keyval1.firstElement());
                    exp = exp + keyval1.firstElement();

                    Node n5 = new Node(keyval1.firstElement() + "");
                    keyval1.remove(0);
                    n5.setVal(inputTokens.firstElement() + "");

                    factor_Z.addChild(n5);

                    currentToken = inputTokens.remove(0);
                    return factor_Z;
                }
            }

            factor_Z.data = "";

            return factor_Z;
        }
        // empty
        return factor_Z;
    }

}
