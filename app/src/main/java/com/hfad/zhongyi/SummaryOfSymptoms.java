package com.hfad.zhongyi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryOfSymptoms extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_of_symptoms);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
    }

    public void onDelete(View view){
        parentLinearLayout.removeView((View)view.getParent());
    }

    /*public void addViews(){

    }*/
}
