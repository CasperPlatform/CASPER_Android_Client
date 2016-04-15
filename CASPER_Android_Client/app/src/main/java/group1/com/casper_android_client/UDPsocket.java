package group1.com.casper_android_client;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andreas Fransson on 16-03-06.
 *
 */
public class UDPsocket extends AsyncTask<Void, Void, Void> {

    // Convesion variables
    InetAddress dstAddress;
    int dstPort;
    private videoStreamInterface videoStream;
    private boolean isAllPackages;
    DatagramSocket UDPsocket;
    /**
     * UDPsocketLocal Constructor
     * @param videoStreamInterface
     * @param addr
     * @param port
     * @throws UnknownHostException
     */
    UDPsocket(videoStreamInterface videoStreamInterface,String addr, int port) throws UnknownHostException {
        dstAddress = InetAddress.getByName(addr);
        dstPort = port;
        this.videoStream = videoStreamInterface;
        System.out.println("starting async task!");

    }


    /**
     * Settup UDP connection.
     * @param v
     * @return
     */
    @Override
    protected Void doInBackground(Void... v) {

        byte[] packet = new byte[8006];
        byte[] sendPacket = new byte[1024];
        byte HEADER_FLAG = 0x01;
        byte PACKET_HEADER_FLAG = 0x02;
        int packageCount = 0;
        int currentPacket = 0;
        byte[] imgArray = new byte[0];

        try {


            // Convert address and port  to InetSocketAddress
            InetSocketAddress udpAddress = new InetSocketAddress(dstAddress,dstPort);
            UDPsocket = new DatagramSocket();
            //UDPsocketLocal.setReuseAddress(true);
            UDPsocket.connect(udpAddress);
            System.out.println("---------->" + UDPsocket.isConnected());
            System.out.println(UDPsocket.getInetAddress());
            System.out.println(UDPsocket.getRemoteSocketAddress());
            //Singleton.getInstance().setUDPsocket(new DatagramSocket());
            //Singleton.getInstance().getUDPsocket().connect(udpAddress);

            //DatagramPacket imgDataPacket = new DatagramPacket(packet, packet.length, Singleton.getInstance().getUDPsocket().getInetAddress(), Singleton.getInstance().getUDPsocket().getPort());
            DatagramPacket imgDataPacket = new DatagramPacket(packet, packet.length, UDPsocket.getInetAddress(), UDPsocket.getPort());

            // Debug
            System.out.println("-------->skickar start cmd<--------");
            // Start cmd
            char startFlag = 'S';
            char stopFlag = 's';
            char idleFlag = 'I';

            byte[] byteArray = {0x01};
            byte[] endArray = {(byte) startFlag, 0x0d, 0x0a};
            byte[] tokenArray = Singleton.getInstance().getLoggedInUser().getToken().getBytes();
            byte[] send = new byte[20];
            System.arraycopy(byteArray, 0, send, 0, byteArray.length);
            System.arraycopy(tokenArray, 0, send, 1, tokenArray.length);
            System.arraycopy(endArray, 0, send, 17, endArray.length);
            System.out.println(Arrays.toString(send));
            sendCmd(send);
            byte[] idle = send;
            idle[17] = (byte)idleFlag;
            final byte[] idle2 = idle;


            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        sendCmd(idle2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }, 0, 5000);



            while (true) {
            //Singleton.getInstance().getUDPsocket().receive(imgDataPacket);
            UDPsocket.receive(imgDataPacket);
            packet = new byte[imgDataPacket.getLength()];
            packet = imgDataPacket.getData();


            // If the package isAllPackages a Header package reset variables and setup for new image
            if(packet[0] == HEADER_FLAG){
                currentPacket = 0;
                imgArray = new byte[(int)getNumberFromBytes(Arrays.copyOfRange(packet,7,11))];
                packageCount = (int)packet[6];

            }

            // If the package isAllPackages a image package read the header and store the image information
            if(packet[0] == PACKET_HEADER_FLAG){
                try {
                    System.arraycopy(packet, 6, imgArray, (int) packet[5] * 8000, imgDataPacket.getLength() - 6);
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("oh noes....");
                }
                currentPacket  = (int)packet[5];
                System.out.println("package: " + (int)packet[5]);
            }


            // If the number of packages recived isAllPackages equal to the number of packages in the imgage total
            // send the image array over the videoStreamInterface to the UI thread.
            if(currentPacket == packageCount-1){

                    System.out.println("image lenght" + imgArray.length);
                    videoStream.imgRecived(imgArray);
                    System.out.println("fick en bild");

                currentPacket=0;
            }

                // if  AsyncTask isAllPackages canceled break out of infinite loop
                if (isCancelled()) {
                    timer.cancel();
                    timer.purge();
                    UDPsocket.disconnect();
                    UDPsocket.close();
                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * Sends a message to the Server
     * @param data
     * @throws IOException
     */
    public void sendData(String data) throws IOException {
        //DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(),data.length(),Singleton.getInstance().getUDPsocket().getInetAddress(),Singleton.getInstance().getUDPsocket().getPort());
        //Singleton.getInstance().getUDPsocket().send(dataAsPackage);
        try {
            DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(), data.length(), UDPsocket.getInetAddress(), UDPsocket.getPort());
            UDPsocket.send(dataAsPackage);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        // Debug
        System.out.println("inne i send");
    }

    public void startStop(String startStop) throws IOException, JSONException {
        char startFlag = 'S';
        char stopFlag = 's';
        byte[] tokenArray = Singleton.getInstance().getLoggedInUser().getToken().getBytes();
        byte[] send = new byte[20];
        send[0] = 0x01;
        System.arraycopy(tokenArray, 0, send, 1, tokenArray.length);
        send[18] = 0x0d;
        send[19] = 0x0a;

        if (startStop.equals("start")){
            send[17] = (byte) startFlag;

        }else if(startStop.equals("stop")){
            send[17] = (byte) stopFlag;
        }

        sendCmd(send);
    }

    /**
     * Sends a message to the Server
     * @param cmd
     * @throws IOException
     */
    public void sendCmd(byte[] cmd) throws IOException {
        //DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(),data.length(),Singleton.getInstance().getUDPsocket().getInetAddress(),Singleton.getInstance().getUDPsocket().getPort());
        //Singleton.getInstance().getUDPsocket().send(dataAsPackage);
        try {
            DatagramPacket dataAsPackage = new DatagramPacket(cmd, cmd.length, UDPsocket.getInetAddress(), UDPsocket.getPort());
            UDPsocket.send(dataAsPackage);
        }catch (NullPointerException e){
           e.printStackTrace();
        }
        // Debug
        System.out.println("inne i send");
    }


    /**
     * Takes a Bytearray and converts it to an "unsigned int" represented in Java by a long
     * @param bytes
     * @return
     */
    public long getNumberFromBytes(byte[] bytes){
        int first,second,third,forth;
        long result;

        first =  0x000000FF & (int)bytes[0];
        second = 0x000000FF & (int)bytes[1];
        third =  0x000000FF & (int)bytes[2];
        forth =  0x000000FF & (int)bytes[3];
        result = (first<<24|second<<16|third<<8|forth)& 0xFFFFFFFFL;
        return result;
    }
}