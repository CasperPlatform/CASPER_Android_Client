package group1.com.casper_android_client;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andreas Fransson on 16-03-06.
 *
 */
public class DriveSocket extends AsyncTask<Void, Void, Void> {

    // Convesion variables
    InetAddress dstAddress;
    int dstPort;
    private videoStreamInterface videoStream;
    private boolean isAllPackages;
    DatagramSocket DriveSocket;

    /**
     * UDPsocketLocal Constructor
     *
     * @param addr
     * @param port
     * @throws UnknownHostException
     */
    DriveSocket(String addr, int port) throws UnknownHostException {
        dstAddress = InetAddress.getByName(addr);
        dstPort = port;
        System.out.println("starting async task!");

    }


    /**
     * Settup UDP connection.
     *
     * @param v
     * @return
     */
    @Override
    protected Void doInBackground(Void... v) {

        byte[] packet = new byte[1024];

        try {


            // Convert address and port  to InetSocketAddress
            InetSocketAddress udpAddress = new InetSocketAddress(dstAddress, dstPort);
            DriveSocket = new DatagramSocket();
            DriveSocket.connect(udpAddress);
            System.out.println("---------->" + DriveSocket.isConnected());
            System.out.println(DriveSocket.getInetAddress());
            System.out.println(DriveSocket.getRemoteSocketAddress());

            //DatagramPacket imgDataPacket = new DatagramPacket(packet, packet.length, Singleton.getInstance().getUDPsocket().getInetAddress(), Singleton.getInstance().getUDPsocket().getPort());
            DatagramPacket imgDataPacket = new DatagramPacket(packet, packet.length, DriveSocket.getInetAddress(), DriveSocket.getPort());

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        sendCmd(Singleton.getInstance().getDrivePackage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }, 0, 50);


            while (true) {
                //Singleton.getInstance().getUDPsocket().receive(imgDataPacket);
                DriveSocket.receive(imgDataPacket);
                packet = new byte[imgDataPacket.getLength()];
                packet = imgDataPacket.getData();

                // if  AsyncTask isAllPackages canceled break out of infinite loop
                if (isCancelled()) {
                    timer.cancel();
                    timer.purge();
                    DriveSocket.disconnect();
                    DriveSocket.close();
                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Sends a message to the Server
     *
     * @param cmd
     * @throws IOException
     */
    public void sendCmd(byte[] cmd) throws IOException {
        //DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(),data.length(),Singleton.getInstance().getUDPsocket().getInetAddress(),Singleton.getInstance().getUDPsocket().getPort());
        //Singleton.getInstance().getUDPsocket().send(dataAsPackage);
        try {
            DatagramPacket dataAsPackage = new DatagramPacket(cmd, cmd.length, DriveSocket.getInetAddress(), DriveSocket.getPort());
            DriveSocket.send(dataAsPackage);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}