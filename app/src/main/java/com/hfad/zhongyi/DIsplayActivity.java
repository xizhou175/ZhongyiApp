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

    // json object for server response
    JSONObject diagInfo;

    static String EXTRA_MASSAGE[] = new String[3];
    String PossibleDiseases= "";
    String Fangji = "";
    String ChineseMedicine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();

        /*PossibleDiseases = intent.getStringArrayExtra(EXTRA_MASSAGE)[0];
        Fangji = intent.getStringArrayExtra(EXTRA_MASSAGE)[1];
        ChineseMedicine = intent.getStringArrayExtra(EXTRA_MASSAGE)[2];*/
        PossibleDiseases = EXTRA_MASSAGE[0];
        Fangji = EXTRA_MASSAGE[1];
        ChineseMedicine = EXTRA_MASSAGE[2];

        SetTable();

        /*button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(DIsplayActivity.this, BodyActivity.class);
                startActivity(intent);
            }
        });*/
    }

    private void SetTable(){

        TableLayout table = findViewById(R.id.display_table);
        String[] keys = new String[]{"可能的症状", "脉象", "舌像"};
        String[] vals = new String[]{"未找到相关信息", "未找到相关信息", "未找到相关信息"};

        keys = new String[]{"证型", "方剂", "中药"};
        vals = new String[]{PossibleDiseases, Fangji, ChineseMedicine};

        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            ((TextView)row.getChildAt(0)).setText(keys[i]);
            ((TextView)row.getChildAt(1)).setText(vals[i]);
        }
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

    public void goToCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void getResult(View view){
        Intent intent = new Intent(this, DIsplayActivity.class);
        String EXTRA[] = new String[3];
        EXTRA[0] = PossibleDiseases;
        EXTRA[1] = Fangji;
        EXTRA[2] = ChineseMedicine;
        //intent.putExtra(DIsplayActivity.EXTRA_MASSAGE, EXTRA);
        startActivity(intent);
    }
}
