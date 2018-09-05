package com.hfad.zhongyi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private String username = "";
    private String password = "";
    //private static final String serverURL = "http://10.0.2.2:8080/login"; // use for emulator
    private static final String serverURL = "http://10.0.0.9:8080/login";   // use for real phones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView t = findViewById(R.id.toRegister);
        t.setOnClickListener(this);

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText usrnameField = findViewById(R.id.username);
                final EditText passwordField = findViewById(R.id.password);
                username = usrnameField.getText().toString();
                password = passwordField.getText().toString();
                if(!username.isEmpty() && !password.isEmpty()){
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                URL url = new URL(serverURL);
                                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                                client.setDoOutput(true);
                                client.setRequestMethod("POST");
                                OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
                                wr.flush();
                                wr.write(String.format("username=%s&password=%s", username, password));
                                wr.flush();
                                int responseCode = client.getResponseCode();
                                System.out.println("response code is:");
                                System.out.println(responseCode);

                                String responseBody = client.getResponseMessage();
                                System.out.println("responseBody: " + responseBody);

                                if(responseCode == 200){
                                    Intent intent = new Intent(LoginActivity.this, BodyActivity.class);
                                    startActivity(intent);
                                } else {
                                    // TODO: AlertDialog
                                }

                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
