package group1.com.casper_android_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andreas Fransson
 * TODO fix so that video feed starts here
 */
public class VideoStreamActivity extends AppCompatActivity implements imgReady{

    // Joystick
    private RelativeLayout layout_joystick;

    // Image's for Joystick
    private ImageView image_joystick, image_border,videoStream;

    // Debug Text fields for Joystick
    private TextView xAxis, yAxis, angle, distance, direction,responce;

    // Checkbox to start the timed sending of values
    private CheckBox fixedValues;
    private Button request;
    TimerTask task;

    // Make a Joystick
    JoyStick js;




    /**
     * Activate the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_stream);

        videoStream =(ImageView)findViewById(R.id.videoStream);

        // Joystick textfields listening on joystick input.
        xAxis = (TextView)findViewById(R.id.xAxis);
        yAxis = (TextView)findViewById(R.id.yAxis);
        angle = (TextView)findViewById(R.id.angle);
        distance = (TextView)findViewById(R.id.distance);
        direction = (TextView)findViewById(R.id.direction);

        // Checkbox to start sending of fixed values
        fixedValues = (CheckBox)findViewById(R.id.fixedValue);
        request = (Button)findViewById(R.id.request);

        // Joystick
        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);


        UDPsocket videoStream = null;
        try {
            videoStream = new UDPsocket(this,
                    "192.168.10.1",
                    6000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        videoStream.execute();


        // Joystick creation
        js = new JoyStick(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(400, 400);
        js.setLayoutAlpha(200);
        js.setStickAlpha(150);
        js.setOffset(70);
        js.setMinimumDistance(50);


        // Set Joystick listener
        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
//                    xAxis.setText("X : " + String.valueOf(js.getX()));
//                    yAxis.setText("Y : " + String.valueOf(js.getY()));
//                    angle.setText("Angle : " + String.valueOf(js.getAngle()));
//                    distance.setText("Distance : " + String.valueOf(js.getDistance()));

                    int direction = js.get8Direction();
//                    if (direction == JoyStick.STICK_UP) {
//                        VideoStreamActivity.this.direction.setText("Direction : Up");
//                    } else if (direction == JoyStick.STICK_UPRIGHT) {
//                        VideoStreamActivity.this.direction.setText("Direction : Up Right");
//                    } else if (direction == JoyStick.STICK_RIGHT) {
//                        VideoStreamActivity.this.direction.setText("Direction : Right");
//                    } else if (direction == JoyStick.STICK_DOWNRIGHT) {
//                        VideoStreamActivity.this.direction.setText("Direction : Down Right");
//                    } else if (direction == JoyStick.STICK_DOWN) {
//                        VideoStreamActivity.this.direction.setText("Direction : Down");
//                    } else if (direction == JoyStick.STICK_DOWNLEFT) {
//                        VideoStreamActivity.this.direction.setText("Direction : Down Left");
//                    } else if (direction == JoyStick.STICK_LEFT) {
//                        VideoStreamActivity.this.direction.setText("Direction : Left");
//                    } else if (direction == JoyStick.STICK_UPLEFT) {
//                        VideoStreamActivity.this.direction.setText("Direction : Up Left");
//                    } else if (direction == JoyStick.STICK_NONE) {
//                        VideoStreamActivity.this.direction.setText("Direction : Center");
//                    }
                    if(!fixedValues.isChecked()&&Singleton.getInstance().getSocket().isBound()) {
                        // Try sending directional values to socket server
                        try {

                            // Command Flags
                            char driveFlag;
                            char cmdFlag = 'D';
                            char angleFlag;

                            // Bad logics hue hue
                            if (js.getY()>0){
                                driveFlag = 'B';
                            }else if(js.getY()==0){
                                driveFlag = 'I';
                            }else{
                                driveFlag = 'F';
                            }

                            // Y
                            int y = js.getY();
                            if (y < 0) {
                                y = Math.abs(y);
                            }
                            // not over 255
                            if (y > 255) {
                                y = 255;
                            }

                            // X
                            int x = js.getX();
                            if(x < 0){
                                x = Math.abs(x);
                                angleFlag = 'L';
                            }else if (x > 0){
                                angleFlag = 'R';
                            }else{
                                angleFlag = 'I';
                            }
                            // If X is bigger then it can be
                            if (x > 90){
                                x = 90;
                            }

                            // Create the 7 byte Array to send over TCP socket connection
                            byte[] byteArray = {0x44,(byte)driveFlag,(byte)angleFlag,(byte)y,(byte)x,0x0d,0x0a,0x04};

//                            // Debug
//                            System.out.println(">>>>>>DF:"+(byte)driveFlag+"<<<<");
//                            System.out.println(">>>>>>AF:"+(byte)angleFlag+"<<<<");
//                            System.out.println(">>>>>>Y:"+(byte)y+"<<<<");
//                            System.out.println(">>>>>>X:"+(byte)x+"<<<<");
//                            System.out.println(byteArray[5]);
//                            System.out.println(byteArray[6]);

                            // Send the byteArray
                            Singleton.getInstance().getTCPsocket().sendBytes(byteArray);

                        } catch (IOException e) {
                            // Server connection error
                            e.printStackTrace();
                        }

                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {

//                    xAxis.setText("X :");
//                    yAxis.setText("Y :");
//                    angle.setText("Angle :");
//                    distance.setText("Distance :");
//                    direction.setText("Direction :");

                    // Create the 7 byte Array to send over TCP socket connection
                    byte[] byteArray = {0x44,(byte)'I',(byte)'I',(byte)0,(byte)0,0xd,0xa,0x4};

                    // Send the byteArray
                    try {
                        Singleton.getInstance().getTCPsocket().sendBytes(byteArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }



    /**
     * Checkbox
     * @param v
     */
    public void onFixed(View v){
        if(fixedValues.isChecked()&&Singleton.getInstance().getSocket().isBound()){
            new Timer().scheduleAtFixedRate(task = new TimerTask() {
                @Override
                public void run() {
                    try {

                        // Command Flags
                        char driveFlag;
                        char cmdFlag = 'D';
                        char angleFlag;

                        // Bad logics
                        if (js.getY()<0){
                            driveFlag = 'B';
                        }else if(js.getY()==0){
                            driveFlag = 'I';
                        }else{
                            driveFlag = 'F';
                        }

                        // Y
                        int y = js.getY();
                        if (y < 0) {
                            y = Math.abs(y);
                        }
                        // not over 255
                        if (y > 255) {
                            y = 255;
                        }

                        // X
                        int x = js.getX();
                        if(x < 0){
                            x = Math.abs(x);
                            angleFlag = 'L';
                        }else if (x > 0){
                            angleFlag = 'R';
                        }else{
                            angleFlag = 'I';
                        }
                        // If X is bigger then it can be
                        if (x > 90){
                            x = 90;
                        }

                        byte[] byteArray = {0x44, (byte) driveFlag, (byte) angleFlag, (byte) y, (byte) x, 0x0d, 0x0a, 0x04};




                        System.out.println(" ");
                        System.out.println((byte)y);
                        System.out.println((byte)x);
                        System.out.println(" ");
                        // Send the byteArray
                        Singleton.getInstance().getTCPsocket().sendBytes(byteArray);

                    } catch (IOException e) {
                        // Server connection error
                        e.printStackTrace();
                    }
                }
            }, 0, 50);
        }else{
            if(task!=null) {
                task.cancel();
            }
        }
    }




