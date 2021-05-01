/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileClient {

    ObjectInputStream ois;
    ByteBuffer buffer;
    SocketChannel client;

    //make a socket connection with fileserver
    public FileClient() {
        //using port 9001 to keep separate socket from port 9000 for joining network if connecting to the same ipaddress
        int port = Integer.parseInt(CECS327_GroupAE.PORT) + 1;
        //transfer 1 megabyte at a time
        buffer = ByteBuffer.allocate(1024);
        try {
            //open a channel for connections on this machine at port 9001
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            client = serverSocket.accept();
            System.out.println("Connection Set:  " + client.getRemoteAddress());
            ois = new ObjectInputStream(client.socket().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //receive files from the socket connection
    public String receiveFile() throws IOException {
        try {
            //priming read to obtain file's path and size in bytes
            String fileString = (String) ois.readObject();
            long fileSize = (long) ois.readObject();
            System.out.println(fileString);
            System.out.println(fileSize);

            //create a new file at the received path, if it exists, overwrite
              //we always keep the server's copy of conflicting files
            Path filePath = Paths.get(fileString);
            FileChannel fileChannel = FileChannel.open(filePath,
                    EnumSet.of(StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING,
                            StandardOpenOption.WRITE)
            );
            
            //while(fileChannel.read(buffer) >= 0 || buffer.position() > 0){
            //similar to above line, but does math to deteremine how many times to accept data
            for (int i = 0; i < Math.ceil((fileSize + 0.0) / buffer.capacity()); ++i) {
                //read a megabyte from tthe buffer and write it to the file
                client.read(buffer);
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
            fileChannel.close();
            
            System.out.println("File Received");
            //return the file's name if successful so that Client knows it was successful
            return fileString;
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return no file name if transfer not successful so that the name is not
          //removed from the list of requested files
        return "";
    }

    //close the connection
    public void close() { try{ client.close(); } catch(IOException e) {} }
}
