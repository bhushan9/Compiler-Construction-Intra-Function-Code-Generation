<h2>IMPORTATNT </h2>
The project submitted is not a complete implementation of intraFunction code generator. Only some part of functionalities are implemented. 



<h2>Following are the functionalities implemented </h2>
1. Generation of parse tree of expression rule using Tree data structure. The parse tree is shown in the input_extra.txt file generated. <br>
2. Three address code generation using stack. This only works for algaebric expression having +,-,*,/ and parenthesis. This does not work for other expression which 
   included conditional operations such as &&,!!, hence it gives a runtime error for some of the test cases. For test cases which does not have conditional operations 
   the output is stored in the input_extra.txt <br>
3. If statement is transformed according to the given rules with the use of goto statements.   


<h2>The project makes use of the parser provided by the Professor on the project description website.</h2>


<h2>How to run the Project?</h2>
1. Extract the IntraFucntionTest folder in a working directory.
2. Set src as the working directory using cd command.
3. compile the scanner package using javac Icg/*.java
4. compile the st package using javac st/*.java
5. compile the Node package using javac Node/*.java
3. compile the threeaddcode package using javac threeaddcode/*.java
5. Run Parser.java using java Icg/Parser input_file.c   Where input_file.c is the name of test program.

<h2>Implementation Methodology</h2>

1. Parse tree is generated using the package Node. The class Node contains val, parent and list of children. Whenever the expression rule is called 
   we create root node named expression and add the productions as its children. Therefore according to our expression rule we have term and expression_prime as 
   the children of expression. Same mehodology is followed for other productions.
   
2. Three address code is generated in following manner:
   
   This is done with the help of a stack and operator precedence. First we evaluate the expression inside the parenthesis then we generate three address code for it.
   Then we add the final variable generated by the three address code into the stack and repeat the procedure till we get the final three address code. <br>
    
   If we have a expression such as a+(b*c)
   three address code is given by   t1=b*c, t2=a+t1 <br>  &nbsp
   
   Input       &nbsp           Stack       &nbsp        Action  
   a+(b*c)      &nbsp          $         &nbsp          input a <br>
   +(b*c)    &nbsp&nbsp             $a      &nbsp            input + <br>
   (b*c)       &nbsp           $a+    &nbsp             input ( <br>
    b*c)     &nbsp             $a+(      &nbsp          input b  <br>
    *c)         &nbsp          $a+(b     &nbsp          input * <br>
     c)           &nbsp        $a+(b*    &nbsp          input c <br>
      )         &nbsp          $a+(b*c    &nbsp         pop the character and store into temporary string t1 until we encounter '(' and push onto stack <br>
     )         &nbsp           $a+t1     &nbsp          discard )  <br>
     eof          &nbsp        $a+t1         &nbsp      Repeat the procedure until we get a single string in the end. which stores the value of the expression <br>
	

<h2>Difficulties faced in the project</h2>
1. The first problem that I encountered was difficulty in generating abstract syntax tree from the parse tree. I could not get rid of the non terminals in the parse 
   tree and hence had to abandon this approach <br>
2. The stack approach to generate TAC works only with the arithmatic operators and fails when comparison operator is encountered. Another problem was creating a
   symbol table and mapping it to the TAC generated variables and locally/globally defined variables.   
