package com.hfad.zhongyi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import static com.hfad.zhongyi.Patient.personalInfo;

public class BodyActivity extends AppCompatActivity {

    private boolean front = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personalInfo.setGender("male");
        setContentView(R.layout.activity_top_level);
        ImageView body = findViewById(R.id.bodyView);
        if(personalInfo.getGender().equals("male")){
            body.setImageResource(R.drawable.male_front);
        }
        else{
            body.setImageResource(R.drawable.female_front);
        }
    }

    public void onClickFlip(View view){
        ImageView imageView = findViewById(R.id.bodyView);
        if(front) {
            if(Patient.personalInfo.getGender().equals("male")) {
                imageView.setImageResource(R.drawable.male_back);
            }
            else imageView.setImageResource(R.drawable.female_back);
            findViewById(R.id.headText).setVisibility(View.INVISIBLE);
            findViewById(R.id.chestText).setVisibility(View.INVISIBLE);
            findViewById(R.id.backText).setVisibility(View.VISIBLE);
        } else {
            if(Patient.personalInfo.getGender().equals("male")) {
                imageView.setImageResource(R.drawable.male_front);
            }
            else imageView.setImageResource(R.drawable.female_front);
            findViewById(R.id.headText).setVisibility(View.VISIBLE);
            findViewById(R.id.chestText).setVisibility(View.VISIBLE);
            findViewById(R.id.backText).setVisibility(View.INVISIBLE);
        }
        front = !front;
    }

    public void onClick(View view){
        Intent intent = new Intent(this, BodyPartsActivity.class);
        if(view.getId() == R.id.headText) {
            intent.putExtra(BodyPartsActivity.EXTRA_MESSAGE, "0");
        }

        else if(view.getId() == R.id.chestText) {
            intent.putExtra(BodyPartsActivity.EXTRA_MESSAGE, "1");
        }

        else if(view.getId() == R.id.abdomenText){
            intent.putExtra(BodyPartsActivity.EXTRA_MESSAGE, "2");
        }
        else if(view.getId() == R.id.groinText){
            intent.putExtra(BodyPartsActivity.EXTRA_MESSAGE, "3");
        }
        else if(view.getId() == R.id.reproductionText){
            intent.putExtra(BodyPartsActivity.EXTRA_MESSAGE, "4");
        }
        else if(view.getId() == R.id.limbsText){
            intent.putExtra(BodyPartsActivity.EXTRA_MESSAGE, "5");
        }
        else if(view.getId() == R.id.bodyText){
            intent.putExtra(BodyPartsActivity.EXTRA_MESSAGE, "6");
        }
        startActivity(intent);
    }
}
