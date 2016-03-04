package group1.com.casper_android_client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created by Andreas Fransson
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    private String userName;
    private String passWord;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    // Socket connection
    private Socket socket = new Socket();
    private DatagramPacket UDPsocketPackage;
    private DatagramSocket UDPsocketSend;

    public DatagramSocket getUDPsocketSend() {
        return UDPsocketSend;
    }

    public void setUDPsocketSend(DatagramSocket UDPsocketSend) {
        this.UDPsocketSend = UDPsocketSend;
    }



    public DatagramPacket getUDPsocketPackage() {
        return UDPsocketPackage;
    }

    public void setUDPsocketPackage(DatagramPacket UDPsocket) {
        this.UDPsocketPackage = UDPsocket;
    }


    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public static Singleton getInstance() {
        return ourInstance;
    }

    public static void setOurInstance(Singleton ourInstance) {
        Singleton.ourInstance = ourInstance;
    }


    private Singleton() {

    }
}
