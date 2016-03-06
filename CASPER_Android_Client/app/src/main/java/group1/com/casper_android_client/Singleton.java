package group1.com.casper_android_client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created by Andreas Fransson
 */
public class Singleton {

    private static Singleton ourInstance = new Singleton();

    /**
     *
     * @return
     */
    public static Singleton getInstance() {
        return ourInstance;
    }

    /**
     *
     * @param ourInstance
     */
    public static void setOurInstance(Singleton ourInstance) {
        Singleton.ourInstance = ourInstance;
    }

    private Singleton() {

    }




    // User instance
    private User loggedInUser;

    /**
     * Get the user
     * @return
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Set the User
     * @param loggedInUser
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }


    /**
     *
     */
    private SocketConnection SocketConnection = new SocketConnection("0",0,false);

    public group1.com.casper_android_client.SocketConnection getSocketConnection() {
        return SocketConnection;
    }

    public void setSocketConnection(group1.com.casper_android_client.SocketConnection socketConnection) {
        SocketConnection = socketConnection;
    }



    // TCP Socket connection
    private Socket socket = new Socket();


    /**
     * Get the current TCP Socket connection
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Set the current TCP socket connection
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }





    // UDP Socket connection.
    private DatagramSocket UDPsocketSend;

    /**
     * Get the current UDP Socket
     * @return
     */
    public DatagramSocket getUDPsocketSend() {
        return UDPsocketSend;
    }

    /**
     * Set the current UDP Socket Connection
     * @param UDPsocketSend
     */
    public void setUDPsocketSend(DatagramSocket UDPsocketSend) {
        this.UDPsocketSend = UDPsocketSend;
    }






    // UDP Package
    private DatagramPacket UDPsocketPackage;

    /**
     * Get the current UDP package
     * @return
     */
    public DatagramPacket getUDPsocketPackage() {
        return UDPsocketPackage;
    }

    /**
     * Set the current UDP Package
     * @param UDPsocket
     */
    public void setUDPsocketPackage(DatagramPacket UDPsocket) {
        this.UDPsocketPackage = UDPsocket;
    }



}
