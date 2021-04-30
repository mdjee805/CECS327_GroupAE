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

    private String clientIp, path;
    private int port;
    private SocketChannel server;
    private SocketAddress socketAddr;
    private ByteBuffer buffer;
    private ObjectOutputStream oos;
    private File directory;
    
    public FileServer(String clientIp) {
        this.port = Integer.parseInt(CECS327_GroupAE.PORT) + 1;
        this.clientIp = clientIp;
        this.path = CECS327_GroupAE.DIRECTORY_PATH;
        try {
            //try to connect to client
            server = SocketChannel.open();
            System.out.println("Waiting for client");
            
            //connect to this client
            socketAddr = new InetSocketAddress(clientIp, port);
            server.connect(socketAddr);
            
            buffer = ByteBuffer.allocate(1024); //set buffer size to 1MB
            oos = new ObjectOutputStream(server.socket().getOutputStream()); //set outputstream to server's output stream
            
            //loop over all files in our directory and send them
            /*directory = new File(path);
            File[] files = directory.listFiles();
            for (File f : files) {
                sendFile(f);
                Thread.sleep(100);
            }
            server.close();*/
            
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (InterruptedException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    /*@Override
    public void run() {
        super.run();
        
    }*/

    public void sendFile(File f) throws IOException {
        
        //open the file and push to buffer
        oos.writeObject(f.getAbsolutePath());
        oos.writeObject(f.length());
        System.out.println(f.getAbsolutePath());
        FileChannel fileChannel = FileChannel.open(f.toPath());

        //read 1 MB of buffer at a time and send it
        while (fileChannel.read(buffer) >= 0 || buffer.position() > 0) {
            buffer.flip();
            server.write(buffer);
            buffer.clear();
        }
        fileChannel.close();
        System.out.println("File Sent");
    }
    
    public void close() { try{server.close();}catch(IOException e){} }
}
