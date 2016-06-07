package group1.com.casper_android_client;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoggedInActivity extends AppCompatActivity {

    // Controll buttons;
    private Button controls;
    private Button sockets;

    // Error msg text
    private TextView responce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);


//        System.out.println("Is user Logged in via REST?: " + Singleton.getInstance().getLoggedInUser());
//       // System.out.println(Singleton.getInstance().getLoggedInUser().toString());
//
//
//        try {
//            Singleton.getInstance().setDriveSocket(new DriveSocket(
//                    "192.168.10.1",
//                    Integer.parseInt("9999")));
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        Singleton.getInstance().getDriveSocket().execute();





        // Declare Video controller Button
        controls = (Button) findViewById(R.id.controlButton);

        // Socket connection activity
        sockets = (Button)findViewById(R.id.sockets);

        responce = (TextView)findViewById(R.id.responce2);


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
                System.out.println("Going to settings");
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);

                return true;
            case R.id.logout:
                System.out.println("logging out");
                finish();
                logout();
                return true;
            default:
                return false;
        }
    }


    public void options(View v){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    public void backButton(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    public void ctrl(View v){
        Intent intent = new Intent(this,VideoStreamActivity.class);
        startActivity(intent);
    }

    // Quit connection
    public void logout(){
        // Debug
        if(Singleton.getInstance().getDriveSocket().DriveSocket.isConnected()) {
            // Close Socket
                finish();
                Singleton.getInstance().getDriveSocket().DriveSocket.disconnect();
                Singleton.getInstance().getDriveSocket().DriveSocket.close();

            // Transfer to main
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{

        }

    }




}
