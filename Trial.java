/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threeaddcode;

import java.util.Scanner;
import java.util.Stack;

import st.*;

/**
 *
 * @author Bhushan
 */
public class Trial {
    
    
    public static String T(String args)
    {
        Scanner sc=new Scanner(System.in);
        //String s=sc.nextLine();
           String s=args;
        Stack<String> st=new Stack();
        st.add("$");
        char exp[]=s.toCharArray();
        StringBuilder sb=new StringBuilder();
        
        for(int i=0;i<exp.length;i++)
        {
         String temp="";
        
        
        
       
            if(exp[i]==')')
            {
               
               while(!st.peek().equals("("))
               {
                 String c=st.pop();
                 //System.out.println(c);
                 temp=c+temp;
                
               }
              
               st.pop();  // to remove the left_parenthesis
               
               // evaluate the sub expression and generate the three address code
               String var=ThreeAddCode.codegen(temp);
                // add the final variable generated by three address code into the stack.
               st.add(var);  
              
            
            
            }
            
            
            else
            st.add(exp[i]+"");
            
            
        
        
        
        
            
        }
       // System.out.println(st);
        
        String temp="",rev;
        
        while(!st.peek().equals("$"))
        {
        
          String c=st.pop();
           //StringBuilder sb=new StringBuilder(c);
           temp=c+temp;
                 //System.out.println(c);
                 sb.insert(0,c);
        
        }
      
        String var=ThreeAddCode.codegen(temp);
     
        
        return var;
            
    
    }
    
    
    
    
}
