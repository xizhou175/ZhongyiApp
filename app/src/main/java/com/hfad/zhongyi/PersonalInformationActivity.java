package com.hfad.zhongyi;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        final Button maleButton = findViewById(R.id.male);
        final Button femaleButton = findViewById(R.id.female);
        final Button confirm  = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable buttonColor = (ColorDrawable) maleButton.getBackground();
                int color = buttonColor.getColor();
                if(color != getResources().getColor(R.color.holo_blue_light)) {
                    Patient.personalInfo.setGender("male");
                    if (color == getResources().getColor(R.color.holo_blue_light)) {
                        maleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    } else {
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        maleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    }
                }
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable buttonColor = (ColorDrawable) femaleButton.getBackground();
                int color = buttonColor.getColor();
                if(color != getResources().getColor(R.color.holo_blue_light)) {
                    Patient.personalInfo.setGender("female");
                    if (color == getResources().getColor(R.color.lightgrey)) {
                        maleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    } else {
                        femaleButton.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                        maleButton.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(this, TopLevelActivity.class);
        startActivity(intent);
    }
}
