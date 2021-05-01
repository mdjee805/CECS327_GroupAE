/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class FileServer extends Thread {

    private SocketChannel server;
    private ByteBuffer buffer;
    private ObjectOutputStream oos;

    //make connection with fileclient
    public FileServer(String clientIp) {
        //using port 9001 to keep separate socket from port 9000 for joining network if connecting to the same ipaddress
        int port = Integer.parseInt(CECS327_GroupAE.PORT) + 1;
        try {
            //try to connect to client
            server = SocketChannel.open();
            System.out.println("Waiting for client");
            
            //connect to this client
            SocketAddress socketAddr = new InetSocketAddress(clientIp, port);
            server.connect(socketAddr);
            
            //set buffer size to 1MB
            buffer = ByteBuffer.allocate(1024);
            
            //set outputstream to server's output stream
            oos = new ObjectOutputStream(server.socket().getOutputStream());
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //receive file from socket
    public void sendFile(File f) throws IOException {
        //send file's path and size in bytes to the fileclient
        oos.writeObject(f.getAbsolutePath());
        oos.writeObject(f.length());
        System.out.println(f.getAbsolutePath());
        
        //open file from the path
        FileChannel fileChannel = FileChannel.open(f.toPath());

        //read 1 MB of buffer at a time and send it to fileclient
        while (fileChannel.read(buffer) >= 0 || buffer.position() > 0) {
            buffer.flip();
            server.write(buffer);
            buffer.clear();
        }
        fileChannel.close();
        System.out.println("File Sent");
    }
    
    //close the connection
    public void close() { try{ server.close(); } catch(IOException e) {} }
}
