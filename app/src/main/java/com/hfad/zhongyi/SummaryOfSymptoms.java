package com.hfad.zhongyi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;

public class SummaryOfSymptoms extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_of_symptoms);
        parentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout);
        addViews();
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
            Intent i = new Intent(this, TopLevelActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    public void addViews(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i = 0; i < Pages.pages.length; i++){
            Page page = Pages.pages[i];
            HashSet<Integer> symptomsChosen = page.getChosen();
            HashMap<Integer, String> id2symptom = page.getId2symptom();
            for(Integer key : symptomsChosen){
                final View rowView = inflater.inflate(R.layout.row, null);
                TextView textView = rowView.findViewById(R.id.symptom);
                textView.setText(id2symptom.get(key));
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            }
        }
    }
}
