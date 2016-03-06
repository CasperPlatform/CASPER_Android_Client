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
import java.net.UnknownHostException;

/**
 * Created by AnkanX on 16-03-06.
 */
public class SocketConnection extends AsyncTask<Void, Void, Void> {

    // Convesion variables
    String dstAddress;
    int dstPort;
    String responsemsg = "";
    byte[] messages = new byte[60000];
    String text;


    SocketConnection(String addr, int port) {
        dstAddress = addr;
        dstPort = port;
    }

    @Override
    protected Void doInBackground(Void... v) {
        System.out.println(">>>>>>>>>bound?: "+Singleton.getInstance().getSocket().isBound());

        /**
         *
         * TEST UDP CONNECTION
         *
         * TODO Make functional implementation
         * TODO A no need to write in connection details
         * TODO Set fixed Ports UDP 9998 % TCP 9999
         * TODO Access the UI of whatever element is handleing the connection
         */

            try {

                System.out.println(">>>>>>>>>>>>>>>>>Starting Socket connection!");
                // Start a TCP socket connection
                Singleton.getInstance().setSocket(new Socket(dstAddress, dstPort));
                Singleton.getInstance().getSocket().setReuseAddress(true);

//            String messageStr="Request!";
//            // Socket Port
//            int udpSocketPort = 9998;
//            // New Socket
//            DatagramSocket UDPsocket = new DatagramSocket();
//            // Create an InetAddress
//            InetAddress casper = InetAddress.getByName("192.168.10.1");
//
//            // Output msg lenght
//            int msg_length= messageStr.length();
//            // Msg to bytes convertion
//            byte[] message = messageStr.getBytes();
//            // Package the message
//            DatagramPacket msgPacket = new DatagramPacket(message, msg_length,casper,udpSocketPort);
//            // Send Message over UDP
//            UDPsocket.send(msgPacket);
//
//            // Incomming message
//            byte[] incImage = new byte[60000];
//            // Pacckage incomming message
//            DatagramPacket imagePacket = new DatagramPacket(incImage, incImage.length,casper,udpSocketPort);
//
//            // Get data from incomming buffer
//            UDPsocket.receive(imagePacket);
//
//            // Debug
//            System.out.println(">>>>>>:" + imagePacket.getData().length);
//
//            // convert .JPG image into Bitmap
//            final Bitmap bMap = BitmapFactory.decodeByteArray(imagePacket.getData(), 0, imagePacket.getData().length);

//            // Debug
//            System.out.println(">>>>>>"+bMap.getHeight());
//            System.out.println(">>>>>"+bMap.getWidth());

                // Set imageview to incomming bitmap
//            runOnUiThread(new Runnable() {
//                              @Override
//                              // UI element handling has to be run in UI thread
//                              public void run() {
//                                  testImage.setImageBitmap(bMap);
//                                  testImage.invalidate();
//                              }
//                          }
//
//            );





                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = Singleton.getInstance().getSocket().getInputStream();



                /*
                 * notice:
                 * inputStream.read() will block if no data return
                */
                while ((bytesRead = inputStream.read(buffer)) != -1) {



                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    responsemsg += byteArrayOutputStream.toString("UTF-8");

                    if(isCancelled()){
                        break;
                    }

                }


            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                responsemsg = "UnknownHostException: " + e.toString();


            } catch (IOException e) {
                // TODO Auto-generated catch block


                e.printStackTrace();
                responsemsg = "IOException: " + e.toString();
            } finally {

                if (Singleton.getInstance().getSocket() != null) {
                    try {
                        // Try closeing the socket connection.
                        Singleton.getInstance().getSocket().close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


            return null;



    }

    /**
     * Send a bytearray over TCP socket connection.
     * @param myByteArray
     * @throws IOException
     */
    public void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, 0, myByteArray.length);
    }

    /**
     * send bytearray over TCP socket connection.
     * @param myByteArray
     * @param start
     * @param len
     * @throws IOException
     */
    public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);

        OutputStream out = Singleton.getInstance().getSocket().getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        if (len > 0) {
            dos.write(myByteArray);
        }
    }

}