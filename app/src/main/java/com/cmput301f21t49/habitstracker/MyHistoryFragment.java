package com.cmput301f21t49.habitstracker;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import java.time.LocalDate;
import java.util.ArrayList;
/*
 * MyHistoryFragment
 *
 * version 1.0
 *
 * November 3, 2021
 *
 *Copyright [2021] CMPUT301F21T49: Purvi Singh, Justin. Saif, Fan Zhu

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 */

/**
 * My history fragment responsible for displaying history of events
 * @author Purvi S.
 * @see MainActivity
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyHistoryFragment extends Fragment {
    private User currentUser;
    public ArrayList<Event> historyEventList = new ArrayList<Event>();
    private ListView listView;
    private MyHistoryAdapter myHistoryAdapter;


    public MyHistoryFragment() {
       // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.my_history_events, container, false);

        if (getArguments() != null) {
            currentUser = (User) this.getArguments().getSerializable("UserObj");
        }else{
            System.out.println(currentUser.getId());
        }


        for(Habit h: currentUser.getHabits()){
            for(Event e: h.getEvents()){
                if(e.getStatus() == Boolean.TRUE){
                    historyEventList.add(e);
                }

            }
        }

        listView = (ListView) v.findViewById(R.id.history_events);
        myHistoryAdapter = new MyHistoryAdapter(getActivity(),R.layout.my_history_item,historyEventList);
        listView.setAdapter(myHistoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return v;
    }


}
