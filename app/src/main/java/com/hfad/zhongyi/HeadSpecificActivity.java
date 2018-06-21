package com.hfad.zhongyi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HeadSpecificActivity extends Activity {

    Page head_page = Pages.pages[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_specific);

        int id = R.id.s1;
        for (int i = 0; i < head_page.getChosen().length; i++) {
            Button button = findViewById(id);
            if (head_page.getChosen()[id - R.id.s1]) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            }
            else button.setBackgroundResource(R.drawable.my_button_released);
            id += 1;
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState){
//        savedInstanceState.putBooleanArray("chosen", chosen);
//    }

//    @Override
//    public void onStop(){
//        super.onStop();
//        wasChosen = chosen;
//    }
//
//    @Override
//    public void onStart(){
//        super.onStart();
//        chosen = wasChosen;
//    }

//    @Override
//    public void onPause(){
//        super.onPause();
//        wasChosen = chosen;
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        chosen = wasChosen;
//    }

    public void onClickButton(View view){
        int id = view.getId();
        Button button = findViewById(id);

        if(!head_page.getChosen()[id - R.id.s1]) {
            button.setBackgroundResource(R.drawable.my_button_pressed);
        }
        else {
            button.setBackgroundResource(R.drawable.my_button_released);
        }
        head_page.setChosen(id - R.id.s1);
    }


}
