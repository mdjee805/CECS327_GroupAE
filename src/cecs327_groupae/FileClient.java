/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 *
 * @author Michael
 */
public class FileClient extends Thread {

    int port;
    Path path;

    public FileClient(String port, String path) {
        this.port = Integer.parseInt(port);
        this.path = Paths.get(path);
    }

    @Override
    public void run() {
        super.run();
        try {
            receiveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFile() throws IOException {
        ServerSocketChannel serverSocket = null;
        SocketChannel client = null;
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        client = serverSocket.accept();
        System.out.println("Connection Set:  " + client.getRemoteAddress());
        FileChannel fileChannel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)
        );
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        while (client.read(buffer) > 0) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }
        
        fileChannel.close();
        System.out.println("File Received");
        client.close();
    }
}
