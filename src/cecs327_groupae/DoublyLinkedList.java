/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;


/**
 *
 * @author Minh
 */
 
public class DoublyLinkedList {
   
    int lengthDLL = 0;
    Node head = null;
    Node tail = null;
    
    
    public void addNode(String data)
    {
        Node newNode = new Node(data);
        lengthDLL++;
        if(head == null)
        {
            head = newNode;
            tail = newNode;
            
            head.prev = tail;
            tail.next = head;
        }
        else
        {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            tail.next = head;
            head.prev = tail;
        }
    }
    public void showData()
    {
        System.out.println("Display List Start");
        Node current = head;
        if(head==null)
        {
            System.out.println("List is empty");
            return;
        }
        for(int i = 0 ; i < lengthDLL ; i++)
        {
            System.out.println(current.data);
            current = current.next;
        }
        System.out.println("Display List End");
    }
    public Node getHead()
    {
        return head;
    }
    
}
