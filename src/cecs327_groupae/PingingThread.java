/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class PingingThread extends Thread {

    int start, end, timeout;
    String subnet;
    ArrayList<String> ipAddresses;

    public PingingThread(int start, int end, String subnet, int timeout, ArrayList<String> ipAddresses) {
        this.start = start;
        this.end = end;
        this.subnet = subnet;
        this.timeout = timeout;
        this.ipAddresses = ipAddresses;
        //FindIpAddresses.threadCreated();
    }

    @Override
    public void run() {
        try {
            FindIpAddresses.threadsRunning();
            for (int i = start; i < end; i++) {
                String host = subnet + "." + i;
                try {
                    if (InetAddress.getByName(host).isReachable(timeout)) {
                        ipAddresses.add(host);
                        //System.out.println(host + " is reachable");
                    } /*else {
                        System.out.println(host + " timed out");
                    }*/
                } catch (SocketException e) {
                } catch (UnknownHostException e) {
                }
            }
        } catch (IOException e) {
        }
        finally{ FindIpAddresses.threadsCompleted(); /*System.out.println("done");*/}
    }
}