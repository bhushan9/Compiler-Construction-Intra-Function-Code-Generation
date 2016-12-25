/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threeaddcode;

/**
 *
 * @author Bhushan
 */

import java.io.*;
import java.util.*;
import st.*;
import Icg.*;
import static Icg.Scanner.writer;

public class ThreeAddCode
{
   static char tc;
    static String symbol;
	private static final char[][] precedence = {
                {'(','1' },
                {')','1'},
		{'/', '1'},
		{'*', '1'},
		{'+', '2'},
		{'-', '2'}
	};
	
	private static int precedenceOf(String t)
	{
		char token = t.charAt(0);
		for (int i=0; i < precedence.length; i++)
		{
			if (token == precedence[i][0])
			{
				return Integer.parseInt(precedence[i][1]+"");
			}
		}
		return -1;
	}
	
	public static String codegen(String args) 
	{
		int i, j, opc=0;
		char token;
		boolean processed[];
		String[][] operators = new String[10][2];
		String expr="", temp;
                //Scanner sc=new Scanner(System.in);
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		//System.out.print("\nEnter an expression: ");
               // expr=sc.nextLine();
		expr = args;
              //  System.out.println(expr.length()+">>>>>>>>");
		processed = new boolean[expr.length()];
		for (i=0; i < processed.length; i++)
		{
			processed[i] = false;
		}
		for (i=0; i < expr.length(); i++)
		{
                        
			token = expr.charAt(i);
			for (j=0; j < precedence.length; j++)
			{
				if (token==precedence[j][0])
				{
					operators[opc][0] = token+"";
					operators[opc][1] = i+"";
					opc++;
					break;
				}
			}
		}
		//System.out.println("\nOperators:\nOperator\tLocation");
		for (i=0; i < opc; i++)
		{
			//System.out.println(operators[i][0] + "\t\t" + operators[i][1]);
		}
		//sort
		for (i=opc-1; i >= 0; i--)
		{
			for (j=0; j < i; j++)
			{
				if (precedenceOf(operators[j][0]) > precedenceOf(operators[j+1][0]))
				{
					temp = operators[j][0];
					operators[j][0] = operators[j+1][0];
					operators[j+1][0] = temp;
					temp = operators[j][1];
					operators[j][1] = operators[j+1][1];
					operators[j+1][1] = temp;
				}				
			}
		}
		//System.out.println("\nOperators sorted in their precedence:\nOperator\tLocation");
		for (i=0; i < opc; i++)
		{
			//System.out.println(operators[i][0] + "\t\t" + operators[i][1]);
		}
		//System.out.println();
                
                writer.write("<-----Three address code for expression----->\n");
		for (i=0; i < opc; i++)
		{
			j = Integer.parseInt(operators[i][1]+"");
			String op1="", op2="";
			if (processed[j-1]==true)
			{
				if (precedenceOf(operators[i-1][0]) == precedenceOf(operators[i][0]))
				{
					op1 = "t"+i;
				}
				else
				{
					for (int x=0; x < opc; x++)
					{
						if ((j-2) == Integer.parseInt(operators[x][1]))
						{
							op1 = "t"+(x+1)+"";
						}
					}
				}
			}
			else
			{
				op1 = expr.charAt(j-1)+"";
			}
			if (processed[j+1]==true)
			{
				for (int x=0; x < opc; x++)
				{
					if ((j+2) == Integer.parseInt(operators[x][1]))
					{
						op2 = "t"+(x+1)+"";
					}
				}
			}
			else
			{
				op2 = expr.charAt(j+1)+"";
			}
			
                         tc=(char) (65 + (int)(Math.random() * ((90 - 65) + 1)));
                         symbol=tc+"";
                         
                        //System.out.println("t"+(i+1)+" = "+op1+operators[i][0]+op2);
                        writer.write("t"+(i+1)+" = "+op1+operators[i][0]+op2);
                       // SymbolTable.symbol_local.put(tc,op1+operators[i][0]+op2);
			processed[j] = processed[j-1] = processed[j+1] = true;
		}
                //System.out.println("t"+opc+"haha");
                 writer.write("\n");
                return symbol;
	}
}