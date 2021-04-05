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
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Michael
 */
public class RequestHandler extends Thread{
    
    private Socket socket;
    private Hashtable hashTestDHT;
    private ArrayList<String> prevNextNodes;
    private int port;
    
    public RequestHandler(Socket socket, ArrayList<String> prevNextNodes, int port)
    {
        this.socket = socket;
        /*hashTestDHT = new Hashtable();
        hashTestDHT.put("Michael", "china numbah one");
        hashTestDHT.put("Bryan", "taiwan numbah one");
        hashTestDHT.put("Minh", "korea numbah one");
        hashTestDHT.put("Alissa", "japan numbah one");*/
        this.prevNextNodes = prevNextNodes;
        this.port = port;
    }
    
    @Override
    public void run()
    {
        super.run();
        try{
        connect();
        }
        catch (IOException e) {}
    }
    private void connect() throws IOException
    {
        try
        {
            System.out.println( "Received a connection" );

            // Get input and output streams
            /*BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            PrintWriter out = new PrintWriter( socket.getOutputStream() );

            // Write out our header to the client
            out.println( "Echoing" );
            out.flush();

            // Echo lines back to the client until the client closes the connection or we receive an empty line
            String line = in.readLine();
            while( line != null && line.length() > 0 )
            {
                out.println( "Echo: " + line );
                out.flush();
                line = in.readLine();
            }*/
            
            /*ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(hashTestDHT);
            oos.close();*/
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //oos.writeObject(hashTestDHT);
            oos.writeObject(prevNextNodes);
            
            //Socket client = new Socket(socket.getInetAddress().toString().substring(1))
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            //Socket serverSocket = (Socket) ois.readObject();
            ArrayList<String> temp = (ArrayList<String>) ois.readObject();
            
            prevNextNodes.set(0, temp.get(0));
            System.out.println("previous: " + prevNextNodes.get(0));
            if (prevNextNodes.get(0) == "") { //if we have 1 node in network, we set previos to the client
                prevNextNodes.set(0, socket.getInetAddress().toString().substring(1));
                System.out.println("previous: " + prevNextNodes.get(0));
            }
            else //if network has more than 1 node, we tell the original previous that its new previous is the original client
            {
                Socket prevSocket = new Socket(prevNextNodes.get(0), port);
                oos = new ObjectOutputStream(prevSocket.getOutputStream());
                oos.writeObject(socket.getInetAddress().toString().substring(1));
            }

            //we always set the next node to the client
            prevNextNodes.set(1, temp.get(1));
            System.out.println("next: " + prevNextNodes.get(1));
            prevNextNodes.set(1, socket.getInetAddress().toString().substring(1));
            System.out.println("next: " + prevNextNodes.get(1));

            // Close our connection
            //in.close();
            //out.close();
            //socket.close();

            System.out.println( "Connection closed" );
            
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            socket.close();
        }
    }
}
