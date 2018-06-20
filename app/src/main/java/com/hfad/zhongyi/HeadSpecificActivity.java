package com.hfad.zhongyi;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HeadSpecificActivity extends AppCompatActivity {

    boolean[] chosen = new boolean[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_specific);
        for(int i = 0; i < 3; i++){
            chosen[i] = false;
        }
    }

    public void onClickButton(View view){
        int id = view.getId();
        Button button = findViewById(id);

        if(!chosen[id - R.id.s1]) {
            button.setBackgroundResource(R.drawable.my_button_pressed);
            button.setTextColor(Color.WHITE);
            chosen[id - R.id.s1] = true;
        } else {
            button.setBackgroundResource(R.drawable.my_button_released);
            button.setTextColor(Color.BLACK);
            chosen[id - R.id.s1] = false;
        }
    }
}
