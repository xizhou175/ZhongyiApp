package com.hfad.zhongyi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hfad.zhongyi.Patient.personalInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "LoginActivity";

    private String username = "";
    private String password = "";
    //private static final String serverURL = "http://10.0.2.2:8080/login"; // use for emulator
    //private static final String serverURL = "http://10.0.0.9:8080/login";   // use for real phones
    private static final String serverURL = "http://18.188.169.26/login"; // aws server

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
                                if(responseCode == 200){
                                    InputStreamReader reader = new InputStreamReader(client.getInputStream());
                                    BufferedReader br = new BufferedReader(reader);
                                    String infoStr = br.readLine();
                                    JSONObject userInfo = new JSONObject(infoStr);
                                    personalInfo.setId(userInfo.getString("id"));
                                    personalInfo.setName(userInfo.getString("name"));
                                    personalInfo.setGender(userInfo.getString("gender"));
                                    Intent intent = new Intent(LoginActivity.this, BodyActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.d(TAG, "Response Code: " + responseCode);
                                    // TODO: AlertDialog
                                }
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
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
