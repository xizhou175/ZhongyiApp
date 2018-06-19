package com.hfad.zhongyi;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TopLevelActivity extends Activity {

    private boolean front = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
    }

    public void onClickFlip(View view){
        ImageView imageView = (ImageView)findViewById(R.id.bodyview);
        if(front){
            imageView.setImageResource(R.drawable.cappuccino);
        }
        else imageView.setImageResource(R.drawable.body);
        front = !front;
    }
}
