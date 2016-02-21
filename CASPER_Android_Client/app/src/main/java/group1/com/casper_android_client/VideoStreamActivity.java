package group1.com.casper_android_client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Andreas Fransson
 */
public class VideoStreamActivity extends AppCompatActivity {
    RelativeLayout layout_joystick;
    ImageView image_joystick, image_border;
    TextView xAxis, yAxis, angle, distance, direction;

    JoyStick js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_stream);


        xAxis = (TextView)findViewById(R.id.xAxis);
        yAxis = (TextView)findViewById(R.id.yAxis);
        angle = (TextView)findViewById(R.id.angle);
        distance = (TextView)findViewById(R.id.distance);
        direction = (TextView)findViewById(R.id.direction);

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
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    xAxis.setText("X : " + String.valueOf(js.getX()));
                    yAxis.setText("Y : " + String.valueOf(js.getY()));
                    angle.setText("Angle : " + String.valueOf(js.getAngle()));
                    distance.setText("Distance : " + String.valueOf(js.getDistance()));

                    int direction = js.get8Direction();
                    if(direction == JoyStick.STICK_UP) {
                        VideoStreamActivity.this.direction.setText("Direction : Up");
                    } else if(direction == JoyStick.STICK_UPRIGHT) {
                        VideoStreamActivity.this.direction.setText("Direction : Up Right");
                    } else if(direction == JoyStick.STICK_RIGHT) {
                        VideoStreamActivity.this.direction.setText("Direction : Right");
                    } else if(direction == JoyStick.STICK_DOWNRIGHT) {
                        VideoStreamActivity.this.direction.setText("Direction : Down Right");
                    } else if(direction == JoyStick.STICK_DOWN) {
                        VideoStreamActivity.this.direction.setText("Direction : Down");
                    } else if(direction == JoyStick.STICK_DOWNLEFT) {
                        VideoStreamActivity.this.direction.setText("Direction : Down Left");
                    } else if(direction == JoyStick.STICK_LEFT) {
                        VideoStreamActivity.this.direction.setText("Direction : Left");
                    } else if(direction == JoyStick.STICK_UPLEFT) {
                        VideoStreamActivity.this.direction.setText("Direction : Up Left");
                    } else if(direction == JoyStick.STICK_NONE) {
                        VideoStreamActivity.this.direction.setText("Direction : Center");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
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
}
