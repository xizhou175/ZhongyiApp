package com.hfad.zhongyi;

import android.app.Fragment;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

public class BodySpecificFragments extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private int pageNum = -1;
    private Page page = null;
    ArrayList<String> ListOfOthers = new ArrayList<>();
    View view;
    ListView lv;
    ArrayList<DataModel> dataModels = new ArrayList<>();
    CustomAdapterForDropDown adapter;

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

            //populate ListView
            for (String s : ListOfOthers) {
                Integer symId = page.symptom2id.get(s);
                dataModels.add(new DataModel(s, page.getChosen().contains(symId)));
            }

            adapter = new CustomAdapterForDropDown(dataModels, view.getContext());
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(this);

            ImageView DropDownSign = view.findViewById(R.id.DropDownSign);
            DropDownSign.setOnClickListener(this);
        }
        return view;
    }

    //list view onItemCLickListener
    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

        DataModel item = dataModels.get(position);

        TextView symtxt = v.findViewById(R.id.symptom);
        String symptom = symtxt.getText().toString();

        Integer symId = Pages.symptomToid.get(symptom);
        Integer idinPage = page.getSymptom2id().get(symptom);

        if (item.getSelected() == true) {
            item.setSelected(false);
            page.setChosen(idinPage);
            Pages.allChosen.remove(symId);
            Patient.getPatient().removeSymptom(Pages.idTosymptom.get(symId));
            DataModel.numSelected -= 1;
            adapter.notifyDataSetChanged();
        } else {
            item.setSelected(true);

            page.setChosen(idinPage);
            Pages.allChosen.add(symId);

            Patient.getPatient().addSymptom(Pages.idTosymptom.get(symId));
            DataModel.numSelected += 1;
            Intent intent = new Intent(getActivity(), ToSelectMoreSymptomsActivity.class);
            intent.putExtra(ToSelectMoreSymptomsActivity.EXTRA_MESSAGE, symptom);
            startActivity(intent);
            adapter.notifyDataSetChanged();
        }
    }


    private void inflateButtons(GridLayout layout) {
        HashMap<Integer, String> map = page.getId2symptom();
        for (int id = 1; id <= 6; id++) {
            if(page.getChosen().contains(id)) page.setChosen(id);
            final String sym  = map.get(id);

            Button button = new Button(layout.getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            button.setText(sym);
            button.setId(id);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setLayoutParams(params);

            if (Pages.allChosen.contains(id)) {
                button.setBackgroundResource(R.drawable.my_button_pressed);
            } else {
                button.setBackgroundResource(R.drawable.my_button_released);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    Button button = view.findViewById(id);
                    String symptom = button.getText().toString();
                    int symId = Pages.symptomToid.get(symptom);
                    if (!Pages.allChosen.contains(symId)) {
                        button.setBackgroundResource(R.drawable.my_button_pressed);
                        page.setChosen(symId);
                        Pages.allChosen.add(symId);
                        Patient.getPatient().addSymptom(Pages.idTosymptom.get(symId));
                        DataModel.numSelected += 1;
                        Intent intent = new Intent(getActivity(), ToSelectMoreSymptomsActivity.class);
                        intent.putExtra(ToSelectMoreSymptomsActivity.EXTRA_MESSAGE, symptom);
                        startActivity(intent);
                    } else {
                        button.setBackgroundResource(R.drawable.my_button_released);
                        Pages.allChosen.remove(symId);
                        page.setChosen(symId);
                        Patient.getPatient().removeSymptom(Pages.idTosymptom.get(symId));
                        DataModel.numSelected -= 1;
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
            } else if(pageNum == 3){
                title.setText("二便症状");
            } else if (pageNum == 2) {
                title.setText("腹部症状");
            } else if(pageNum == 4){
                title.setText("生殖症状");
            } else if(pageNum == 5){
                title.setText("四肢症状");
            } else if(pageNum == 6){
                title.setText("其他");
            }
        }
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
}