/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class FindIpAddresses extends Thread{

    static private String ipAddress, subnet;
    static private ArrayList<String> ipAddresses;
    static private ArrayList<Socket> ipSockets;
    
    public FindIpAddresses(ArrayList<Socket> ipSockets) throws IOException
    {
        ipAddresses = new ArrayList<String>();
        this.ipSockets = ipSockets;
    }
    
    public void printIpAddresses()
    {
        System.out.println("Devices on subnet " + subnet + ": ");
        for(String s : ipAddresses)
        {
            System.out.println(s);
        }
    }
    
    @Override
    public void run() { try{getSockets();}catch(IOException e){} }
    
    public ArrayList<String> getNodes() { return ipAddresses; }
    
    public void getSockets() throws IOException {
        try(final DatagramSocket socket = new DatagramSocket()){
        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        ipAddress = socket.getLocalAddress().getHostAddress(); //find local ip address
        subnet = ipAddress.substring(0, ipAddress.lastIndexOf('.')); //get the subnet from it
        }
        catch(SocketException e) {
            e.printStackTrace();
        }
        catch(UnknownHostException e) {
            e.printStackTrace();
        }
        
        //make a bunch of threads and try to connect to all possible ip addresses in the subnet
        int timeout = 1000, threads = 255, devices = 255, split = devices / threads;
        ipAddresses = new ArrayList<String>();
        for (int i = 0; i < threads; ++i) {
            if(!ipAddress.equals(subnet + '.' + i))
            {
                PingingThread thread = new PingingThread(split * i, split * (i + 1), subnet, timeout, ipAddresses, ipSockets);
                thread.start();
            }
        }
        
        //wait for the timeouts on all the thread's attempted connections
        try{
        Thread.sleep(1000);}
        catch(InterruptedException e)
        {
            System.out.println("Network scan timeout");
        }
        
        for(String s : ipAddresses)
        {
            //don't put the local ip address in the list
            if(s.equals(ipAddress))
                ipAddresses.remove(s);
        }
        
        printIpAddresses();
    }
    
    public String getIpAddress() { return ipAddress; }
}