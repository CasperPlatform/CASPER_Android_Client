package group1.com.casper_android_client;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;


/**
 * Created by Andreas Fransson
 */
public class MainActivity extends AppCompatActivity {

    // Declare server URL
    private final String PATH = "http://192.168.0.31:3000";

    // Progress bar
    private ProgressBar loading;

    // Login Button
    private Button LoginButton;

    // Input fields for login
    private TextView user;
    private TextView password;

    // Error message TextView
    private TextView errormsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // Declare Login Button
        LoginButton = (Button)findViewById(R.id.LoginButton);

        // Declare Text inputfields
        user = (TextView)findViewById(R.id.User);
        password = (TextView)findViewById(R.id.Password);
        errormsg = (TextView)findViewById(R.id.ErrorMsg);

        // Declare Loading wheel
        loading = (ProgressBar) findViewById(R.id.progressBar);

    }

        /**
         * On Login start a new thread to authenticate credentials
         * & break into UI threads to access View items
         * @param v
         */
        public void onLogin(final View v)
        {
            // admin
            if(user.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {

                // Load next Activity
                finish();
                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                startActivity(intent);


            }// Backdoor for test purposes
            else if(user.getText().toString().equals("demo") && password.getText().toString().equals("demo")){

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // Set progress wheel visable to indicate process
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              // Show the loading wheel
                                              loading.setVisibility(v.VISIBLE);
                                          }
                                      }

                        );


                        try {
                            if (InetAddress.getByName("192.168.0.15").isReachable(2000)) {




                            // Setting up Socket connection
                            SocketConnection SocketConnection = new SocketConnection(
                                    "192.168.0.15",
                                    Integer.parseInt("9999"),false);
                            SocketConnection.execute();



                            // Set progress wheel visable to indicate process
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  // Show the loading wheel
                                                  loading.setVisibility(v.VISIBLE);
                                              }
                                          }

                            );

                            // Ugly but it takes a few milliseconds to do
                            while(!Singleton.getInstance().getSocket().isBound()){

                            }

                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  // Stop the loading wheel and show Error Msg.
                                                  loading.setVisibility(v.INVISIBLE);
                                              }
                                          }
                            );



                            // Load next Activity
                            finish();
                            Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                            startActivity(intent);
                            }else{

                                runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      errormsg.setText("sConnection Error!");
                                                      // Stop the loading wheel and show Error Msg.
                                                      loading.setVisibility(v.INVISIBLE);
                                                      errormsg.setVisibility(v.VISIBLE);
                                                  }
                                              }
                                );
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            errormsg.setText("Connection Error!");
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  // Stop the loading wheel and show Error Msg.
                                                  loading.setVisibility(v.INVISIBLE);
                                                  errormsg.setVisibility(v.VISIBLE);
                                              }
                                          }
                            );
                        }
                    }
                });


                // Start the thread
                thread.start();

            }
            else if(user.getText().toString().equals("demo2") && password.getText().toString().equals("demo2")) {
                // Setting up Socket connection

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // Set progress wheel visable to indicate process
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              // Show the loading wheel
                                              loading.setVisibility(v.VISIBLE);
                                          }
                                      }

                        );


                        try {
                            if (InetAddress.getByName("192.168.10.1").isReachable(2000)) {


                                Singleton.getInstance().setSocketConnection(new SocketConnection(
                                        "192.168.10.1",
                                        Integer.parseInt("9999"), false));

                                Singleton.getInstance().getSocketConnection().execute();


                                // Ugly but it takes a few milliseconds to do
                                while (!Singleton.getInstance().getSocket().isBound()) {

                                }

                                runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      // Stop the loading wheel and show Error Msg.
                                                      loading.setVisibility(v.INVISIBLE);
                                                  }
                                              }
                                );


                                // Load next Activity
                                finish();

                                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                                startActivity(intent);
                            }else{

                                runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      errormsg.setText("Connection Error!");
                                                      // Stop the loading wheel and show Error Msg.
                                                      loading.setVisibility(v.INVISIBLE);
                                                      errormsg.setVisibility(v.VISIBLE);
                                                  }
                                              }
                                );
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            errormsg.setText("Connection Error!");
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  // Stop the loading wheel and show Error Msg.
                                                  loading.setVisibility(v.INVISIBLE);
                                                  errormsg.setVisibility(v.VISIBLE);
                                              }
                                          }
                            );
                        }
                    }
                });


                    // Start the thread
                    thread.start();
            }
            else if(user.getText().toString().equals("demo3") && password.getText().toString().equals("demo3")) {
                // Setting up Socket connection

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // Set progress wheel visable to indicate process
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              // Show the loading wheel
                                              loading.setVisibility(v.VISIBLE);
                                          }
                                      }

                        );


                        try {
                            if (InetAddress.getByName("192.168.10.1").isReachable(2000)) {



                Singleton.getInstance().setSocketConnection(new SocketConnection(
                        "192.168.10.1",
                        Integer.parseInt("9999"),true));
                Singleton.getInstance().getSocketConnection().execute();




                // Set progress wheel visable to indicate process
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      // Show the loading wheel
                                      loading.setVisibility(v.VISIBLE);
                                  }
                              }

                );

                // Ugly but it takes a few milliseconds to do
                while (!Singleton.getInstance().getSocket().isBound()) {

                }

                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      // Stop the loading wheel and show Error Msg.
                                      loading.setVisibility(v.INVISIBLE);
                                  }
                              }
                );


                // Load next Activity
                finish();

                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                startActivity(intent);
            }else{

                 runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        errormsg.setText("Connection Error!");
                        // Stop the loading wheel and show Error Msg.
                        loading.setVisibility(v.INVISIBLE);
                        errormsg.setVisibility(v.VISIBLE);
                    }
                 });
            }
            } catch (IOException e) {
                            e.printStackTrace();
                            errormsg.setText("Connection Error!");
                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  // Stop the loading wheel and show Error Msg.
                                                  loading.setVisibility(v.INVISIBLE);
                                                  errormsg.setVisibility(v.VISIBLE);
                                              }
                                          }
                            );
                        }
                    }
                });


                // Start the thread
                thread.start();
            }
            // Actual Access to Rest server and authentiation
            else
            {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    // Set progress wheel visable to indicate process
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          // Show the loading wheel
                                          loading.setVisibility(v.VISIBLE);
                                      }
                                  }

                    );

                    // Get inputfield info
                    String userNameString = user.getText().toString();
                    String passwordString = password.getText().toString();

                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{ \"username\" : \"" + user.getText() + "\", \"password\" : \"" + password.getText() + "\"}");
                    Request request = new Request.Builder()
                            .url(PATH + "/login")
                            .post(body)
                            .addHeader("content-type", "application/json")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "1afc98db-9786-e9a1-92dd-c93c082f958f")
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        String jsonString = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonString);
                        // check if answer contains token
                        if (jsonObject.has("token")) {
                            //authResult.setText("Successfully handshaked with\nDrone!");
                            System.out.println(jsonObject.get("token"));
                            // Set User
                            Singleton.getInstance().setLoggedInUser(new User(jsonObject));
                            // Make sure the error msg isnt displayed after haveing the correct login information.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    errormsg.setVisibility(v.INVISIBLE);
                                }
                            });

                            // Make sure the Activity is finished and deleted.
                            finish();
                            Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                            startActivity(intent);
                        }
                        // if it doesn't, we failed to authenticate
                        else {

                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  // Stop the loading wheel and show Error Msg.
                                                  loading.setVisibility(v.INVISIBLE);
                                                  errormsg.setVisibility(v.VISIBLE);
                                              }
                                          }
                            );
                            // authResult.setText("Handshake failed, bad credentials");
                            System.out.println("Wrong username or password");
                        }
                    } catch (IOException e) {
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              // Stop the loading wheel and show Error Msg.
                                              loading.setVisibility(v.INVISIBLE);
                                              errormsg.setText("Connection Error!");
                                              errormsg.setVisibility(v.VISIBLE);
                                          }
                                      }
                        );
                        // authResult.setText("Handshake failed, bad credentials");
                        System.out.println("Wrong username or password");

                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Start the thread
            thread.start();

        }


        }



}
