package group1.com.casper_android_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class LoggedInActivity extends AppCompatActivity {

    private Button back;
    private Button settings;
    private Button controls;
    private Button sockets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);


        // Declare Back button
        back = (Button) findViewById(R.id.backButton);

        // Declare Settings Button
        settings = (Button) findViewById(R.id.Settingsbutton);

        // Declare Video controller Button
        controls = (Button) findViewById(R.id.controlButton);

        // Socket connection activity
        sockets = (Button)findViewById(R.id.sockets);

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
        Intent intent = new Intent(this,Socket_Connection.class);
        startActivity(intent);
    }

}
