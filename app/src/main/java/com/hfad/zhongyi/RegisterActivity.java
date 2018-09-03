package com.hfad.zhongyi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

public class RegisterActivity extends Activity implements View.OnClickListener{

    private String email = "";
    private String password = "";

    private boolean emailGood = true;
    private boolean passwordGood = true;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.registerPassword);
        email = emailField.getText().toString();
        password = passwordField.getText().toString();

        emailGood = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
        if(password.isEmpty() && password.length() < 6) passwordGood = false;

        createAlertDiaglog();

        if(!passwordGood || !emailGood){
            this.alertDialog.show();
            return;
        }

        if(!email.isEmpty() && !password.isEmpty()){
            //create a new user
            personalInfo.setEmail(email);
            personalInfo.setPassword(password);
        }

        Intent intent = new Intent(RegisterActivity.this, PersonalInformationActivity.class);
        startActivity(intent);
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

    private void createAlertDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!emailGood) {
            builder.setMessage("请输入正确的邮箱").setTitle("无效邮箱").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        }
        else if(!passwordGood){
            builder.setMessage("密码必须大于或等于6位").setTitle("无效密码").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
        }
        this.alertDialog = builder.create();
    }

}
