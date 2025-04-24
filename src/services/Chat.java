
package services;

import pojos.ChatClient;
import pojos.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;



public interface Chat extends Remote{
    
    public void send_message(Message msg) throws RemoteException;//Send Message Method

    public Message broadcast() throws RemoteException;//Broadcast Method used by the RMI Client to Broadcast all message it receives
    
    public void subscribre(int group_id,ChatClient c) throws RemoteException;//Method for Users to Subscribe for Scpecific Chat Group
    
    public void unsubscribre(int group_id,ChatClient c) throws RemoteException;//Method for Users to unSubscribe from Scpecific Chat Group
    
    public boolean is_subscribed(int client_id) throws RemoteException;//Method for check whether the Specific user is Subscribe or Not to Broadcast Messages 
}
