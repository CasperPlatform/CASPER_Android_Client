package group1.com.casper_android_client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Andreas Fransson on 16-03-06.
 * TODO fix construcor so that the right type of connection is established.
 *
 */
public class UDPsocket extends AsyncTask<Void, Void, Void> {

    // Convesion variables
    String dstAddress;
    int dstPort;
    boolean dstUDP;
    String responsemsg = "";

    UDPsocket(String addr, int port, boolean udpAllso) {
        dstAddress = addr;
        dstPort = port;
        dstUDP = udpAllso;
    }

    @Override
    protected Void doInBackground(Void... v) {
        System.out.println(">>>>>>>>>bound?: " + Singleton.getInstance().getSocket().isBound());

        /**
         *
         * TEST UDP CONNECTION
         *
         * TODO Make functional implementation
         * TODO A no need to write in connection details
         * TODO Set fixed Ports UDP 9998 % TCP 9999
         * TODO Access the UI of whatever element is handleing the connection
         */
        byte[] messages = new byte[60000];
        String text = "";

        try {

            String messageStr = "Request!";
            // Socket Port
            int udpSocketPort = dstPort;
            // New Socket
            DatagramSocket UDPsocket = new DatagramSocket();
            // Create an InetAddress
            InetAddress casper = InetAddress.getByName("192.168.10.1");

            // Output msg lenght
            // int msg_length = messageStr.length();
            // Msg to bytes convertion
            // byte[] message = messageStr.getBytes();
            // Package the message
            // DatagramPacket msgPacket = new DatagramPacket(message, msg_length, casper, udpSocketPort);
            // Send Message over UDP
            // UDPsocket.send(msgPacket);

            // Incomming message
            byte[] incImage = new byte[8000];

            while (true) {

                // Pacckage incomming message
                //DatagramPacket imagePacket = new DatagramPacket(incImage, incImage.length, casper, udpSocketPort);

                // Get data from incomming buffer
                //UDPsocket.receive(imagePacket);

                // Debug
                //System.out.println(">>>>>>:" + imagePacket.getData().length);
                DatagramPacket test = new DatagramPacket(incImage, incImage.length, casper, udpSocketPort);
                text = text + test.getData().toString();
                System.out.println(text);

            }
            // convert .JPG image into Bitmap
            //final Bitmap bMap = BitmapFactory.decodeByteArray(imagePacket.getData(), 0, imagePacket.getData().length);

            // Debug
            //System.out.println(">>>>>>" + bMap.getHeight());
            //System.out.println(">>>>>" + bMap.getWidth());


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}