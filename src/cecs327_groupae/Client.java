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
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author Michael
 */
public class Client extends Thread {
    
    private String server;
    private int port;
    boolean isConnected = false;
    private Socket socket;
    private ArrayList<String> prevNextNodes;
    private File[] files;
    private final Path path = Paths.get(CECS327_GroupAE.DIRECTORY_PATH);
    private ArrayList<String> fileList;
    private ObjectOutputStream oos;
    private FileClient fc;
    
    //private NodeVariables nv;

    public Client(ArrayList<String> prevNextNodes, String ipAddress)
    {
        this.prevNextNodes = prevNextNodes;
        //this.socket = serverIp;
        this.port = Integer.parseInt(CECS327_GroupAE.PORT) + Integer.parseInt(ipAddress.substring(ipAddress.lastIndexOf('.') + 1));
        File directory = path.toFile();
        this.files = directory.listFiles();
    }
    
    public void connect() throws IOException {
        while (!isConnected) {
            try {
                System.out.println("Listening for a server");
                socket = new Socket(prevNextNodes.get(1), port);
                
                //send over the previous and next nodes' ip addreeses
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(prevNextNodes);
                
                //receive server's previous and next nodes' ip addresses
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                ArrayList<String> temp = (ArrayList<String>) ois.readObject();
                
                //join network
                prevNextNodes.set(0, temp.get(0)); //set client's previous node to client's previous node ip
                System.out.println("previous: " + prevNextNodes.get(0));
                
                 //client's previous is always the server
                prevNextNodes.set(0, socket.getInetAddress().toString().substring(1));
                System.out.println("previous: " + prevNextNodes.get(0));

                prevNextNodes.set(1, temp.get(1));
                System.out.println("next: " + prevNextNodes.get(1));
                if(prevNextNodes.get(1) == ".0") //if there is 1 node in the network, we set next as the server, otherwise we steal the server's next
                {
                    prevNextNodes.set(1, socket.getInetAddress().toString().substring(1));
                }
                System.out.println("next: " + prevNextNodes.get(1));
                
                //receive hashtable from server
                Map<String, String> serverFileMap = (HashMap<String, String>) ois.readObject();
                Set<String> set = serverFileMap.keySet();
                for(String s : set)
                {
                    System.out.println("Key: " + s);
                    System.out.println("Value: " + serverFileMap.get(s));
                }
                
                //make a fresh hashtable from local files
                Map<String, String> fileMap = new HashMap<>();
                for (File f : files) {
                    fileMap.put(f.getName(), Long.toString(f.lastModified()));
                    //System.out.println("file: " + f.getName());
                }
                
                
                //create a filelist of names of files client wants
                fileList = new ArrayList<>();
                fileList.add("gimme files");
                //merge the two hashtables
                if(!fileMap.equals(serverFileMap))//see if hashtables equal
                {
                    Map<String, String> tempFileMap = new HashMap<>(); //if not make a temp hashtable
                    tempFileMap.putAll(fileMap); //copy the client file info into it
                    
                    for(String s : set) //loop over the server files and see which are different
                    {
                        if(tempFileMap.containsKey(s)) //if filenames are the same, get the server's copy if contents are different
                        {
                            if(!tempFileMap.get(s).equals(serverFileMap.get(s)))
                            {
                                tempFileMap.put(s, serverFileMap.get(s)); //theoretically, compare the file contents hashed here and use that for file merging
                                fileList.add(s); //add to list to send to server which files client wants
                            }
                        }
                        else  //if client does not have the file, add to dht and request from server
                        {
                            tempFileMap.put(s, serverFileMap.get(s));
                            fileList.add(s); //add to list to send to server which files client wants
                        }
                    }
                }
                else
                {
                    fileList.set(0, "nah I'm good");
                }
                
                for(int i = 0; i < fileList.size(); ++i)
                {
                    System.out.println(fileList.get(i));
                }
                
                //printing files that is received
                oos.writeObject(fileList);
                getFiles();
                while(fileList.size() > 1)
                {
                    for(int i = 0; i < fileList.size(); ++i)
                        System.out.println(fileList.get(i));
                    Thread.sleep(1000);
                    getFiles();
                }
                
                isConnected = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    //received files from other nodes
    private void getFiles() {
        try{
            fc = new FileClient();
            int size = fileList.size();
        for (int i = 1; i < size; ++i) {
            
            try {
                String fileReceived = fc.receiveFile();
                fileReceived = fileReceived.substring(fileReceived.lastIndexOf('\\') + 1);
                fileList.remove(fileReceived);
                System.out.println(fileReceived);
                System.out.println("still want: " + fileList.size());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error transferring file");
            }
            
        }
        fc.close();
        oos.writeObject(fileList);
        }
        catch(Exception e) { }
    }

    @Override
    public void run() {
        super.run();
        try{
            connect();
        }
        catch(IOException e) {}
    }
}
