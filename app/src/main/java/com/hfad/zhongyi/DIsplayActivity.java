package com.hfad.zhongyi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DIsplayActivity extends AppCompatActivity {

    String serverURL = "18.188.169.26/diag/";
    String diseasesInString;
    String maixiang;
    String shexiang;
    static final String EXTRA_MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        final String fileName = intent.getStringExtra(EXTRA_MESSAGE);
        new Thread(new Runnable() {
            public void run() {
                try {
                    serverURL += fileName;
                    URL url = new URL(serverURL);
                    HttpURLConnection client = (HttpURLConnection) url.openConnection();
                    client.setDoOutput(true);
                    client.setRequestMethod("GET");

                    InputStreamReader reader = new InputStreamReader(client.getInputStream());
                    BufferedReader br = new BufferedReader(reader);
                    String infoStr = br.readLine();
                    JSONObject diagInfo = new JSONObject(infoStr);
                    JSONArray possibleDiseases = diagInfo.getJSONArray("症状");
                    String maixiang = diagInfo.getString("脉象");
                    String shexiang = diagInfo.getString("舌象");
                    for(int i = 0; i < possibleDiseases.length(); i++){
                        diseasesInString += possibleDiseases.getString(i);
                        diseasesInString += "\n";
                    }

                    TextView textView = findViewById(R.id.potientialDiseases);
                    textView.setText(diseasesInString);
                    textView = findViewById(R.id.maixiang);
                    textView.setText(maixiang);
                    textView.setText(shexiang);
                } catch (Exception e) {
                    //Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
