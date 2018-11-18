package com.hfad.zhongyi;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.hfad.zhongyi.Patient.personalInfo;

public class ToSelectMoreSymptomsActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener {

    ArrayList<DataModel> dataModels;
    public static final String EXTRA_MESSAGE = "";
    private static CustomAdapterForDropDown adapter;
    private HttpURLConnection connection;
    private String server_url = "http://10.0.2.2:5000";
    private String CRLF = "\r\n";
    private String CharSet = "UTF-8";
    private String TAG = "uploadSelectedSymptom";

    HttpURLConnection client;
    String readline = "";
    ArrayList<String> tempSymptoms;

    public static ArrayList<String> possibleSymptoms = new ArrayList<String>();
    public static ArrayList<String> possibleDiseases = new ArrayList<String>();
    public static ArrayList<String> fangJi = new ArrayList<String>();
    public static ArrayList<String> chineseMedicine = new ArrayList<String>();

    // json object for server response
    JSONObject diagInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_select_more_symptoms);
        Intent intent = getIntent();
        String symptomSelected = intent.getStringExtra(EXTRA_MESSAGE);
        System.out.println("EXTRA MESSAGE:" + EXTRA_MESSAGE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadSelectedSymptom();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("possibleDiseases:" + possibleDiseases.toString());
                        if(possibleDiseases.size() != 0){
                            System.out.println("enter here");
                            Button b = findViewById(R.id.result_button);
                            ListView lv = findViewById(R.id.lv);
                            lv.setAdapter(null);
                            b.setVisibility(View.VISIBLE);
                        }
                        else {
                            addListView();
                        }
                    }
                });
            }
        }).start();
    }

    public void clearData(){
        Pages.allChosen.clear();
        personalInfo.clearSymptoms();
        possibleSymptoms.clear();
        possibleDiseases.clear();
        tempSymptoms.clear();
        fangJi.clear();
        chineseMedicine.clear();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        clearData();
        Intent it = new Intent(ToSelectMoreSymptomsActivity.this, BodyActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
        finish();
    }

    public void addListView(){
        dataModels = new ArrayList<>();
        System.out.println("possibleSymptoms in addListView:" + possibleSymptoms.toString());// have to wait for possibleSymptoms to be filled
        for(int i = 0; i < possibleSymptoms.size(); i++){
            Boolean selected = false;
            dataModels.add(new DataModel(possibleSymptoms.get(i), selected));
        }

        adapter = new CustomAdapterForDropDown(dataModels, getApplicationContext());
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
    }

    private boolean uploadSelectedSymptom() {
        try {
            URL url = new URL(server_url + "/match-symptoms");
            client = (HttpURLConnection) url.openConnection();
            client.setDoOutput(true);
            client.setRequestProperty("Content-Type", "application/json");
            OutputStream output = client.getOutputStream();
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(output, CharSet));
            writeSelectedSymptoms(writer);
            writer.flush();

            int responseCode = client.getResponseCode();
            Log.d(TAG, String.format("upload patient data response Code: %d", responseCode));

            InputStreamReader reader = new InputStreamReader(client.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            String infoStr = br.readLine();
            readline = infoStr.equals("{}") ? "0" : infoStr;

            System.out.println("infoStr:" + infoStr);

            diagInfo = new JSONObject(infoStr);

            tempSymptoms = getJsonData("症状");

            System.out.println("tempSymptoms:" + tempSymptoms.size());

            possibleSymptoms = tempSymptoms;

            possibleDiseases = getJsonData("证型");
            System.out.println(possibleDiseases);
            System.out.println(possibleDiseases.toString());
            fangJi = getJsonData("方剂");
            chineseMedicine = getJsonData("中药");


            return true;

        } catch (Exception e) {
            //Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            client.disconnect();
        }
    }

    //list view onItemCLickListener
    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

        DataModel item = dataModels.get(position);

        TextView symtxt = v.findViewById(R.id.symptom);
        String symptom = symtxt.getText().toString();
        Log.d("onItemClick", "pos: "+position + " " + "symptom: " + symptom);
        Integer symId = Pages.symptomToid.get(symptom);
        if (item.getSelected() == true) {
            item.setSelected(false);
            Pages.allChosen.remove(symId);
            personalInfo.removeSymptom(Pages.idTosymptom.get(symId));
            DataModel.numSelected -= 1;
        } else {
            item.setSelected(true);
            Pages.allChosen.add(symId);
            personalInfo.addSymptom(Pages.idTosymptom.get(symId));
            System.out.println("personalInfo:" + personalInfo.getSymptoms().toString());
            DataModel.numSelected += 1;
        }

        System.out.println("number of selected symptoms:" + DataModel.numSelected);

        adapter.notifyDataSetChanged();

        if(DataModel.numSelected != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadSelectedSymptom();
                    System.out.println("possibleDiaeases size:" + possibleDiseases.size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(possibleDiseases.size() != 0){
                                System.out.println("enter here");
                                Button b = findViewById(R.id.result_button);
                                ListView lv = findViewById(R.id.lv);
                                lv.setAdapter(null);
                                b.setVisibility(View.VISIBLE);
                            }
                            else {
                                ListView lv = findViewById(R.id.lv);
                                lv.setAdapter(null);
                                addListView();
                            }
                        }
                    });
                }
            }).start();
        }
        else{
            Button b = findViewById(R.id.result_button);
            b.setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<String> getJsonData(String key) {
        ArrayList<String> result = new ArrayList();
        try {
            JSONArray arrayData = diagInfo.getJSONArray(key);
            for (int i = 0; i < arrayData.length(); i++) {
                result.addAll(flatten(arrayData.get(i)));
            }
            return result;
            //return String.join("\n", result);
        } catch (JSONException e) {
            return result;
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

    private void writeSelectedSymptoms(JsonWriter writer) throws IOException {
        writer.beginArray();
        for(String symptom : personalInfo.getSymptoms()){
            writer.value(symptom);
        }
        writer.endArray();
    }

    public void startOver(View view){
        Intent i = new Intent(this, BodyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        clearData();
        startActivity(i);
    }

    public void gotoSummary(View view){
        Intent intent = new Intent(this, SummaryOfSymptoms.class);
        String EXTRA[] = new String[3];
        EXTRA[0] = possibleDiseases.toString();
        EXTRA[1] = fangJi.toString();
        EXTRA[2] = chineseMedicine.toString();

        possibleSymptoms.clear();
        possibleDiseases.clear();
        tempSymptoms.clear();
        fangJi.clear();
        chineseMedicine.clear();

        intent.putExtra(DIsplayActivity.EXTRA_MASSAGE, EXTRA);
        startActivity(intent);
    }



}
