//package group1.com.casper_android_client;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.AsyncTask;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
///**
// * Created by AnkanX on 16-03-06.
// */
//public class SocketConnection extends AsyncTask<Void, Void, Void> {
//
//    // Convesion variables
//    String dstAddress;
//    int dstPort;
//    String responsemsg = "";
//    byte[] messages = new byte[60000];
//    String text;
//
//
//    SocketConnection(String addr, int port){
//        dstAddress = addr;
//        dstPort = port;
//    }
//
//    @Override
//    protected Void doInBackground(Void... v) {
//
//
//        /**
//         *
//         * TEST UDP CONNECTION
//         *
//         * TODO Make functional implementation
//         * TODO A no need to write in connection details
//         * TODO Set fixed Ports UDP 9998 % TCP 9999
//         * TODO Access the UI of whatever element is handleing the connection
//         */
//
//        try {
//            // Start a TCP socket connection
//            Singleton.getInstance().setSocket(new Socket(dstAddress, dstPort));
//
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
//
//            // Debug
//            System.out.println(">>>>>>"+bMap.getHeight());
//            System.out.println(">>>>>"+bMap.getWidth());
//
//            // Set imageview to incomming bitmap
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
//
//
//
//            runOnUiThread(new Runnable() {
//                              @Override
//                              public void run() {
//                                  loading.setVisibility(errorView.INVISIBLE);
//                                  setVisable(errorView);
//                                  error.setTextColor(Color.rgb(0, 255, 0));
//                                  error.setText("server is reacheble");
//                              }
//                          }
//
//            );
//
//
//
//            ByteArrayOutputStream byteArrayOutputStream =
//                    new ByteArrayOutputStream(1024);
//            byte[] buffer = new byte[1024];
//
//            int bytesRead;
//            InputStream inputStream = Singleton.getInstance().getSocket().getInputStream();
//
//
//            // Get server address as message
//            runOnUiThread(new Runnable() {
//                              @Override
//                              public void run() {
//                                  loading.setVisibility(errorView.INVISIBLE);
//                                  response.setText("Server respond -> " + Singleton.getInstance().getSocket().getInetAddress().toString());
//                              }
//                          }
//
//            );
//
//                /*
//                 * notice:
//                 * inputStream.read() will block if no data return
//                */
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//                responsemsg += byteArrayOutputStream.toString("UTF-8");
//            }
//
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            // e.printStackTrace();
//            responsemsg = "UnknownHostException: " + e.toString();
//            runOnUiThread(new Runnable() {
//                              @Override
//                              public void run() {
//                                  loading.setVisibility(errorView.INVISIBLE);
//                                  setVisable(errorView);
//                                  error.setTextColor(Color.rgb(255,0,0));
//                                  error.setText("server is unreacheble!");
//                              }
//                          }
//
//            );
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            // Show user there was a problem
//            runOnUiThread(new Runnable() {
//                              @Override
//                              public void run() {
//                                  loading.setVisibility(errorView.INVISIBLE);
//                                  setVisable(errorView);
//                                  error.setTextColor(Color.rgb(255,0,0));
//                                  error.setText("server is unreacheble!");
//                              }
//                          }
//
//            );
//            e.printStackTrace();
//            responsemsg = "IOException: " + e.toString();
//        } finally {
//
//            if (Singleton.getInstance().getSocket() != null) {
//                try {
//                    // Try closeing the socket connection.
//                    Singleton.getInstance().getSocket().close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
//}