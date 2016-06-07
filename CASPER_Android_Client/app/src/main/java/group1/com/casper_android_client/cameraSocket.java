package group1.com.casper_android_client;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Andreas Fransson on 16-03-06.
 * TODO fix construcor so that the right type of connection is established.
 *
 */
public class cameraSocket extends AsyncTask<Void, Void, Void> {

    // Convesion variables
    String dstAddress;
    int dstPort;
    boolean dstUDP;
    String responsemsg = "";

    cameraSocket(String addr, int port) {
        dstAddress = addr;
        dstPort = port;

        try{
            byte[] tokenArray = Singleton.getInstance().getLoggedInUser().getToken().getBytes();
            byte[] byteArray = new byte[24];
            byteArray[0] = 0x44;
            System.arraycopy(tokenArray, 0, byteArray, 1, tokenArray.length);
            byteArray[17] = (byte) 'I';
            byteArray[18] = (byte) 'I';
            byteArray[19] = (byte) 0;
            byteArray[20] = (byte) 0;
            byteArray[21] = (byte) 0x0d;
            byteArray[22] = (byte) 0x0a;
            byteArray[23] = (byte) 0x04;

            Singleton.getInstance().setDrivePackage(byteArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(Void... v) {
        System.out.println(">>>>>>>>>bound?: "+ Singleton.getInstance().getCameraSocket().isBound());

        /**
         *
         *
         * TODO Set fixed Ports TCP 9999
         * TODO Access the UI of whatever element is handleing the connection via interface
         */

            try {

                System.out.println(">>>>>>>>>>>>>>>>>Starting Socket connection!");
                // Start a TCP socket connection
                Singleton.getInstance().setCameraSocket(new Socket(dstAddress, dstPort));


                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = Singleton.getInstance().getCameraSocket().getInputStream();



                /*
                 * notice:
                 * inputStream.read() will block if no data return
                */
                while ((bytesRead = inputStream.read(buffer)) != -1) {


                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    responsemsg += byteArrayOutputStream.toString("UTF-8");
                    Singleton.getInstance().setSocketData(responsemsg);

                    System.out.println("---------------------------------------------------------------------------------->" + responsemsg);

                    if (isCancelled()) {
                        break;
                    }

                }


            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                responsemsg = "UnknownHostException: " + e.toString();
                System.out.println(">>>>>>>>>>>>>>>>>Starting Socket connection! error recived at catch 1");

            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println(">>>>>>>>>>>>>>>>>Starting Socket connection! error recived at catch 2");
                e.printStackTrace();
                responsemsg = "IOException: " + e.toString();
            } finally {

                if (Singleton.getInstance().getCameraSocket() != null) {
                    try {
                        // Try closeing the socket connection.
                        Singleton.getInstance().getCameraSocket().close();
                    } catch (IOException e) {
                        System.out.println(">>>>>>>>>>>>>>>>>Starting Socket connection! error recived at catch 3");
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

        OutputStream out = Singleton.getInstance().getCameraSocket().getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        if (len > 0) {
            dos.write(myByteArray);
        }
    }

}