package com.hfad.zhongyi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashSet;
import java.util.Set;

public class HeadSpecificActivity extends AppCompatActivity {

    Set<Integer> chosen = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_specific);
    }

    public void onClickButton(View view){
        int id = view.getId();
        Button button = findViewById(id);

        if(!chosen.contains(id)) {
            button.setBackgroundResource(R.drawable.my_button_pressed);
            button.setTextColor(Color.WHITE);
            chosen.add(id);
        } else {
            button.setBackgroundResource(R.drawable.my_button_released);
            button.setTextColor(Color.BLACK);
            chosen.remove(id);
        }
    }
}
