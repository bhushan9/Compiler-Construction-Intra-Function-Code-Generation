/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Node;

import Icg.*;

import Icg.RecursiveParsing;
import static Icg.Scanner.writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Bhushan
 */
 public class Node<T> implements Iterable<Node<T>> {
   //  static ArrayList<String> yolo=new ArrayList<String>();

   public String val;
   public T data;
   public Node<T> parent;
   
 
   public ArrayList<Node<T>> children;
   
     public void setVal(String s)
   {
   this.val=s;
   }
  

    public Node(T data) {
        //this.val = val;
        this.data =  data;
       // this.val= (String) data;
         this.children = new ArrayList<Node<T>>();
    }

    public Node<T> createNode(Node[] T) {
        for (int i = T.length - 1; i >= 1; i--) {
            T[i - 1].addChild(T[i]);
        }
        return T[0];
    }

    
    // adds a child to the parent
    public Node<T> addChild(T child) {
        Node<T> childNode = (Node<T>)child;
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    
    //return the list of child nodes
    public List<Node<T>> getChildren() {
        return this.children;
    }
    
    
    //used to build preorder traversal.
    public ArrayList<Node<T>> getPreOrderTraversal() {
        ArrayList<Node<T>> preOrder = new ArrayList<Node<T>>();
        buildPreOrder(RecursiveParsing.yo, preOrder);
        writer.write("Parsing Tree for the expression\n");
         for (int i = 0; i < preOrder.size(); i++) {
                        writer.write(preOrder.get(i).data+"<------ Non Terminal/ Terminal"+"\n");
			//System.out.println(preOrder.get(i).data+"<-----Pre");
		}
        return preOrder;
    }

     private void buildPreOrder(Node<T> node, ArrayList<Node<T>> preOrder) {
        preOrder.add(node);
        for (Node<T> child : node.getChildren()) {
           // System.out.println(child.data+"<--DATA");
            buildPreOrder(child, preOrder);
        }
    }
    
  
    // other features ...
    @Override
    public Iterator<Node<T>> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
