package com.hfad.zhongyi;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;
    public int items = 0;

    public CustomAdapter(ArrayList<DataModel> data, Context context){
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.items = data.size();
    }

    public static class ViewHolder{
        TextView txtsym;
        ImageView delete;
    }

    private int lastPosition = -1;

    @Override
    public void onClick(View v){
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel)object;
        switch(v.getId()){
            case R.id.removeSign:
                String symptom = dataModel.getSymptom();
                for(int i = 0; i < Pages.pages.length; i++){
                    Page page = Pages.pages[i];
                    HashMap<String, Integer> symptom2id = page.getSymptom2id();
                    if(symptom2id.containsKey(symptom)){
                        page.setChosen(symptom2id.get(symptom));
                    }
                }
                dataSet.remove(dataModel);
                notifyDataSetChanged();
                items -= 1;
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtsym = (TextView) convertView.findViewById(R.id.symptom);
            viewHolder.delete = (ImageView)convertView.findViewById(R.id.removeSign);
            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.txtsym.setText(dataModel.getSymptom());
        viewHolder.delete.setTag(position);
        viewHolder.delete.setOnClickListener(this);
        viewHolder.delete.setTag(position);
        return convertView;
    }

}
