/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;
import java.util.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Michael
 */
public class CECS327_GroupAE {
    
    static private ArrayList<String> ipAddresses;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try(final DatagramSocket socket = new DatagramSocket()){
        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        ipAddresses.add(socket.getLocalAddress().getHostAddress());
        //socket.connect(InetAddress.getByName("MICHAELSHAREDPC"), 10002);
        for(String s : ipAddresses)
            System.out.println(s);
        //String s = socket.getLocalAddress().getHostAddress();
        //System.out.println(s);
        }
        catch(SocketException e) {
            e.printStackTrace();
        }
        catch(UnknownHostException e) {
            e.printStackTrace();
        }
    }
    
}
