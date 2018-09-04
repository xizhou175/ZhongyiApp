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

    private String username = "";
    private String password = "";
    private String passwordValidation = "";

    private boolean usernameGood = true;
    private boolean passwordGood = true;
    private boolean validationGood = true;

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
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.registerPassword);
        EditText passwordValidationField = findViewById(R.id.passwordValidation);
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();
        passwordValidation = passwordValidationField.getText().toString();

        if(!password.equals(passwordValidation)) validationGood = false;

        if(username.length() < 4) usernameGood = false;
        if(password.isEmpty() && password.length() < 6) passwordGood = false;

        createAlertDiaglog();

        if(!passwordGood || !usernameGood || !validationGood){
            this.alertDialog.show();
            return;
        }

        if(!username.isEmpty() && !password.isEmpty()){
            //create a new user
            personalInfo.setName(username);
            personalInfo.setPassword(password);
        }

        Intent intent = new Intent(RegisterActivity.this, PersonalInformationActivity.class);
        startActivity(intent);
    }

    private void createAlertDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(!usernameGood) {
            builder.setMessage("用户名应不小于4位字母").setTitle("无效用户名").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
        }
        else if(!passwordGood){
            builder.setMessage("密码必须大于或等于6位").setTitle("无效密码").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
        }
        else if(!validationGood){
            builder.setTitle("两次输入的密码不匹配").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
        }
        this.alertDialog = builder.create();
    }

}
