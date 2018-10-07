package com.hfad.zhongyi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class DIsplayActivity extends AppCompatActivity {

    String serverURL = "http://18.188.169.26/diag/";

    // table keys
    String possibleSymtoms = "";
    String maixiang = "";
    String shexiang = "";

    String possibleDiseases = "";
    String fangji = "";
    String zhongyao = "";

    // json object for server response
    JSONObject diagInfo;

    String fileId;
    static final String EXTRA_MESSAGE = "FileId";

    //final String fileId = "a04a8a21-ae1a-40c5-ac28-e37429caff68_180918092717";
    //final String fileId = "a04a8a21-ae1a-40c5-ac28-e37429caff68_290918061135"; // Expected return {}
    //final String fileId = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        fileId = intent.getStringExtra(EXTRA_MESSAGE);

        Button button = findViewById(R.id.test);
        button.setVisibility(View.INVISIBLE);

        TextView textView = findViewById(R.id.loading);
        textView.setText("正在查询...");

        runQuery(button, textView);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(DIsplayActivity.this, BodyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void runQuery(final Button button, final TextView textView) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL(serverURL + fileId);
                    HttpURLConnection client = (HttpURLConnection) url.openConnection();
                    client.setDoInput(true);
                    client.setRequestMethod("GET");

                    InputStreamReader reader = new InputStreamReader(client.getInputStream());
                    BufferedReader br = new BufferedReader(reader);
                    String infoStr = br.readLine();

                    System.out.println(infoStr);

                    diagInfo = new JSONObject(infoStr);

                    possibleSymtoms = getJsonData("症状");
                    shexiang = getJsonData("舌象");
                    maixiang = getJsonData("脉象");

                    possibleDiseases = getJsonData("证型");
                    fangji = getJsonData("方剂");
                    zhongyao = getJsonData("中药");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("查询结束");
                        button.setVisibility(View.VISIBLE);
                        TableLayout table = findViewById(R.id.display_table);
                        String[] keys = new String[]{"可能的症状", "脉象", "舌像"};
                        String[] vals = new String[]{"未找到相关信息", "未找到相关信息", "未找到相关信息"};
                        if (possibleSymtoms != "") {
                            vals = new String[]{possibleSymtoms, maixiang, shexiang};
                        } else if (possibleDiseases != "") {
                            keys = new String[]{"证型", "方剂", "中药"};
                            vals = new String[]{possibleDiseases, fangji, zhongyao};
                        }
                        for (int i = 0; i < 3; i++) {
                            TableRow row = (TableRow) table.getChildAt(i);
                            ((TextView)row.getChildAt(0)).setText(keys[i]);
                            ((TextView)row.getChildAt(1)).setText(vals[i]);
                        }
                    }
                });
            }
        }).start();
    }

    private String getJsonData(String key) {
        try {
            JSONArray arrayData = diagInfo.getJSONArray(key);
            ArrayList<String> result = new ArrayList();
            for (int i = 0; i < arrayData.length(); i++) {
                result.addAll(flatten(arrayData.get(i)));
            }
            return String.join("\n", result);
        } catch (JSONException e) {
            return "";
        }
    }

    private ArrayList<String> flatten(Object obj) throws JSONException {
        if (obj instanceof JSONArray) {
            ArrayList<String> result = new ArrayList();
            JSONArray arr = (JSONArray) obj;
            for (int i = 0; i < arr.length(); i++) {
                result.addAll(flatten(arr.get(i)));
            }
            return result;
        } else if (obj instanceof String) {
            return new ArrayList(Arrays.asList((String) obj));
        } else {
            throw new JSONException("Unexpected object");
        }
    }
}
