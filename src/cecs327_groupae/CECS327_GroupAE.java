/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;
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

    //hard code port number and directory path to make life easy
    public static final String PORT = "9000", DIRECTORY_PATH = "C:/cecs327";
    private static final Path path = Paths.get(DIRECTORY_PATH);
    private static File[] files;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        //the ip address of the previous and next nodes in the doubly linked list
        ArrayList<String> prevNextNodes = new ArrayList<String>();
        prevNextNodes.add(".0"); //1st is prev (default set to .0)
        prevNextNodes.add(".0"); //2nd is next (default set to .0)
        ArrayList<Socket> nodes = new ArrayList<>(); //array of sockets of nodes in the network
        Client client;
        Server server;

        try {
            //finds all nodes in the network (using port 9000) and returns the opened sockets in an array
            //we should be checking here for ".0" in prevNextNodes, closing the socket then reopening in below loop
            FindIpAddresses findIps = null;
            while(prevNextNodes.get(1).equals(".0"))
            {
                try{
                //open server port
                server = new Server(prevNextNodes);
                server.setPort(Integer.parseInt(PORT));
                server.start();
                Thread.sleep(1000);
                System.out.println("Trying");
                //find all ip address
                findIps = new FindIpAddresses(nodes);
                findIps.start();
                Thread.sleep(1000);
                System.out.println(prevNextNodes.get(1));
                prevNextNodes.set(1, findIps.getIpAddresses().get(0));
                }
                catch(SocketException e) {
                    System.out.println("CATCH");
                    Thread.sleep(19000);
                }
            }
                    

            Thread.sleep(1000);
            while (true) //constantly running in case a client wants to join the network
            {
                System.out.println(prevNextNodes.get(0) + " " + prevNextNodes.get(1));
                
                client = new Client(prevNextNodes, findIps.getIpAddress());
                client.start();
                
                //server tries to connect
                server = new Server(prevNextNodes);
                server.start();
                Thread.sleep(1000);
                server.startRequestHandler();
                
                Thread.sleep(15000);
            }
            
        } catch (Exception e) {
            
        }
    }
}
