package com.cmput301f21t49.habitstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class EventAdapter extends ArrayAdapter<Event> {

    ArrayList<Event> tempList;


    public EventAdapter(ArrayList<Event> tempList, Context context) {
        super(context, 0, tempList);
        this.tempList = tempList;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        }

        return v;
    }


}




