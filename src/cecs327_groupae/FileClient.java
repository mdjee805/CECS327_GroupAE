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

    int port;
    String path;
    ObjectInputStream ois;
    ByteBuffer buffer;
    SocketChannel client;
    ServerSocketChannel serverSocket;

    public FileClient() {
        this.port = Integer.parseInt(CECS327_GroupAE.PORT) + 1;
        this.path = CECS327_GroupAE.DIRECTORY_PATH;
        this.buffer = ByteBuffer.allocate(1024);
        try {
            client = null;
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            client = serverSocket.accept();
            System.out.println("Connection Set:  " + client.getRemoteAddress());
            ois = new ObjectInputStream(client.socket().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void run() {
        super.run();
        
    }*/

    public String receiveFile() throws IOException {
        try {
            String fileString = (String) ois.readObject();
            long fileSize = (long) ois.readObject();
            System.out.println(fileString);
            System.out.println(fileSize);

            Path filePath = Paths.get(fileString);
            FileChannel fileChannel = FileChannel.open(filePath,
                    EnumSet.of(StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING,
                            StandardOpenOption.WRITE)
            );
            //while (client.read(buffer)> 0) {
            for (int j = 0; j < Math.ceil((fileSize + 0.0) / buffer.capacity()); ++j) {
            //while(fileChannel.read(buffer) >= 0 || buffer.position() > 0){
                client.read(buffer);
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
            fileChannel.close();
            System.out.println("File Received");
            return fileString;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public void close() {try{client.close();serverSocket.close();}catch(IOException e){}}

    //client.close();
}
