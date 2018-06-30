package com.hfad.zhongyi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryOfSymptoms extends AppCompatActivity {

    private LinearLayout parentTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_of_symptoms);
        parentTableLayout = findViewById(R.id.table_layout);
    }

    public void onDelete(View view){
        parentTableLayout.removeView((View)view.getParent());
    }

    /*public void addViews(){

    }*/

    public void goToCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}
package com.hfad.zhongyi;

        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.Layout;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.LinearLayout;
        import android.widget.TextView;

public class SummaryOfSymptoms extends AppCompatActivity {

    private LinearLayout parentTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_of_symptoms);
        parentTableLayout = findViewById(R.id.table_layout);
    }

    public void onDelete(View view){
        parentTableLayout.removeView((View)view.getParent());
    }

    /*public void addViews(){

    }*/

    public void goToCamera(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }
}
