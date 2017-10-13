package com.example.weiting.ui_02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Weiting on 2017/10/9.
 */

public class deviceAdapter extends BaseAdapter {
    public Data[] data;
    public int view;
    public Context context;
    public deviceAdapter(Data[] data,int view,Context context){
        this.data = data;
        this.view = view;
        this.context = context;
    }
    @Override
    public  int getCount(){
        return data.length;
    }
    @Override
    public Data getItem(int position){
        return data[position];
    }
    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public View getView(int position, View rowView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        rowView = inflater.inflate(view,parent,false);
        TextView name = (TextView) rowView.findViewById(R.id.deviceName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.view);
        name.setText(data[position].name);
        imageView.setImageResource(data[position].photo);
        return rowView;
    }
}
