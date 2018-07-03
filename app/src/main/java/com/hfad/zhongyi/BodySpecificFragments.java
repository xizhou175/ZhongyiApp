package com.hfad.zhongyi;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;

public class BodySpecificFragments extends Fragment{

    private int pageNum = 0;
    private Page page = Pages.pages[pageNum];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_specific_symptoms, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        page = Pages.pages[pageNum];
        this.setTitle(view);
        this.setText(view);
        System.out.println(pageNum);
        for (Integer id : page.getId2symptom().keySet()) {
            Button button = view.findViewById(id);
            if(page.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            }
            else{
                button.setBackgroundResource(R.drawable.my_button_released);
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        View view = getView();
        for (Integer id : page.getId2symptom().keySet()) {
            //int id = page.getId2symptom().get(key);
            Button button = view.findViewById(id);
            //System.out.println(id);
            if(page.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            }
            else{
                button.setBackgroundResource(R.drawable.my_button_released);
            }
        }
    }


    public void setPageNum(int num){
        this.pageNum = num;
    }

    //set page title
    public void setTitle(View view){
        if(view != null){
            TextView title = view.findViewById(R.id.title);
            if(pageNum == 0){
                title.setText("头部症状");
            }
            else if(pageNum == 1){
                title.setText("胸部症状");
            }
            else if(pageNum == 2){
                title.setText("背部症状");
            }
        }
    }

    //set text on buttons
    public void setText(View view){
        HashSet<String> symptoms = page.getSymptoms();
        int id = R.id.s1;
        for(String key : symptoms){
            Button button = view.findViewById(id++);
            button.setText(key);
        }
    }

}