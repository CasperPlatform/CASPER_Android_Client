package group1.com.casper_android_client;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

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
     * UDPsocket Constructor
     * @param videoStreamInterface
     * @param addr
     * @param port
     * @throws UnknownHostException
     */
    UDPsocket(videoStreamInterface videoStreamInterface,String addr, int port) throws UnknownHostException {
        dstAddress = InetAddress.getByName(addr);
        dstPort = port;
        this.videoStream = videoStreamInterface;

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
            DatagramSocket UDPsocket = new DatagramSocket();
            UDPsocket.setReuseAddress(true);
            UDPsocket.connect(udpAddress);
            //Singleton.getInstance().setUDPsocket(new DatagramSocket());
            //Singleton.getInstance().getUDPsocket().connect(udpAddress);

            //DatagramPacket imgDataPacket = new DatagramPacket(packet, packet.length, Singleton.getInstance().getUDPsocket().getInetAddress(), Singleton.getInstance().getUDPsocket().getPort());
            DatagramPacket imgDataPacket = new DatagramPacket(packet, packet.length, UDPsocket.getInetAddress(), UDPsocket.getPort());

            // Debug
            System.out.println("-------->skickar start cmd<--------");
            // Start cmd
            sendData("start");


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
                isAllPackages = true;
                if (isAllPackages){
                    videoStream.imgRecived(imgArray);
                    System.out.println("fick en bild");
                }
                currentPacket=0;
                isAllPackages = false;
            }

                // if  AsyncTask isAllPackages canceled break out of infinite loop
                if (isCancelled()) {
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
     * @param data
     * @throws IOException
     */
    public void sendData(String data) throws IOException {
        //DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(),data.length(),Singleton.getInstance().getUDPsocket().getInetAddress(),Singleton.getInstance().getUDPsocket().getPort());
        //Singleton.getInstance().getUDPsocket().send(dataAsPackage);
        DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(),data.length(),UDPsocket.getInetAddress(),UDPsocket.getPort());
        UDPsocket.send(dataAsPackage);
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