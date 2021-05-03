/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class Server extends Thread {
    
    private int port;
    private boolean isConnected;
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<String> prevNextNodes;
    
    public Server(ArrayList<String> prevNextNodes) throws IOException {
        //ip addresses of the previous node and next node, respectively
        this.prevNextNodes = prevNextNodes;
        //port 9000
        port = Integer.parseInt(CECS327_GroupAE.PORT);
        //flag indicating if we should keep looping
        isConnected = false;
        //connect using the port of 9000 + last digits of ip address
        serverSocket = new ServerSocket(port + Integer.parseInt(prevNextNodes.get(1).substring(prevNextNodes.get(1).lastIndexOf('.') + 1)));
        System.out.println((port + Integer.parseInt(prevNextNodes.get(1).substring(prevNextNodes.get(1).lastIndexOf('.') + 1))));
    }
    
    //manually set port and reconnnect to new socket
    public void setPort(int port) throws IOException { this.port = port; /*serverSocket = new ServerSocket(port);*/ }
    @Override
    public void run() {
        //loop while the socket is not connected
        while (!isConnected) {
            try {
                System.out.println("Listening for a client");

                try {
                    //connect to client on designated port
                    socket = serverSocket.accept();

                    //stop looping
                    isConnected = true;
                } catch (SocketException e) {
                    System.out.println("Error connecting to port: " + String.valueOf(port));
                    //e.printStackTrace();
                } finally {
                    //serverSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //theoretically, hand off to new thread to keep 'server' thread clear for more connections
    public void startRequestHandler() {
        RequestHandler reqHand = new RequestHandler(socket, prevNextNodes);
        reqHand.start();
    }
}
