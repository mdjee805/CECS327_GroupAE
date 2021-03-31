/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;
import java.io.IOException;

/**
 *
 * @author Michael
 */
public class CECS327_GroupAE {



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try{
        //FindIpAddresses fia = new FindIpAddresses();
        
        //Client client = new Client();
        //client.start();
        
        Server server = new Server();
        server.start();
        }
        catch(IOException e) {}
    }
}
