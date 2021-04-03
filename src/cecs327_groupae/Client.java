/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Set;

/**
 *
 * @author Michael
 */
public class Client extends Thread {
    
    private String server = "192.168.254.18";//, path = "/search?q=banana";
    private int port = 43594;
    boolean isConnected = false;
    private Socket socket;

    public Client() throws IOException {
        
        while (!isConnected) {
            
            try {
                System.out.println("Listening for a server");

                // Connect to the server
                /*Socket socket = new Socket(server, port);

                // Create input and output streams to read from and write to the server
                PrintStream out = new PrintStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Follow the HTTP protocol of GET <path> HTTP/1.0 followed by an empty line
                /*out.println("GET " + path + " HTTP/1.0");
            out.println();*/
                // Read data from the server until we finish reading the document
                /*String line = in.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = in.readLine();
                }*/
                
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                Hashtable testHashDHT = (Hashtable) ois.readObject();
                
                Set<String> tableSet = testHashDHT.keySet();
                for(String key : tableSet)
                {
                    System.out.println("key: " + key + " // value: " + testHashDHT.get(key));
                }
                
                isConnected = true;

                // Close our streams
//                in.close();
//                out.close();
                //socket.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally
            {
                socket.close();
            }
        }
    }
    
    @Override
    public void run() {super.run();}
}