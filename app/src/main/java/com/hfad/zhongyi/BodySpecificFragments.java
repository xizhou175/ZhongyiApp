package com.hfad.zhongyi;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class BodySpecificFragments extends Fragment implements View.OnClickListener {

    private int pageNum = -1;
    private Page page = null;

    public static BodySpecificFragments newInstance(int pg) {
        Bundle args = new Bundle();
        args.putInt("pageNum", pg);
        BodySpecificFragments frag = new BodySpecificFragments();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        page = Pages.pages[pageNum];
        //this.setText(view);
        /*for (Integer id : page.getId2symptom().keySet()) {
            Button button = view.findViewById(id);
            if(page.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            }
            else{
                button.setBackgroundResource(R.drawable.my_button_released);
            }
        }*/
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        for (Integer id : page.getId2symptom().keySet()) {
            //int id = page.getId2symptom().get(key);
            Button button = view.findViewById(id);
            //System.out.println(id);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_specific_symptoms, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageNum = bundle.getInt("pageNum");
            page = Pages.pages[pageNum];
        }
        setPageTitle(view);
        LinearLayout buttonsLayout = view.findViewById(R.id.buttons_layout);
        if (pageNum != -1 && page != null) { // only inflate when we receive the pageNum arg and init page obj
            inflateButtons(buttonsLayout);
        }
        return view;
    }

    private void inflateButtons(LinearLayout layout) {
        HashMap<Integer, String> map = page.getId2symptom();
        for (Integer id : map.keySet()) {
            Button button = new Button(layout.getContext());
            button.setText(map.get(id));
            button.setId(id);

            if (page.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            } else {
                button.setBackgroundResource(R.drawable.my_button_released);
            }

            button.setOnClickListener(this);
            layout.addView(button);
        }
    }

    public void setPageTitle(View view){
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


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Button button = view.findViewById(id);
        if(!page.getChosen().contains(id)) {
            button.setBackgroundResource(R.drawable.my_button_pressed);
            page.getChosen().add(id);
        }
        else {
            button.setBackgroundResource(R.drawable.my_button_released);
            page.getChosen().remove(id);
        }
    }
}