package com.hfad.zhongyi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
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
            findViewById(R.id.chestText).setVisibility(View.INVISIBLE);
            findViewById(R.id.backText).setVisibility(View.VISIBLE);
        } else {
            imageView.setImageResource(R.drawable.body);
            findViewById(R.id.headText).setVisibility(View.VISIBLE);
            findViewById(R.id.chestText).setVisibility(View.VISIBLE);
            findViewById(R.id.backText).setVisibility(View.INVISIBLE);
        }
        front = !front;
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, HeadSpecificActivity.class);
        if(view.getId() == R.id.headText) {
            intent.putExtra(HeadSpecificActivity.EXTRA_MESSAGE, "0");
        }
        else if(view.getId() == R.id.chestText){
            intent.putExtra(HeadSpecificActivity.EXTRA_MESSAGE, "1");
        }
        else if(view.getId() == R.id.backText){
            intent.putExtra(HeadSpecificActivity.EXTRA_MESSAGE, "2");
        }
        startActivity(intent);
    }
}
