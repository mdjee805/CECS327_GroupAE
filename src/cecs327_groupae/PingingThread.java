/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class PingingThread extends Thread {

    int start, end, timeout;
    String subnet, ipAddress;
    ArrayList<String> ipAddresses;
    ArrayList<Socket> ipSockets;

    public PingingThread(int start, int end, String subnet, int timeout, ArrayList<String> ipAddresses, ArrayList<Socket> ipSockets, String ipAddress) {
        this.start = start;
        this.end = end;
        this.subnet = subnet;
        this.timeout = timeout;
        this.ipAddresses = ipAddresses;
        this.ipSockets = ipSockets;
        this.ipAddress = ipAddress;
    }

    @Override
    public void run() {
        try {
            for (int i = start; i < end; i++) {
                String host = subnet + "." + i;
                try {
                    if (InetAddress.getByName(host).isReachable(timeout)) {
                        //SocketChannel server = SocketChannel.open();
                        //SocketAddress socketAddr = new InetSocketAddress(host, Integer.parseInt("9000"));
                        Socket socket = new Socket(host, Integer.parseInt(CECS327_GroupAE.PORT));
                        if (socket.isConnected() && ipAddress != host) {
                            ipAddresses.add(host);
                            ipSockets.add(socket);
                        }
                        //server.close();
                    }
                } catch (SocketException e) {
                } catch (UnknownHostException e) {
                }
            }
        } catch (IOException e) {
        }
    }
}