    /**
     * Create the menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    /**
     * Set the menu button actions
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Settings:
                task.cancel();
                System.out.println("Going to settings");
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);

                return true;
            case R.id.logout:
                System.out.println("logging out");
                logout();
                return true;

            default:
                return false;
        }
    }


    /**
     * Log out
     */
    public void logout(){
        // Debug
        if(Singleton.getInstance().getSocket().isBound()) {
            try {
                if(task != null){
                    task.cancel();
                    task = null;
                }

                Singleton.getInstance().getSocket().close();

                // Transfer to main
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

        }

    }



    /**
     * Update the video display
     * @param byteArray
     */
    @Override
    public void imgEvent(byte[] byteArray) {
        // Create a bitmap
        Bitmap bMap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        // Set the imageview to bitmap
        final Bitmap finalBMap = bMap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoStream.setImageBitmap(finalBMap);
            }
        });

    }



    /**
     * Send a kill command to the Server
     * @throws IOException
     */
    public void killSwitch(View v) throws IOException {
        String killswitch = "kill";
        DatagramPacket killSwitchPacket = new DatagramPacket(killswitch.getBytes(),killswitch.length(),Singleton.getInstance().getUDPsocket().getInetAddress(),Singleton.getInstance().getUDPsocket().getPort());
        Singleton.getInstance().getUDPsocket().send(killSwitchPacket);
    }


}



