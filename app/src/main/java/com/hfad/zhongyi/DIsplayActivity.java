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

    String serverURL = "http://18.188.169.26/diag/";
    String diseasesInString;
    String maixiang;
    String shexiang;
    static final String EXTRA_MESSAGE = "FileId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        final String fileId= intent.getStringExtra(EXTRA_MESSAGE);
        // final String fileId = "a04a8a21-ae1a-40c5-ac28-e37429caff68_290918061135";
        // final String fileId = "a04a8a21-ae1a-40c5-ac28-e37429caff68_290918061104";

        new Thread(new Runnable() {
            public void run() {
                try {
                    serverURL += fileId;
                    URL url = new URL(serverURL);
                    HttpURLConnection client = (HttpURLConnection) url.openConnection();
                    client.setDoInput(true);
                    client.setRequestMethod("GET");

                    InputStreamReader reader = new InputStreamReader(client.getInputStream());
                    BufferedReader br = new BufferedReader(reader);
                    String infoStr = br.readLine();
                    JSONObject diagInfo = new JSONObject(infoStr);
                    JSONArray possibleDiseases = diagInfo.getJSONArray("症状");
                    maixiang = diagInfo.getString("脉象");
                    shexiang = diagInfo.getString("舌象");
                    for(int i = 0; i < possibleDiseases.length(); i++){
                        diseasesInString += possibleDiseases.getString(i);
                        diseasesInString += "\n";
                    }
                } catch (Exception e) {
                    //Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.potientialDiseases)).setText(diseasesInString == null ? "未找到相关信息" : diseasesInString);
                        ((TextView) findViewById(R.id.maixiang)).setText(maixiang == null ? "未找到相关信息" : maixiang);
                        ((TextView) findViewById(R.id.shexiang)).setText(shexiang == null ? "未找到相关信息" : shexiang);
                    }
                });
            }
        }).start();
    }
}
