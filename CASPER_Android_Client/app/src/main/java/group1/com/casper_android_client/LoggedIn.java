package group1.com.casper_android_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoggedIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // get the intent sen
        Intent intent = getIntent();

        TextView user = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.thepassword);
        Button back = (Button) findViewById(R.id.backButton);
        if(!intent.getStringExtra(MainActivity.EXTRA_MESSAGE1).isEmpty()) {
            user.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE1));
            password.setText(intent.getStringExtra(MainActivity.EXTRA_MESSAGE2));
        }else{
            user.setText("No User");
            password.setText("No Passwrd");
        }

    }

    public void backButton(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }




}
