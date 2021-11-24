package com.cmput301f21t49.habitstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyHistoryAdapter extends ArrayAdapter<Event> {
    private int mResource;

    public MyHistoryAdapter(Context context, int resource, ArrayList<Event> objects) {
        super(context, resource, objects);
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.my_history_item,parent,false);


        TextView EName = (TextView) convertView.findViewById(R.id.textView5);
        TextView EDate = (TextView) convertView.findViewById(R.id.textView6);

        Date completionDate;
        String sDate;

        String name = getItem(position).getName();
        EName.setText(name); //Set name

        //Set Date
        if(getItem(position).getStatus() == Boolean.TRUE){
            completionDate = getItem(position).getCompletionDate();
            sDate = new SimpleDateFormat("dd/MM/yyyy").format(completionDate);
        }else{
            sDate = "N/A";
        }
        EDate.setText(sDate); //Set Date

        return convertView;
    }
}
