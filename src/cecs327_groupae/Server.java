/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import com.google.common.collect.Multimap;
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
    private ArrayList<String> prevNextNodes;
    private String clientIp;
    
    public String getClientIp() { return clientIp; }
    
    public Server(ArrayList<String> prevNextNodes) throws IOException {
        port = Integer.parseInt(CECS327_GroupAE.PORT);
        isConnected = false;
        serverSocket = new ServerSocket(port); //open port
        this.prevNextNodes = prevNextNodes;
    }
    
    @Override
    public void run() {
    while (!isConnected) {
            try {
                System.out.println( "Listening for a client" );
                
                //invoke accept method to listen on port
                try
                {
                    //connect to client
                    Socket socket = serverSocket.accept();
                    clientIp = socket.getInetAddress().getHostAddress();
                    System.out.println(clientIp);
                    
                    //hand off to new thread to keep 'server' thread clear for more connections
                    RequestHandler reqHand = new RequestHandler(socket, prevNextNodes);
                    reqHand.start();
                    isConnected = true;
                }
                catch(SocketException e)
                {
                    System.out.println("Error connecting to port: " + String.valueOf(port));
                    e.printStackTrace();
                }
                finally
                {
                    serverSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
