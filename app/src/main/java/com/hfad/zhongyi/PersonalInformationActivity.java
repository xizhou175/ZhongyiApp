package com.hfad.zhongyi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.app.PendingIntent.getActivity;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener{

    private AlertDialog alertDialog;

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

        createAlertDiaglog();
    }

    private void createAlertDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请输入正确的年龄").setTitle("无效年龄").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        this.alertDialog = builder.create();
    }

    @Override
    public void onClick(View view){
        EditText text_age = findViewById(R.id.text_age);
        String age = text_age.getText().toString();
        try {
            Integer int_age = Integer.parseInt(age);
            if (int_age < 0 || int_age > 200) {
                this.alertDialog.show();
                return;
            }
        } catch (NumberFormatException e) {
            this.alertDialog.show();
            return;
        }
        Intent intent = new Intent(this, BodyActivity.class);
        startActivity(intent);
    }
}
