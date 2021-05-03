/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Michael
 */
public class RequestHandler extends Thread{
    
    private Socket socket;
    private ArrayList<String> prevNextNodes;
    private int port;
    private final Path path = Paths.get(CECS327_GroupAE.DIRECTORY_PATH);
    private File[] files;
    //private NodeVariables nv;
    
    public RequestHandler(Socket socket, ArrayList<String> prevNextNodes)
    {
        this.socket = socket;
        this.prevNextNodes = prevNextNodes;
        this.port = Integer.parseInt(CECS327_GroupAE.PORT);
        
        //used to list all files in our shared directory
        File directory = path.toFile();
        files = directory.listFiles();
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
    
    //exchange previous and next nodes, exchange hashtables, synchronize files
    private void connect() throws IOException
    {
        try
        {
            System.out.println( "Received a connection" );

            for(File f : files)
            {
                System.out.println(f.getName());
            }
            
        //1. exchanging previous and next nodes
            //send over sever's previous and next nodes' ip addreeses
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(prevNextNodes);
            
            //receive client's previous and next nodes' ip addresses
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ArrayList<String> clientPrevNextNodes = (ArrayList<String>) ois.readObject();
            
            //handle adding client into the network            
            if (prevNextNodes.get(0).equals(".0")) { //if we have 1 node in network, we set previous to the client
                prevNextNodes.set(0, socket.getInetAddress().toString().substring(1));
                System.out.println("previous: " + prevNextNodes.get(0));
            } else //if network has more than 1 node, we tell the original previous that its new previous is the original client
            {
                Socket prevSocket = new Socket(prevNextNodes.get(0), Integer.parseInt(CECS327_GroupAE.PORT));
                oos = new ObjectOutputStream(prevSocket.getOutputStream());
                oos.writeObject(socket.getInetAddress().toString().substring(1));
            }

            //we always set the next node to the client's ip
            prevNextNodes.set(1, socket.getInetAddress().toString().substring(1));
            System.out.println("next: " + prevNextNodes.get(1));

        //2. exchanging dhts
            //create new dht from local folder
            Map<String, String> fileMap = new HashMap<>();
            for (File f : files) {
                fileMap.put(f.getName(), Long.toString(f.lastModified()));
                System.out.println("file: " + f.getName());
            }
            System.out.println("hash size: " + fileMap.size());

            //send dht to client
            oos.writeObject(fileMap);

            //await for response for which files the client wants, an array list 
              //with the first item not being "gimme files" means no files requested
            ArrayList<String> fileList = (ArrayList<String>) ois.readObject();
            
            //while client wants files, create new fileserver and send files
            while(fileList.get(0).equals("gimme files")) {
                FileServer fs = new FileServer(socket.getInetAddress().toString().substring(1));
                try {
                    //loop over the number of files wanted and send the requested file if it exists
                    int size = fileList.size();
                    for (int i = 1; i < size; ++i) {
                        File f = new File(CECS327_GroupAE.DIRECTORY_PATH + '/' + fileList.get(i));
                        if (f != null) {
                            fs.sendFile(f);
                            Thread.sleep(1000);
                        }
                    }
                } catch (IOException e) {
                }
                fs.close();
                
                //check if any files corrupted in transit and client wants them re-sent
                fileList = (ArrayList<String>) ois.readObject();
                Thread.sleep(1000);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
