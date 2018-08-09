package com.hfad.zhongyi;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.TreeSet;

public class BodySpecificFragments extends Fragment implements View.OnClickListener, ListView.OnItemClickListener{

    private int pageNum = -1;
    private Page page = null;
    ArrayList<String> ListOfOthers = new ArrayList<String>();
    View view;
    ListView lv;

    public static BodySpecificFragments newInstance(int pg) {
        Bundle args = new Bundle();
        args.putInt("pageNum", pg);
        BodySpecificFragments frag = new BodySpecificFragments();
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_specific_symptoms, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageNum = bundle.getInt("pageNum");
            page = Pages.pages[pageNum];
        }
        setPageTitle(view);
        lv = view.findViewById(R.id.lvOfOthers);
        RelativeLayout buttonsLayout = view.findViewById(R.id.buttons_layout);
        if (pageNum != -1 && page != null) { // only inflate when we receive the pageNum arg and init page obj
            inflateButtons(buttonsLayout);
            ListOfOthers = page.getOtherSymptoms();

            //populate listview
            ArrayList<DataModel> datamodels = new ArrayList<>();
            for (String s : ListOfOthers) {
                datamodels.add(new DataModel(s));
            }

            CustomAdapterForDropDown adapter = new CustomAdapterForDropDown(datamodels, view.getContext());
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
            lv.post(new Runnable(){
                @Override
                public void run(){
                    setListView(lv);
                }
            });
            ImageView DropDownSign = view.findViewById(R.id.DropDownSign);
            DropDownSign.setOnClickListener(this);
        }

        return view;
    }

    //list view onItemCLickListener
    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
        ColorDrawable colorDrawable = (ColorDrawable)lv.getChildAt(position).getBackground();
        TextView symtxt = v.findViewById(R.id.symptom);
        String symptom = symtxt.getText().toString();
        int symId = page.getSymptom2id().get(symptom);
        if(colorDrawable.getColor() != getResources().getColor(R.color.holo_blue_light)) {
            lv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
            ImageView plusOrMinus = lv.getChildAt(position).findViewById(R.id.removeSign);
            plusOrMinus.setImageResource(R.drawable.if_minus_118643);
            page.getChosen().add(symId);
        }
        else {
            lv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.lightgrey));
            ImageView plusOrMinus = lv.getChildAt(position).findViewById(R.id.removeSign);
            plusOrMinus.setImageResource(R.drawable.plus);
            page.getChosen().remove(symId);
        }
    }


    private void inflateButtons(RelativeLayout layout) {
        HashMap<Integer, String> map = page.getId2symptom();
        TreeSet<Integer> ts1= new TreeSet<Integer>(map.keySet());
        int size = map.size();
        for (int id = 1; id <= 6; id++) {


            Button button = new Button(layout.getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 90);
            button.setText(map.get(id));
            button.setId(id);
            button.setTextColor(getResources().getColor(R.color.white));
            int marginTop = 60 + ((id - 1) / 3) * 120;
            if ((id + 1) % 3 == 0) {
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                params.setMargins(0, marginTop, 0, 0);

            } else if (id % 3 == 0) {
                //params.setMargins(0, marginTop, 80, 0);
                params.setMargins(0, marginTop, 155, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            }
            else{
                params.setMargins(150, marginTop, 0, 0);
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

    @Override
    public void onClick(View v) {
        ListView lv = view.findViewById(R.id.lvOfOthers);
        if(lv.getVisibility() == View.INVISIBLE) {
            lv.setVisibility(View.VISIBLE);
        }
        else{
            lv.setVisibility(View.INVISIBLE);
        }
    }

    public void setListView(ListView lv) {
        for(int position = 0; position < lv.getChildCount(); position++){
            View v = lv.getChildAt(position);
            TextView symView = v.findViewById(R.id.symptom);
            String symptom = symView.getText().toString();
            int symId = page.getSymptom2id().get(symptom);
            if(page.getChosen().contains(symId)){
                lv.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                ImageView plusOrMinus = lv.getChildAt(position).findViewById(R.id.removeSign);
                plusOrMinus.setImageResource(R.drawable.if_minus_118643);
            }
        }
    }


}