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

/**
 *
 * @author Michael
 */
public class CECS327_GroupAE {

    static private ArrayList<String> ipAddresses;
    static private String subnet;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ipAddresses.add(socket.getLocalAddress().getHostAddress());
            //socket.connect(InetAddress.getByName("MICHAELSHAREDPC"), 10002);
            for (String s : ipAddresses) {
                System.out.println(s);
            }
            //String s = socket.getLocalAddress().getHostAddress();
            //System.out.println(s);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/

        /*try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    System.out.println(i.getHostAddress());
                }
            }
        } catch(SocketException e) {
            e.printStackTrace();
        }*/
        String ip;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
            subnet = ip.substring(0, ip.lastIndexOf('.'));
            //System.out.println(subnet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        int timeout = 1000;
        ipAddresses = new ArrayList<String>();
        try {
            for (int i = 0; i < 255; i++) {
                String host = subnet + "." + i;
                try {
                    if (InetAddress.getByName(host).isReachable(timeout)) {
                        ipAddresses.add(host);
                        //System.out.println(host + " is reachable");
                    }
                    /*else {
                        System.out.println(host + " timed out");
                    }*/
                } catch (SocketException e) {
                } catch (UnknownHostException e) {
                }
            }
        } catch (IOException e) {
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Network scan timeout");
        }
        /*int timewaster = 0;
        threadsCompleted = 0;
        while (threadsCompleted < threadsRunning) {
            try {
                wait(10);
                System.out.println(String.valueOf(threadsCompleted));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            ++timewaster;
            System.out.println(String.valueOf(timewaster));
        }
        
        System.out.println(String.valueOf(timewaster));*/
        printIpAddresses();

        //socket.send()
        //socket.receive()
    }

    public static void printIpAddresses() {
        System.out.println("Devices on subnet " + subnet + ": ");
        for (String s : ipAddresses) {
            System.out.println(s);
        }
    }
}
