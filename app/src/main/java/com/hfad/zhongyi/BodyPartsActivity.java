package com.hfad.zhongyi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BodyPartsActivity extends Activity {

    private int pageNum = 0;
    private Page page = Pages.pages[pageNum];
    public static final String EXTRA_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boay_parts);
        BodySpecificFragments frag = (BodySpecificFragments)getFragmentManager().findFragmentById(R.id.symptoms_frag);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        pageNum = Integer.parseInt(message);
        page = Pages.pages[pageNum];
        frag.setPageNum(pageNum);
    }

    public void onClickButton(View view){
        int id = view.getId();
        Button button = view.findViewById(id);
        System.out.println(button.getText().toString());
        if(!page.getChosen().contains(id)) {
            button.setBackgroundResource(R.drawable.my_button_pressed);
            page.getChosen().add(id);
        }
        else {
            button.setBackgroundResource(R.drawable.my_button_released);
            page.getChosen().remove(id);
        }
    }

    public void onClickSummary(View view){
        Intent intent = new Intent(this, SummaryOfSymptoms.class);
        startActivity(intent);
    }
}
