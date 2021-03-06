package com.hfad.zhongyi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hfad.zhongyi.Patient.getPatient;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener{

    private AlertDialog alertDialog;
    private String serverURL = Config.getConfig().server_url;
    private PersonalInfo personalInfo = Patient.getPatient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        final Button maleButton = findViewById(R.id.male);
        final Button femaleButton = findViewById(R.id.female);
        final Button confirm  = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        if (((ColorDrawable) maleButton.getBackground()).getColor() == getResources().getColor(R.color.holo_blue_light)) {
            personalInfo.setGender("male");
        } else {
            personalInfo.setGender("female");
        }

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable buttonColor = (ColorDrawable) maleButton.getBackground();
                int color = buttonColor.getColor();
                personalInfo.setGender("male");
                if(color != getResources().getColor(R.color.holo_blue_light)) {
                    if (color == getResources().getColor(R.color.holo_blue_light)) {
                        maleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    } else {
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        maleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    }
                }
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personalInfo.setGender("female");
                ColorDrawable buttonColor = (ColorDrawable) femaleButton.getBackground();
                int color = buttonColor.getColor();
                if(color != getResources().getColor(R.color.holo_blue_light)) {
                    if (color == getResources().getColor(R.color.lightgrey)) {
                        maleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    } else {
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        maleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    }
                }
            }
        });

        createAlertDiaglog();
    }

    private void createAlertDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请输入正确的年龄").setTitle("无效年龄").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        this.alertDialog = builder.create();
    }

    @Override
    public void onClick(View view){
        EditText text_age = findViewById(R.id.text_age);
        String age = text_age.getText().toString();
        try {
            Integer int_age = Integer.parseInt(age);
            if (int_age < 0 || int_age > 200) {
                this.alertDialog.show();
                return;
            }
            personalInfo.setAge(int_age);
        } catch (NumberFormatException e) {
            this.alertDialog.show();
            return;
        }

        //send a request to the server
        new Thread(new Runnable() {
            public void run() {
                JSONObject jsonObject = formatDataAsJson();
                try {
                    URL url = new URL(serverURL + "/registration");
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
                    final int responseCode = client.getResponseCode();
                    System.out.println("response code is:" + responseCode);

                    if(responseCode == 200) {
                        InputStreamReader reader = new InputStreamReader(client.getInputStream());
                        BufferedReader br = new BufferedReader(reader);
                        String infoStr = br.readLine();
                        JSONObject userInfo = new JSONObject(infoStr);
                        personalInfo.setId(userInfo.getString("id"));
                        personalInfo.setName(userInfo.getString("name"));
                        personalInfo.setGender(userInfo.getString("gender"));
                        Log.d("PersonalInformation", "startBodyActivity");
                        Intent intent = new Intent(PersonalInformationActivity.this, BodyActivity.class);
                        startActivity(intent);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                registerFailedDialog(responseCode).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private AlertDialog registerFailedDialog(int code) {
        String message;
        switch (code) {
            case 409:
                message = "用户已存在，请直接登录";
                break;
            case 500:
                message = "服务器故障，请稍后再试";
                break;
            case 400:
                message = "输入的信息有误";
                break;
            default:
                message = "未知错误";
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("注册失败").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        return builder.create();
    }

    private JSONObject formatDataAsJson() {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("username", personalInfo.getName());
            jsonObject.accumulate("age", personalInfo.getAge());
            jsonObject.accumulate("gender", personalInfo.getGender());
            jsonObject.accumulate("password", personalInfo.getPassword());
        }catch(Exception e){
            System.out.println("failed to create json");
        }
        String json = jsonObject.toString();
        Log.d("formatDataAsJson", json);
        return jsonObject;
    }
}
