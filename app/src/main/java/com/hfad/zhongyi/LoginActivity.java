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
    //private static final String serverURL = "http://10.0.2.2:8080/login";
    private static final String serverURL = "http://10.0.0.9:8080/login";

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
                EditText passwordField = findViewById(R.id.password);
                username = usrnameField.getText().toString();
                password = passwordField.getText().toString();
                if(!username.isEmpty() && !password.isEmpty()){
                    new Thread(new Runnable() {
                        public void run() {
                            JSONObject jsonObject = formatDataAsJson();
                            try {
                                URL url = new URL(serverURL);
                                HttpURLConnection client = (HttpURLConnection) url.openConnection();
                                client.setDoOutput(true);
                                client.setDoInput(true);
                                client.setRequestProperty("Content-Type", "text/plain");
                                client.setRequestProperty("Accept", "text/plain");
                                client.setRequestMethod("POST");
                                OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
                                wr.flush();
                                wr.write(jsonObject.toString());
                                wr.flush();
                                int responseCode = client.getResponseCode();
                                if(responseCode == 200){
                                    Intent intent = new Intent(LoginActivity.this, BodyActivity.class);
                                    startActivity(intent);
                                }
                                System.out.println("response code is:");
                                System.out.println(responseCode);
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

    private JSONObject formatDataAsJson(){
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("username", username);
            jsonObject.accumulate("password", password);
        }catch(Exception e){
            System.out.println("failed to create json");
        }
        String json = jsonObject.toString();
        System.out.print("This is what jason file looks like:");
        System.out.println(json);
        return jsonObject;
    }
}
