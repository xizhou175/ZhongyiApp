package com.hfad.zhongyi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapterForDropDown extends ArrayAdapter<DataModel> {
    private ArrayList<DataModel> dataSet;
    Context mContext;

    public CustomAdapterForDropDown(ArrayList<DataModel> data, Context context){
        super(context, R.layout.row, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public static class ViewHolder{
        TextView txtsym;
        ImageView delete;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DataModel dataModel = getItem(position);
        CustomAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new CustomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row, parent, false);
            viewHolder.txtsym = (TextView) convertView.findViewById(R.id.symptom);
            viewHolder.delete = (ImageView)convertView.findViewById(R.id.removeSign);
            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.txtsym.setText(dataModel.getSymptom());
        viewHolder.delete.setTag(position);
        viewHolder.delete.setTag(position);
        return convertView;
    }

}
