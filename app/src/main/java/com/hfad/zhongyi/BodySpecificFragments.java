package com.hfad.zhongyi;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
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
import java.util.TreeSet;

public class BodySpecificFragments extends Fragment {

    private int pageNum = -1;
    private Page page = null;

    public static BodySpecificFragments newInstance(int pg) {
        Bundle args = new Bundle();
        args.putInt("pageNum", pg);
        BodySpecificFragments frag = new BodySpecificFragments();
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_specific_symptoms, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageNum = bundle.getInt("pageNum");
            page = Pages.pages[pageNum];
        }
        setPageTitle(view);
        RelativeLayout buttonsLayout = view.findViewById(R.id.buttons_layout);
        if (pageNum != -1 && page != null) { // only inflate when we receive the pageNum arg and init page obj
            inflateButtons(buttonsLayout);
        }
        return view;
    }

    private void inflateButtons(RelativeLayout layout) {
        HashMap<Integer, String> map = page.getId2symptom();
        TreeSet<Integer> ts1= new TreeSet<Integer>(map.keySet());
        int size = map.size();
        //System.out.println(size);
        for (int id = 1; id <= size; id++) {


            Button button = new Button(layout.getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 90);
            button.setText(map.get(id));
            button.setId(id);
            int marginTop = 60 + ((id - 1) / 3) * 120;
            if ((id + 1) % 3 == 0) {
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                params.setMargins(0, marginTop, 0, 0);

            } else if (id % 3 == 0) {
                //params.setMargins(0, marginTop, 80, 0);
                params.setMargins(0, marginTop, 110, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            }
            else{
                params.setMargins(110, marginTop, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            }
            button.setLayoutParams(params);


            if (page.getChosen().contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            } else {
                button.setBackgroundResource(R.drawable.my_button_released);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    Button button = view.findViewById(id);
                    if (!page.getChosen().contains(id)) {
                        button.setBackgroundResource(R.drawable.my_button_pressed);
                        page.getChosen().add(id);
                    } else {
                        button.setBackgroundResource(R.drawable.my_button_released);
                        page.getChosen().remove(id);
                    }
                }
            });
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

    public void setPageNum(int num){
        this.pageNum = num;
    }
}