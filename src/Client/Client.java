/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Connection.TCPConnection;
import Connection.TCPConnectionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author l
 */
public class Client extends JFrame
{
    private static final String IP = "localhost";
    private static final int PORT = 8000;
    
    private JTextArea screen = new JTextArea();
    private JFormattedTextField nick = new JFormattedTextField("guest");
    private JFormattedTextField text = new JFormattedTextField();
    
    private TCPListener listener = new TCPListener();
    private TCPConnection connection;
    
    public Client()
    {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(600, 400);
            setLocationRelativeTo(null);
            
            screen.setEditable(false);
            screen.setLineWrap(true);
            getContentPane().add(screen, BorderLayout.CENTER);
            
            text.addActionListener(new TextListener());
            getContentPane().add(nick, BorderLayout.NORTH);
            getContentPane().add(text, BorderLayout.SOUTH);
            
            setVisible(true);
            
            try {
            connection = new TCPConnection(listener, IP, PORT);
        } catch (IOException ex) {
            listener.onException(connection, ex);
        }
      
    }
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new Client();
            }
        });
    }
    
    
    private class TextListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            String message = text.getText();
            if(message.isEmpty()) return;
            
            text.setText(null);
            connection.writeMessage(nick.getText() + ": " + message);
        }
        
    }
    
    private class TCPListener implements TCPConnectionListener
    {

        @Override
        public synchronized void onConnectionReady(TCPConnection connection) 
        {
            printText("Connection ready");
        }

        @Override
        public synchronized void onRecieveString(TCPConnection connection, String value) 
        {
            printText(nick.getText() + ": " + value);
        }

        @Override
        public synchronized void onDisconnect(TCPConnection connection) 
        {
            printText(nick.getText() + "was dicsonnected!!!!");
        }

        @Override
        public synchronized void onException(TCPConnection connection, Exception e) 
        {
            printText(e.getMessage());
        }
        
        private void printText(String message)
        {
            SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() 
                {
                    screen.append(message + "\n");
//                    connection.writeMessage(message);
                }
            });
        }
        
    }
}
