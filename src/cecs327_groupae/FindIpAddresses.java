/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class FindIpAddresses {

    static private ArrayList<String> ipAddresses;
    static private String subnet;

    public FindIpAddresses() {
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

        int timeout = 1000, threads = 64, devices = 255, split = devices / threads; //minimum timeout from methods .isReachable calls seems to be 1sec
        ipAddresses = new ArrayList<String>();
        for (int i = 0; i < threads; ++i) {
            PingingThread thread = new PingingThread(split * i, split * (i + 1), subnet, timeout, ipAddresses);
            thread.start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Network scan timeout");
        }
    }

    public void printIpAddresses() {
        System.out.println("Devices on subnet " + subnet + ": ");
        for (String s : ipAddresses) {
            System.out.println(s);
        }
    }
    
    public ArrayList<String> getNodes() { return ipAddresses; }
}
