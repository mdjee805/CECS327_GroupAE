/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
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
    private final Path path = Paths.get("C:/cecs327");
    private File[] files;
    private NodeVariables nv;
    
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
        File directory = path.toFile();
        files = directory.listFiles();
        nv = NodeVariablesSingleton.getNodeVariablesSingleton();
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

            for(File f : files)
            {
                System.out.println(f.getName());
            }
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
            } else //if network has more than 1 node, we tell the original previous that its new previous is the original client
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

            //create new dht from local folder
            Multimap<String, String> multimap = ArrayListMultimap.create();
            for (File f : files) {
                multimap.put(f.getName(), Long.toString(f.lastModified()));
            }
            
            //compare new dht to stored
            if(!nv.getDht().equals(multimap)) //if dhts not equal
            {
                nv.setDht(multimap);
                Date dt = new Date();
                nv.setDhtTime(dt.getTime());
            }

            oos.writeObject(multimap);
            
            //send dht timestamp    
            if(multimap.isEmpty())
            {
                oos.writeObject(0);
            }
            else
            {
                oos.writeObject(nv.getDhtTime());
            }

            // Close our connection
            //in.close();
            //out.close();
            //socket.close();

            System.out.println( "Connection closed" );
            
            //NioSocketServer nss = new NioSocketServer();
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
