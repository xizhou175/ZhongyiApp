package com.hfad.zhongyi;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BodyPartsActivity extends Activity {

    public static final String EXTRA_MESSAGE = "message";
    private String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boay_parts);
        // get the pageNum from intent
        Intent intent = getIntent();
        message = intent.getStringExtra(EXTRA_MESSAGE);
        int pg = Integer.parseInt(message);
        // init and add fragment
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTrans = fragManager.beginTransaction();
        BodySpecificFragments frag = BodySpecificFragments.newInstance(pg);
        fragTrans.add(R.id.symptoms_frag, frag);
        fragTrans.commit();
    }


    public void onClickSummary(View view){
        Intent intent = new Intent(this, SummaryOfSymptoms.class);
        intent.putExtra(SummaryOfSymptoms.MESSAGE, message);
        startActivity(intent);
    }
}
