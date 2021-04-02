/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Michael
 */
public class FileServer extends Thread {

    String clientIp;
    int port;
    Path path;
    
    public FileServer(String clientIp, String port, String path) {
        this.port = Integer.parseInt(port);
        this.clientIp = clientIp;
        this.path = Paths.get(path);
    }

    @Override
    public void run() {
        super.run();
        try {
            sendFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile() throws IOException {
        SocketChannel server = SocketChannel.open();
        System.out.println("Waiting for client");
        SocketAddress socketAddr = new InetSocketAddress(clientIp, port);
        server.connect(socketAddr);
        FileChannel fileChannel = FileChannel.open(path);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) > 0) {
            buffer.flip();
            server.write(buffer);
            buffer.clear();
        }
        
        fileChannel.close();
        System.out.println("File Sent");
        server.close();
    }
}
