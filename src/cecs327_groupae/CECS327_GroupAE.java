/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.util.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.NetworkInterface;
import java.lang.Thread;
import java.nio.channels.CompletionHandler;
import java.io.File;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Michael
 */
public class CECS327_GroupAE {

    public static final String PORT = "9000", DIRECTORY_PATH = "C:/cecs327";
    private static final Path path = Paths.get(DIRECTORY_PATH);
    private static File[] files;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //the ip address of the previous and next nodes in the doubly linked list
        ArrayList<String> prevNextNodes = new ArrayList<String>();
        prevNextNodes.add(""); //1st is prev
        prevNextNodes.add(""); //2nd is next
        ArrayList<Socket> nodes; //array of sockets of nodes in the network
        
        /*Scanner scan = new Scanner(System.in);
        String path = "C:/cecs327/test.txt";
        System.out.print("1. Setup Server\n");
        System.out.print("2. Setup Client\n");
        System.out.print("3. Make Folder\n");
        System.out.print("4. DoublyLinkedList\n");

        try {
            String userInput = scan.nextLine();

            FindIpAddresses findIps = new FindIpAddresses();
            nodes = findIps.getNodes();

            if (userInput.equals("1")) {
                SocketServer socketServer = new SocketServer(nodes.get(0), PORT, SERVER_FILE_PATH);
                socketServer.start();
            } else if (userInput.equals("2")) {
                SocketClient socketClient = new SocketClient(PORT, CLIENT_FILE_PATH);
                socketClient.start();
            } else if (userInput.equals("3")) {
                File f1 = new File(SERVER_FILE_PATH);

                boolean bool = f1.mkdir();
                if (bool) {
                    System.out.println("Folder is created successfully");
                } else {
                    System.out.println("Folder already created!");
                }
            } else if(userInput.equals("4"))
            {
                DoublyLinkedList DLL = new DoublyLinkedList();
                DLL.addNode("420.420.420.420");
                DLL.addNode("1.1.1.1");
                DLL.addNode("2.2.2.2");
                DLL.showData();
                //Node myHead = DLL.getHead();
                //System.out.println(myHead.data);
                //System.out.println(myHead.next.data);
                //System.out.println(myHead.next.next.data);
                //System.out.println(myHead.next.next.next.data);
                
                //System.out.println(myHead.data);
                //System.out.println(myHead.prev.data);
                //System.out.println(myHead.prev.prev.data);
                //System.out.println(myHead.prev.prev.prev.data);
            }

        } catch (IOException e) {}*/
        try {
            //finds all nodes in the network (using port 9000) and returns the opened sockets in an array
            /*FindIpAddresses findIps = new FindIpAddresses();
            nodes = findIps.getSockets();*/

            //constantly running in case a client wants to join the network
            Server server = new Server(prevNextNodes);
            server.start();
        
            //pause while client side node is looking for nodes in network
            try{
                Thread.sleep(3000);
            }
            catch(InterruptedException e) {}
            
            //synchronizing (hash table and) files
            System.out.println(server.getClientIp());
            FileServer fileServer = new FileServer(server.getClientIp());
            fileServer.start();
            /*if (!nodes.isEmpty()) { //if a network exists, try to join the network
                Client client = new Client(nodes.get(0), prevNextNodes);
                client.start();
            }*/

            //wait while client joins network
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }

            //try to synchronize files
            //FileClient fileClient = new FileClient();
            System.out.println(prevNextNodes.get(0) + " " + prevNextNodes.get(0));

        } catch (Exception e) {
        }
    }
}
