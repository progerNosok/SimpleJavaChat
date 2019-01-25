/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author l
 */
public class TCPConnection 
{
    private TCPConnectionListener listener;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Thread thread;
    
    public TCPConnection(TCPConnectionListener listener, String ip, int port) throws IOException
    {
        this(listener, new Socket(ip, port));
    }
    
    public TCPConnection(TCPConnectionListener listener, Socket socket) throws IOException
    {
        this.listener = listener;
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        thread = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try
                {
                    listener.onConnectionReady(TCPConnection.this);
                    
                    while(!thread.isInterrupted())
                    {
                        listener.onRecieveString(TCPConnection.this, reader.readLine());
                    }
                    
                } catch(IOException e)
                {
                    listener.onException(TCPConnection.this, e);
                }
                finally
                {
                    listener.onDisconnect(TCPConnection.this);
                }
            }
        });
        thread.start();
    }
    
    public synchronized void writeMessage(String message)
    {
        try {
            writer.write(message + "\r\n");
            writer.flush();
        } catch (IOException ex) {
           listener.onException(this, ex);
        }
    }
    
    public synchronized void disconect()
    {
        try {
            thread.interrupt();
            socket.close();
        } catch (IOException ex) {
            listener.onException(this, ex);
        }
    }

    @Override
    public String toString()
    {
        return socket.getInetAddress() + " : " + socket.getPort();
    }
    
    
}
