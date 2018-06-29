package com.hfad.zhongyi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TopLevelActivity extends AppCompatActivity {

    private boolean front = true;
    Bitmap body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
    }

    public void onClickFlip(View view){
        ImageView imageView = findViewById(R.id.bodyView);
        if(front) {
            imageView.setImageResource(R.drawable.cappuccino);
            findViewById(R.id.headText).setVisibility(View.INVISIBLE);
        } else {
            imageView.setImageResource(R.drawable.body);
            findViewById(R.id.headText).setVisibility(View.VISIBLE);
        }
        front = !front;
    }

    public void onClickHead(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}
