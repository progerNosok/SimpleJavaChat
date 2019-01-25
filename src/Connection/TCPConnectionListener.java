/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

/**
 *
 * @author l
 */
public interface TCPConnectionListener 
{
    void onConnectionReady(TCPConnection connection);
    void onRecieveString(TCPConnection connection, String value);
    void onDisconnect(TCPConnection connection);
    void onException(TCPConnection connection, Exception e);
}
