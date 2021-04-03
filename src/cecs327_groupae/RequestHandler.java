/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs327_groupae;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

/**
 *
 * @author Michael
 */
public class RequestHandler extends Thread{
    
    private Socket socket;
    private Hashtable hashTestDHT;
    
    public RequestHandler(Socket socket)
    {
        this.socket = socket;
        hashTestDHT = new Hashtable();
        hashTestDHT.put("Michael", "china numbah one");
        hashTestDHT.put("Bryan", "taiwan numbah one");
        hashTestDHT.put("Minh", "korea numbah one");
        hashTestDHT.put("Alissa", "japan numbah one");
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
    private void connect() throws IOException
    {
        try
        {
            System.out.println( "Received a connection" );

            // Get input and output streams
            /*BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            PrintWriter out = new PrintWriter( socket.getOutputStream() );

            // Write out our header to the client
            out.println( "Echoing" );
            out.flush();

            // Echo lines back to the client until the client closes the connection or we receive an empty line
            String line = in.readLine();
            while( line != null && line.length() > 0 )
            {
                out.println( "Echo: " + line );
                out.flush();
                line = in.readLine();
            }*/
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(hashTestDHT);
            oos.close();

            // Close our connection
            //in.close();
            //out.close();
            //socket.close();

            System.out.println( "Connection closed" );
            
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            socket.close();
        }
    }
}
