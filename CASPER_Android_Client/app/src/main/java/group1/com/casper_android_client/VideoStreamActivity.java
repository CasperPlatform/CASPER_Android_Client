package group1.com.casper_android_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andreas Fransson
 * TODO fix so that video feed starts here
 */
public class VideoStreamActivity extends AppCompatActivity implements videoStreamInterface {

    boolean is = false;
    // Joystick
    private RelativeLayout layout_joystick;

    // Image's for Joystick
    private ImageView image_joystick, image_border,videoStream,mapView;

    // Debug Text fields for Joystick
    private TextView xAxis, yAxis, angle, distance, direction,responce, videoStatusMsg;
    private boolean videoStatus;

    // Checkbox to start the timed sending of values
    private CheckBox fixedValues;
    Timer task = new Timer();

    // Make a Joystick
    JoyStick driveStick;
    UDPsocket videoStreamTask = null;




    /**
     * Activate the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_stream);

        // Background imageView for displaying video
        videoStream = (ImageView)findViewById(R.id.videoStream);

        videoStatusMsg = (TextView)findViewById(R.id.videoStatus);

        // Joystick textfields listening on joystick input.
        xAxis = (TextView)findViewById(R.id.xAxis);
        yAxis = (TextView)findViewById(R.id.yAxis);
        angle = (TextView)findViewById(R.id.angle);
        distance = (TextView)findViewById(R.id.distance);
        direction = (TextView)findViewById(R.id.direction);

        // Checkbox to start sending of fixed values
        fixedValues = (CheckBox)findViewById(R.id.fixedValue);
        mapView = (ImageView)findViewById(R.id.mapView);

        // Joystick
        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

            try{
                videoStreamTask = new UDPsocket(this,
                        "192.168.10.1",
                        6000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            videoStreamTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            videoStreamTask.execute();
        }





        // Drive Joystick creation and settings
        driveStick = new JoyStick(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        driveStick.setStickSize(150, 150);
        driveStick.setLayoutSize(400, 400);
        driveStick.setLayoutAlpha(200);
        driveStick.setStickAlpha(150);
        driveStick.setOffset(70);
        driveStick.setMinimumDistance(50);


        // Set drive Joystick listener
        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                driveStick.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {

                    int direction = driveStick.get8Direction();

                        // Command Flags
                        char driveFlag;
                        char angleFlag;

                        // Bad logics
                        if (driveStick.getY() > 0) {
                            driveFlag = 'B';
                        } else if (driveStick.getY() == 0) {
                            driveFlag = 'I';
                        } else {
                            driveFlag = 'F';
                        }

                        // Y
                        int y = driveStick.getY();
                        if (y < 0) {
                            y = Math.abs(y);
                        }
                        // not over 255
                        if (y > 255) {
                            y = 255;
                        }

                        // X
                        int x = driveStick.getX();
                        if (x < 0) {
                            x = Math.abs(x);
                            angleFlag = 'L';
                        } else if (x > 0) {
                            angleFlag = 'R';
                        } else {
                            angleFlag = 'I';
                        }
                        // If X is bigger then it can be
                        if (x > 90) {
                            x = 90;
                        }
                        byte[] byteArray = {0x44, (byte) driveFlag, (byte) angleFlag, (byte) y, (byte) x, 0x0d, 0x0a, 0x04};
                        Singleton.getInstance().setTcpPackage(byteArray);

                        // Debug
//                                System.out.println(" ");
//                                System.out.println((byte)y);
//                                System.out.println((byte)x);
//                                System.out.println(" ");
                        // Send the byteArray
                }else if(arg1.getAction() == MotionEvent.ACTION_UP){
                    byte[] byteArray = {0x44, (byte) 'I', (byte) 'I', (byte) 0, (byte) 0, 0x0d, 0x0a, 0x04};
                    Singleton.getInstance().setTcpPackage(byteArray);
                }
                return true;
            }
        });


        task.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Singleton.getInstance().getTCPsocket().sendBytes(Singleton.getInstance().getTcpPackage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, 0, 50);


    }



//
//    /**
//     * Checkbox
//     * @param v
//     */
//    public void onFixed(View v){
//        if(fixedValues.isChecked()&&Singleton.getInstance().getSocket().isBound()){
//            task.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    try {
//
//                        // Command Flags
//                        char driveFlag;
//                        char cmdFlag = 'D';
//                        char angleFlag;
//
//                        // Bad logics
//                        if (driveStick.getY()<0){
//                            driveFlag = 'B';
//                        }else if(driveStick.getY()==0){
//                            driveFlag = 'I';
//                        }else{
//                            driveFlag = 'F';
//                        }
//
//                        // Y
//                        int y = driveStick.getY();
//                        if (y < 0) {
//                            y = Math.abs(y);
//                        }
//                        // not over 255
//                        if (y > 255) {
//                            y = 255;
//                        }
//
//                        // X
//                        int x = driveStick.getX();
//                        if(x < 0){
//                            x = Math.abs(x);
//                            angleFlag = 'L';
//                        }else if (x > 0){
//                            angleFlag = 'R';
//                        }else{
//                            angleFlag = 'I';
//                        }
//                        // If X is bigger then it can be
//                        if (x > 90){
//                            x = 90;
//                        }
//
//                        byte[] byteArray = {0x44, (byte) driveFlag, (byte) angleFlag, (byte) y, (byte) x, 0x0d, 0x0a, 0x04};
//
//                        // Debug
//                        System.out.println(" ");
//                        System.out.println((byte)y);
//                        System.out.println((byte)x);
//                        System.out.println(" ");
//                        // Send the byteArray
//                        Singleton.getInstance().getTCPsocket().sendBytes(byteArray);
//
//                    } catch (IOException e) {
//                        // Server connection error
//                        e.printStackTrace();
//                    }
//                }
//            }, 0, 50);
//        }else{
//                task.cancel();
//                task.purge();
//
//        }
//    }




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
                task.purge();
                System.out.println("Going to settings");
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);

                return true;
            case R.id.logout:
                System.out.println("logging out");
                logout();
                return true;
            case android.R.id.home:
                System.out.println("------>Pressed android.native back navigation button<----");
                try {
                    task.cancel();
                    task.purge();
                    task = null;
                    videoStreamTask.startStop("stop");
                    videoStreamTask.cancel(true);

                    System.out.println("----->" + videoStreamTask.isCancelled());
                    finish();
                    NavUtils.navigateUpFromSameTask(this);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch(NullPointerException e){
                    System.out.println("no can do");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    task.cancel();
                    task.purge();
                    task = null;

                videoStreamTask.startStop("stop");
                videoStreamTask.UDPsocket.disconnect();
                videoStreamTask.UDPsocket.close();
                videoStreamTask.cancel(true);
                Singleton.getInstance().getSocket().close();

                // Transfer to main
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
    public void imgRecived(byte[] byteArray) {

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




    public void bigsmall(View v){

        final float growTo = 1.5f;
        final long duration = 1200;
        AnimationSet growAndShrink = new AnimationSet(true);

        if(!is) {
            is = true;
            ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            grow.setDuration(duration / 2);
            growAndShrink.addAnimation(grow);
        }else {
            is = false;
            ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            shrink.setDuration(duration / 2);
            shrink.setStartOffset(duration / 2);
            growAndShrink.addAnimation(shrink);
        }
        growAndShrink.setFillAfter(true);
        growAndShrink.setInterpolator(new LinearInterpolator());
        mapView.startAnimation(growAndShrink);

    }

}



