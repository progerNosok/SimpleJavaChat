/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Connection.TCPConnection;
import Connection.TCPConnectionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author l
 */
public class Server implements TCPConnectionListener
{
//    private static final String IP = "localhost";
    private static final int PORT = 8000;
    
    private ArrayList<TCPConnection> connections = new ArrayList<>();
    
    private Server()
    {
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(PORT))
        {
            while(true)
            {
                new TCPConnection(this, serverSocket.accept());
            }
        }catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws IOException
    {
        new Server();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection connection)
    {
        sendMessageToAll(connection + "присоеденился");
        connections.add(connection);
    }

    @Override
    public synchronized void onRecieveString(TCPConnection connection, String value) 
    {
        sendMessageToAll(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection connection)
    {
        sendMessageToAll(connection + "disconected");
        connections.remove(connection);
        connection.disconect();
    }

    @Override
    public synchronized void onException(TCPConnection connection, Exception e) 
    {
        sendMessageToAll(e.getMessage());
        connection.disconect();
    }
    
    private void sendMessageToAll(String message)
    {
        System.out.println(message);
        
        for(TCPConnection connection : connections)
        {
            connection.writeMessage(message);
        }
    }
    
   
}
