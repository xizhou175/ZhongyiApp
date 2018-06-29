package com.hfad.zhongyi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HeadSpecificActivity extends Activity {

    Page headPage = Pages.pages[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_specific);

        for (Integer id : headPage.getId2symptom().keySet()) {
            //int id = headPage.getId2symptom().get(key);
            Button button = findViewById(id);
            //System.out.println(id);
            if(headPage.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            }
            else{
                button.setBackgroundResource(R.drawable.my_button_released);
            }
        }
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
//          for (Integer id : headPage.getId2symptom().keySet()) {
//              //int id = headPage.getId2symptom().get(key);
//              Button button = findViewById(id);
//              //System.out.println(id);
//              if(headPage.getChosen().contains(id)) {
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
        for (Integer id : headPage.getId2symptom().keySet()) {
            //int id = headPage.getId2symptom().get(key);
            Button button = findViewById(id);
            //System.out.println(id);
            if(headPage.getChosen().contains(id)) {
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
        if(!headPage.getChosen().contains(id)) {
            button.setBackgroundResource(R.drawable.my_button_pressed);
            headPage.getChosen().add(id);
        }
        else {
            button.setBackgroundResource(R.drawable.my_button_released);
            headPage.getChosen().remove(id);
        }

    }

    public void onClickSummary(View view){
        Intent intent = new Intent(this, SummaryOfSymptoms.class);
        startActivity(intent);
    }

}
