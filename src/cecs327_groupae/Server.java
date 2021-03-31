/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Michael
 */
public class Server extends Thread {
    
    private int port;
    private boolean isConnected;
    private ServerSocket serverSocket;
    
    public Server() throws IOException {
        port = 43594;
        isConnected = false;
        serverSocket = new ServerSocket(port); //open port
    }
    
    @Override
    public void run() {
    while (!isConnected) {
            try {
                System.out.println( "Listening for a client" );
                
                //invoke accept method to listen on port
                try
                {
                    Socket socket = serverSocket.accept();
                    
                    System.out.println("Connected");
                    
                    isConnected = true;
                }
                catch(SocketException e)
                {
                    System.out.println("Error connecting to port: " + String.valueOf(port));
                }
                finally
                {
                    serverSocket.close();
                }

                //close socket
                //serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}