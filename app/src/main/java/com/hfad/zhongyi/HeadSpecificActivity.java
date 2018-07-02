package com.hfad.zhongyi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HeadSpecificActivity extends Activity {

    private int pageNum = 0;
    private Page page = Pages.pages[pageNum];
    public static final String EXTRA_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        System.out.println(message);
        pageNum = Integer.parseInt(message);
        System.out.println(pageNum );
        if(pageNum == 0) {
            setContentView(R.layout.activity_head_specific);
        }
        else if(pageNum == 1){
            setContentView(R.layout.activity_chest_specific);
        }
        else if(pageNum == 2){
            setContentView(R.layout.activity_back_specific);
        }

        setPage(pageNum);
        for (Integer id : page.getId2symptom().keySet()) {
            //int id = page.getId2symptom().get(key);
            Button button = findViewById(id);
            //System.out.println(id);
            if(page.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            }
            else{
                button.setBackgroundResource(R.drawable.my_button_released);
            }
        }
    }


    public void setPage(int num){
        page = Pages.pages[pageNum];
    }
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState){
//        savedInstanceState.putBooleanArray("chosen", chosen);
//    }

//      @Override
//      public void onStop(){
//          super.onStop();
//      }
//
//      @Override
//      public void onStart(){
//          super.onStart();
//          for (Integer id : page.getId2symptom().keySet()) {
//              //int id = page.getId2symptom().get(key);
//              Button button = findViewById(id);
//              //System.out.println(id);
//              if(page.getChosen().contains(id)) {
//                  button.setBackgroundResource(R.drawable.my_button_pressed);
//              }
//              else{
//                  button.setBackgroundResource(R.drawable.my_button_released);
//              }
//          }
//      }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onResume(){
        super.onResume();
        for (Integer id : page.getId2symptom().keySet()) {
            //int id = page.getId2symptom().get(key);
            Button button = findViewById(id);
            //System.out.println(id);
            if(page.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            }
            else{
                button.setBackgroundResource(R.drawable.my_button_released);
            }
        }
    }

    public void onClickButton(View view){
        int id = view.getId();
        Button button = findViewById(id);
        //String symptom = button.getText().toString();
        //System.out.println(symptom);
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
