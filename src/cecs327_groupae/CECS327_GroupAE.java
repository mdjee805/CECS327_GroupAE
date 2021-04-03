/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class CECS327_GroupAE {
    
    static final String PORT = "9000", SERVER_FILE_PATH = "C:/cecs327/test.txt", CLIENT_FILE_PATH = SERVER_FILE_PATH;
    static ArrayList<String> nodes;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try{
        FindIpAddresses fia = new FindIpAddresses();
        nodes = fia.getNodes();
        
        Client client = new Client();
        client.start();
        
        Server server = new Server();
        server.start();
        
        //FileClient fc = new FileClient(PORT, CLIENT_FILE_PATH);
        //fc.start();
        
        //FileServer fs = new FileServer(nodes.get(0), PORT, SERVER_FILE_PATH);
        //fs.start();
        }
        catch(IOException e) {}
    }
}
