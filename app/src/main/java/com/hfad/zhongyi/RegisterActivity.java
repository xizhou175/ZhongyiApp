package com.hfad.zhongyi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hfad.zhongyi.Patient.personalInfo;

public class RegisterActivity extends Activity {

    private String email = "";
    private String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailField = findViewById(R.id.email);
                EditText passwordField = findViewById(R.id.registerPassword);
                email = emailField.getText().toString();
                password = passwordField.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    //create a new user
                    personalInfo.setEmail(email);
                    personalInfo.setPassword(password);
                }

                Intent intent = new Intent(RegisterActivity.this, PersonalInformationActivity.class);
                startActivity(intent);
            }
        });
    }

    private JSONObject formatDataAsJson(){
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("email", email);
            jsonObject.accumulate("password", password);
        }catch(Exception e){
            System.out.println("failed to create json");
        }
        String json = jsonObject.toString();
        System.out.println(json);
        return jsonObject;
    }
}
