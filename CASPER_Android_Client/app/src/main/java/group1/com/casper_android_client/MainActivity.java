package group1.com.casper_android_client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * Created by Andreas Fransson
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare Login Button
        Button LoginButton = (Button)findViewById(R.id.LoginButton);
        // Declare Text inputfields
        TextView user = (TextView)findViewById(R.id.User);
        TextView password = (TextView)findViewById(R.id.Password);
    }

        // change Activity on buttonclick
        public void onClick(View v){

            TextView user = (TextView)findViewById(R.id.User);
            TextView password = (TextView)findViewById(R.id.Password);
            ProgressBar loading = (ProgressBar)findViewById(R.id.progressBar);

            String userNameString = user.getText().toString();
            String passwordString = password.getText().toString();

            Intent intent = new Intent(this,LoggedInActivity.class);
            // Pass the extra data to next activity
            Singleton.getInstance().setUserName(userNameString);
            Singleton.getInstance().setPassWord(passwordString);

            // Show the loading wheel
            loading.setVisibility(v.VISIBLE);

            startActivity(intent);


        }




}
