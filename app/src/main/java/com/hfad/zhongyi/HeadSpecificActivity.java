package com.hfad.zhongyi;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HeadSpecificActivity extends AppCompatActivity {

    boolean[] click = new boolean[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_specific);
        for(int i = 0; i < 3; i++){
            click[i] = false;
        }
    }

    public void onClickButton(View view){
        int id=view.getId();
        Button button = (Button) findViewById(id);

        if(click[id - R.id.s1] == false) {
            button.setBackgroundResource(R.drawable.my_button_pressed);
            click[id - R.id.s1] = true ;
        }
        else {
            button.setBackgroundResource(R.drawable.my_button_released);
            click[id - R.id.s1] = false;
        }
    }
}
