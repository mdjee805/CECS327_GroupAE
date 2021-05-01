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
        prevNextNodes.add(".0"); //1st is prev
        prevNextNodes.add(".0"); //2nd is next
        ArrayList<Socket> nodes = new ArrayList<>(); //array of sockets of nodes in the network
        Client client;
        Server server;

        try {
            //finds all nodes in the network (using port 9000) and returns the opened sockets in an array
            //we should be checking here for empty string in prevNextNodes, closing the socket then reopening in below loop
            FindIpAddresses findIps = null;
            
            while(nodes.size() == 0)//nodes.isEmpty())
            {
                try{
                findIps = new FindIpAddresses(nodes);
                findIps.start();
                server = new Server(prevNextNodes);
                server.start();
                Thread.sleep(19000);
                }
                catch(SocketException e) {}
            }
            for(int i = 0; i < nodes.size(); ++i)
            {
                nodes.get(i).close();
            }
            
            ///client = new Client(nodes.get(0), prevNextNodes);
            //client.start();

            System.out.println(prevNextNodes.get(0) + " " + prevNextNodes.get(0));

            while (true) //constantly running in case a client wants to join the network
            {
                //server should only try to push files if it has an update
                server = new Server(prevNextNodes);
                server.start();

                if (prevNextNodes.get(1).equals("")){//!nodes.isEmpty()) { //if a network exists, try to join the network
                    client = new Client(/*nodes.get(1),*/ prevNextNodes, findIps.getIpAddress());
                    client.start();
                }
            }
        } catch (Exception e) {
        }
    }
}
