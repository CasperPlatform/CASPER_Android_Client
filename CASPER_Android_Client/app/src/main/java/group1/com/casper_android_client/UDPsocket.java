package group1.com.casper_android_client;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Andreas Fransson on 16-03-06.
 * TODO fix construcor so that the right type of connection is established.
 *
 */
public class UDPsocket extends AsyncTask<Void, Void, Void> {

    // Convesion variables
    InetAddress dstAddress;
    int dstPort;

    UDPsocket(String addr, int port) throws UnknownHostException {
        dstAddress = InetAddress.getByName(addr);
        dstPort = port;

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
        byte[] messages = new byte[8000];

        try {
            Singleton.getInstance().setUDPsocket(new DatagramSocket(dstPort,dstAddress));


            // Output msg lenght
            // int msg_length = messageStr.length();
            // Msg to bytes convertion
            // byte[] message = messageStr.getBytes();
            // Package the message
            // DatagramPacket msgPacket = new DatagramPacket(message, msg_length, casper, udpSocketPort);
            // Send Message over UDP
            // UDPsocket.send(msgPacket);

            // Incomming message
            byte[] incPackage = new byte[8000];


            while (true) {

                // Pacckage incomming message
                //DatagramPacket imagePacket = new DatagramPacket(incPackage, incPackage.length, casper, udpSocketPort);
                // Get data from incomming buffer
                //UDPsocket.receive(imagePacket);
                // Debug
                //System.out.println(">>>>>>:" + imagePacket.getData().length);
                DatagramPacket test = new DatagramPacket(incPackage, incPackage.length, Singleton.getInstance().getUDPsocket().getInetAddress(), Singleton.getInstance().getUDPsocket().getPort());
                String text = test.getData().toString();
                System.out.println("Test data output ----->" + text);

            }
            // convert .JPG image into Bitmap
            //final Bitmap bMap = BitmapFactory.decodeByteArray(imagePacket.getData(), 0, imagePacket.getData().length);

            // Debug
            //System.out.println(">>>>>>" + bMap.getHeight());
            //System.out.println(">>>>>" + bMap.getWidth());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }




    public void sendData(String data) throws IOException {
        DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(),data.length());
        Singleton.getInstance().getUDPsocket().send(dataAsPackage);

    }






}