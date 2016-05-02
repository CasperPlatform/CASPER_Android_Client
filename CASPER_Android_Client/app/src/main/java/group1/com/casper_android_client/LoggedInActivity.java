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

import java.io.IOException;
import java.net.InetAddress;

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


        System.out.println("Is user Logged in via REST?: " + Singleton.getInstance().getLoggedInUser());
       // System.out.println(Singleton.getInstance().getLoggedInUser().toString());






                            Singleton.getInstance().setTCPsocket(new TCPsocket(
                                    "192.168.10.1",
                                    Integer.parseInt("9999")));
                            Singleton.getInstance().getTCPsocket().execute();









        // Declare Video controller Button
        controls = (Button) findViewById(R.id.controlButton);

        // Socket connection activity
        sockets = (Button)findViewById(R.id.sockets);

        responce = (TextView)findViewById(R.id.responce2);


        if(Singleton.getInstance().getSocket().isBound()){
            responce.setTextColor(Color.rgb(0,255,0));
            responce.setText(" Socket Connection at: \n " + Singleton.getInstance().getSocket().getRemoteSocketAddress());
        }else{
            responce.setTextColor(Color.rgb(255,0,0));
            responce.setText("No Socket Connection Found!");
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
        System.out.println("--------------------->Bound? " + Singleton.getInstance().getSocket().isBound());
        System.out.println("---------------------->closed? " + Singleton.getInstance().getSocket().isClosed());
        if(Singleton.getInstance().getSocket().isBound()) {
            // Close Socket
            try {
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

    public void socket(View v){
        Intent intent = new Intent(this,Manual_socket_connection.class);
        startActivity(intent);
    }


}
