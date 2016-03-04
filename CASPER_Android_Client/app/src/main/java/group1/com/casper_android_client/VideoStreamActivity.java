package group1.com.casper_android_client;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andreas Fransson
 */
public class VideoStreamActivity extends AppCompatActivity {
    private RelativeLayout layout_joystick;
    private ImageView image_joystick, image_border;
    private TextView xAxis, yAxis, angle, distance, direction;
    private CheckBox fixedValues;
    TimerTask task;

    JoyStick js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_stream);

        // Joystick textfields listening on joystick input.
        xAxis = (TextView)findViewById(R.id.xAxis);
        yAxis = (TextView)findViewById(R.id.yAxis);
        angle = (TextView)findViewById(R.id.angle);
        distance = (TextView)findViewById(R.id.distance);
        direction = (TextView)findViewById(R.id.direction);

        fixedValues = (CheckBox)findViewById(R.id.fixedValue);

        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

        js = new JoyStick(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(400, 400);
        js.setLayoutAlpha(200);
        js.setStickAlpha(150);
        js.setOffset(70);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    xAxis.setText("X : " + String.valueOf(js.getX()));
                    yAxis.setText("Y : " + String.valueOf(js.getY()));
                    angle.setText("Angle : " + String.valueOf(js.getAngle()));
                    distance.setText("Distance : " + String.valueOf(js.getDistance()));

                    int direction = js.get8Direction();
                    if (direction == JoyStick.STICK_UP) {
                        VideoStreamActivity.this.direction.setText("Direction : Up");
                    } else if (direction == JoyStick.STICK_UPRIGHT) {
                        VideoStreamActivity.this.direction.setText("Direction : Up Right");
                    } else if (direction == JoyStick.STICK_RIGHT) {
                        VideoStreamActivity.this.direction.setText("Direction : Right");
                    } else if (direction == JoyStick.STICK_DOWNRIGHT) {
                        VideoStreamActivity.this.direction.setText("Direction : Down Right");
                    } else if (direction == JoyStick.STICK_DOWN) {
                        VideoStreamActivity.this.direction.setText("Direction : Down");
                    } else if (direction == JoyStick.STICK_DOWNLEFT) {
                        VideoStreamActivity.this.direction.setText("Direction : Down Left");
                    } else if (direction == JoyStick.STICK_LEFT) {
                        VideoStreamActivity.this.direction.setText("Direction : Left");
                    } else if (direction == JoyStick.STICK_UPLEFT) {
                        VideoStreamActivity.this.direction.setText("Direction : Up Left");
                    } else if (direction == JoyStick.STICK_NONE) {
                        VideoStreamActivity.this.direction.setText("Direction : Center");
                    }
                    if(!fixedValues.isChecked()&&Singleton.getInstance().getSocket().isBound()) {
                        // Try sending directional values to socket server
                        try {

                            // Command Flags
                            char driveFlag;
                            char cmdFlag = 'D';
                            char angleFlag;

                            // Bad logics hue hue
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

                            byte[] byteArray = {0x44,(byte)driveFlag,(byte)angleFlag,(byte)y,(byte)x,0xd,0xa};

                            System.out.println(">>>>>>DF:"+(byte)driveFlag+"<<<<");
                            System.out.println(">>>>>>AF:"+(byte)angleFlag+"<<<<");
                            System.out.println(">>>>>>Y:"+(byte)y+"<<<<");
                            System.out.println(">>>>>>X:"+(byte)x+"<<<<");
                            System.out.println(byteArray[5]);
                            System.out.println(byteArray[6]);

                            // Send the byteArray
                            sendBytes(byteArray);

                        } catch (IOException e) {
                            // Server connection error
                            e.printStackTrace();
                        }

                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    xAxis.setText("X :");
                    yAxis.setText("Y :");
                    angle.setText("Angle :");
                    distance.setText("Distance :");
                    direction.setText("Direction :");
                }
                return true;
            }
        });



    }

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

                            byte[] byteArray = {0x44, (byte) driveFlag, (byte) angleFlag, (byte) y, (byte) x, 0xd, 0xa};




                        System.out.println(" ");
                        System.out.println((byte)y);
                        System.out.println((byte)x);
                        System.out.println(" ");
                        // Send the byteArray
                        sendBytes(byteArray);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

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

    // Quit connection
    public void logout(){
        // Debug
        System.out.println("--------------------->Bound? " + Singleton.getInstance().getSocket().isBound());
        System.out.println("---------------------->closed? " + Singleton.getInstance().getSocket().isClosed());
        if(Singleton.getInstance().getSocket().isBound()) {
            // Close Socket
            try {
                task.cancel();
                finish();
                Singleton.getInstance().getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Transfer to main
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{

        }

    }


    public void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, 0, myByteArray.length);
    }

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
