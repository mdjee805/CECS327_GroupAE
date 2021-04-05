/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class CECS327_GroupAE {
    
    static final String PORT = "9000", SERVER_FILE_PATH = "C:/cecs327/test.txt", CLIENT_FILE_PATH = SERVER_FILE_PATH;
    static ArrayList<String> nodes;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<String> prevNextNodes = new ArrayList<String>();
        prevNextNodes.add("");
        prevNextNodes.add("");
        //ArrayList<String> nodes;
        ArrayList<Socket> nodes;
        
        try {
        //FindIpAddresses findIps = new FindIpAddresses();
        //nodes = findIps.getSockets();
        
        Server server = new Server(prevNextNodes);
        server.start();
        
        /*try{
            Thread.sleep(100);
        }
        catch(InterruptedException e) {}

        if(!nodes.isEmpty())
        {
            Client client = new Client(nodes.get(0), prevNextNodes);
            client.start();
        }*/
        
        try{
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {}
        
        System.out.println(prevNextNodes.get(0) + " " + prevNextNodes.get(0));
        
        }
        catch (IOException e) {}
    }
}
