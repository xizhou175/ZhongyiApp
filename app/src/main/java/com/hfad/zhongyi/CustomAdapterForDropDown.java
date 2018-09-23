package com.hfad.zhongyi;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomAdapterForDropDown extends ArrayAdapter<DataModel> {
    private ArrayList<DataModel> dataSet;
    Context mContext;
    private ArrayList<Boolean> checkList = new ArrayList<Boolean>();

    public CustomAdapterForDropDown(ArrayList<DataModel> data, Context context){
        super(context, R.layout.row_item_dropdown, data);
        this.dataSet = data;
        this.mContext = context;

        for (int i = 0; i < dataSet.size(); i++) {
            checkList.add(false);
        }
    }

    public static class ViewHolder{
        TextView txtsym;
        ImageView delete;
    }

    private int lastPosition = -1;

    @Override
    public int getCount() {
        return ((null != dataSet) ? dataSet.size() : 0);
    }

    @Override
    public DataModel getItem(int position) {
        if(dataSet != null) return dataSet.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DataModel dataModel = getItem(position);
        CustomAdapter.ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new CustomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_dropdown, parent, false);
            viewHolder.txtsym = (TextView) convertView.findViewById(R.id.symptom);
            viewHolder.delete = (ImageView)convertView.findViewById(R.id.removeSign);
        }else{
            viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.txtsym.setText(dataModel.getSymptom());
        viewHolder.delete.setTag(position);
        viewHolder.delete.setOnClickListener(mListener);
        if(checkList.get(position) == false){
            System.out.println("position:" + position);
            ImageView plusOrMinus = viewHolder.delete.findViewById(R.id.removeSign);
            plusOrMinus.setImageResource(R.drawable.plus);
        }
        else{
            System.out.println("position:" + position);
            ImageView plusOrMinus = viewHolder.delete.findViewById(R.id.removeSign);
            plusOrMinus.setImageResource(R.drawable.if_minus_118643);
        }
        convertView.setTag(viewHolder);


        return convertView;
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(checkList.get((Integer)v.getTag()) == false) {
                System.out.println((Integer)v.getTag());
                checkList.set((Integer) v.getTag(), true);
            }
            else {
                System.out.println((Integer)v.getTag());
                checkList.set((Integer)v.getTag(), false);
            }
        }
    };

}
