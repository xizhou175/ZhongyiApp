package com.hfad.zhongyi;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SummaryOfSymptoms extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    ArrayList<DataModel> datamodels;
    private static CustomAdapter adapter;
    public static final String MESSAGE = "message";
    private String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_of_symptoms);
        parentLinearLayout = findViewById(R.id.linear_layout);
        Intent intent = getIntent();
        this.message = intent.getStringExtra(MESSAGE);
        addListView();
    }

    public void goToCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent it = new Intent(SummaryOfSymptoms.this, BodyPartsActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        System.out.println(this.message);
        it.putExtra(BodyPartsActivity.EXTRA_MESSAGE, this.message);
        startActivity(it);
        finish();
    }

    public void onDelete(View view){
        //TextView textView = view.getParent().findViewById(R.id.symptom);
        TextView textView = (TextView)((View)view.getParent()).findViewById(R.id.symptom);
        String symptom = textView.getText().toString();
        for(int i = 0; i < Pages.pages.length; i++){
            Page page = Pages.pages[i];
            HashMap<String, Integer> symptom2id = page.getSymptom2id();
            if(symptom2id.containsKey(symptom)){
                page.setChosen(symptom2id.get(symptom));
            }
        }
        System.out.println(symptom);
        parentLinearLayout.removeView((View)view.getParent());
        if(parentLinearLayout.getChildCount() == 0){
            Intent i = new Intent(this, BodyActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void addListView(){
        ArrayList<String> overallList = new ArrayList<String>();
        for(int i = 0; i < Pages.pages.length; i++){
            Page page = Pages.pages[i];
            HashSet<Integer> symptomsChosen = page.getChosen();
            HashMap<Integer, String> id2symptom = page.getId2symptom();
            String[] chosenSymptoms = new String[symptomsChosen.size()];
            int j = 0;
            for(Integer key : symptomsChosen){
                chosenSymptoms[j++] = id2symptom.get(key);
                //System.out.println(chosenSymptoms[j - 1]);
            }
            ArrayList<String> symptomsList = new ArrayList<String>(Arrays.asList(chosenSymptoms));
            overallList.addAll(symptomsList);
        }

        datamodels = new ArrayList<>();
        for(String s : overallList){
            datamodels.add(new DataModel(s));
        }

        for(DataModel d : datamodels){

            System.out.println(d.getSymptom());
        }

        adapter = new CustomAdapter(datamodels, getApplicationContext());
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);
    }

    public void addSymptoms(View view){
        Intent i = new Intent(this, BodyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
