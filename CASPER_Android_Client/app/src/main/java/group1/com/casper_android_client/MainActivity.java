package group1.com.casper_android_client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Data we want accessable in the next Activity
    public final static String EXTRA_MESSAGE1 = "group1.com.casper_android_client.MainActivity.username";
    public final static String EXTRA_MESSAGE2 = "group1.com.casper_android_client.MainActivity.passwordsend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare Login Button
        Button LoginButton = (Button)findViewById(R.id.LoginButton);
        // Declare Text inputfields
        TextView user = (TextView)findViewById(R.id.User2);
        TextView password = (TextView)findViewById(R.id.Password2);
    }

        // change Activity on buttonclick
        public void onClick(View v){
            Intent intent = new Intent(this,LoggedIn.class);

            TextView user = (TextView)findViewById(R.id.User2);
            TextView password = (TextView)findViewById(R.id.Password2);
            String username = user.getText().toString();
            String passwordsend = password.getText().toString();
            // Pass the extra data to next activity
            intent.putExtra(EXTRA_MESSAGE1,username);
            intent.putExtra(EXTRA_MESSAGE2,passwordsend);
            startActivity(intent);

        }

}
