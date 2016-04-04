package group1.com.casper_android_client;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
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
    private imgReady myImageReady;
    private boolean is;

    /**
     * UDPsocket Constructor
     * @param ir
     * @param addr
     * @param port
     * @throws UnknownHostException
     */
    UDPsocket(imgReady ir,String addr, int port) throws UnknownHostException {
        dstAddress = InetAddress.getByName(addr);
        dstPort = port;
        this.myImageReady = ir;

    }


    /**
     * Settup UDP connection.
     * @param v
     * @return
     */
    @Override
    protected Void doInBackground(Void... v) {

        byte[] packet = new byte[8006];
        byte HEADER_FLAG = 0x01;
        byte PACKET_HEADER_FLAG = 0x02;
        int packageCount = 0;
        int currentPacket = 0;
        byte[] imgArray = new byte[0];

        try {

            InetSocketAddress udpAddress = new InetSocketAddress(dstAddress,dstPort);
            Singleton.getInstance().setUDPsocket(new DatagramSocket());
            Singleton.getInstance().getUDPsocket().connect(udpAddress);

            DatagramPacket imgDataPacket = new DatagramPacket(packet, packet.length, Singleton.getInstance().getUDPsocket().getInetAddress(), Singleton.getInstance().getUDPsocket().getPort());

            sendData("start");


            while (true) {
            Singleton.getInstance().getUDPsocket().receive(imgDataPacket);
            packet = new byte[imgDataPacket.getLength()];
            packet = imgDataPacket.getData();

            if(packet[0] == HEADER_FLAG){
                currentPacket = 0;
                imgArray = new byte[(int)getNumberFromBytes(Arrays.copyOfRange(packet,7,11))];
                packageCount = (int)packet[6];

            }

            if(packet[0] == PACKET_HEADER_FLAG){
                try {
                    System.arraycopy(packet, 6, imgArray, (int) packet[5] * 8000, imgDataPacket.getLength() - 6);
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("oh noes....");
                }
                currentPacket  = (int)packet[5];
                System.out.println("package: " + (int)packet[5]);
            }

            if(currentPacket == packageCount-1){
                is = true;
                if (is){
                    myImageReady.imgEvent(imgArray);
                    System.out.println("fick en bild");
                }
                currentPacket=0;
                is = false;
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
        DatagramPacket dataAsPackage = new DatagramPacket(data.getBytes(),data.length(),Singleton.getInstance().getUDPsocket().getInetAddress(),Singleton.getInstance().getUDPsocket().getPort());
        Singleton.getInstance().getUDPsocket().send(dataAsPackage);
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