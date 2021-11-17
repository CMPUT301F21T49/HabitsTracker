package com.cmput301f21t49.habitstracker;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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
 * @see MainActivity
 */

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
 * @see MainActivity
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyHistoryFragment extends Fragment {
    private User currentUser;
    private ArrayList<Habit> habitArrayList;
    public ArrayList<Event> historyEventList = new ArrayList<Event>();
    private LocalDate Today = LocalDate.now();
    private ListView listview;
    private EventAdapter adapter;


    public MyHistoryFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Load today's events into the arraylist
        if (getArguments() != null) {
            habitArrayList = currentUser.getHabits();
            for ( Habit h : habitArrayList) {
                ArrayList<Event> eventArrayList = h.getAllEvents();
                for (Event e:
                        eventArrayList) {
                    if (e.getDate() == Today && !(historyEventList.contains(e))) {
                        historyEventList.add(e);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.my_history_events, container, false);
        listview = v.findViewById(R.id.history_events);
        ArrayList<Event> tempList = new ArrayList<Event>();
        tempList.add(new Event());
        adapter = new EventAdapter(tempList, getContext());
        listview.setAdapter((ListAdapter) adapter);
        return v;
    }
}
