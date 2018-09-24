package com.hfad.zhongyi;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
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

public class BodySpecificFragments extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private int pageNum = -1;
    private Page page = null;
    ArrayList<String> ListOfOthers = new ArrayList<String>();
    View view;
    ListView lv;
    private ArrayList<Boolean> checkList = new ArrayList<Boolean>();
    ArrayList<DataModel> datamodels = new ArrayList<>();
    CustomAdapterForDropDown adapterForDropDown;

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

        GridLayout buttonsLayout = view.findViewById(R.id.buttons_layout);
        if (pageNum != -1 && page != null) { // only inflate when we receive the pageNum arg and init page obj
            inflateButtons(buttonsLayout);
            ListOfOthers = page.getOtherSymptoms();

            //populate listview

            for (String s : ListOfOthers) {
                datamodels.add(new DataModel(s));
            }

            for(int i = 0; i < datamodels.size(); i++){
                checkList.add(false);
            }

            CustomAdapterForDropDown adapter = new CustomAdapterForDropDown(datamodels, view.getContext());
            lv.setAdapter(adapter);

            //lv.setOnItemClickListener(this);

            ImageView DropDownSign = view.findViewById(R.id.DropDownSign);
            DropDownSign.setOnClickListener(this);
        }
        return view;
    }

    //list view onItemCLickListener
    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

        if(checkList.get(position) == false) {
            System.out.println((Integer)v.getTag());
            checkList.set(position, true);
        }
        else {
            System.out.println(position);
            checkList.set(position, false);
        }

        datamodels.get(position).setSymptom("1");

        adapterForDropDown = new CustomAdapterForDropDown(datamodels, view.getContext());


        adapterForDropDown.notifyDataSetChanged();
        lv.setAdapter(adapterForDropDown);

        /*ColorDrawable colorDrawable = (ColorDrawable)v.getBackground();
        TextView symtxt = v.findViewById(R.id.symptom);
        String symptom = symtxt.getText().toString();
        System.out.println(symptom);
        int symId = page.getSymptom2id().get(symptom);
        if(colorDrawable.getColor() != getResources().getColor(R.color.holo_blue_light)) {
            v.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
            ImageView plusOrMinus = v.findViewById(R.id.removeSign);
            plusOrMinus.setImageResource(R.drawable.if_minus_118643);
            page.getChosen().add(symId);
        }
        else {
            v.setBackgroundColor(getResources().getColor(R.color.lightgrey));
            ImageView plusOrMinus = v.findViewById(R.id.removeSign);
            plusOrMinus.setImageResource(R.drawable.plus);
            page.getChosen().remove(symId);
        }*/
    }


    private void inflateButtons(GridLayout layout) {
        HashMap<Integer, String> map = page.getId2symptom();
        for (int id = 1; id <= 6; id++) {
            Button button = new Button(layout.getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            button.setText(map.get(id));
            button.setId(id);
            button.setTextColor(getResources().getColor(R.color.white));
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
                title.setText("头部/面部症状");
            } else if(pageNum == 1){
                title.setText("胸部症状");
            } else if(pageNum == 2){
                title.setText("二便症状");
            } else if (pageNum == 3) {
                title.setText("腹部症状");
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