package com.cmput301f21t49.habitstracker;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/*
 * EventsTodayFragment
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
 * Events today fragment responsible for displaying events happened today
 * @see MainActivity
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventsTodayFragment extends Fragment {
    private User currentUser;
    private ArrayList<Habit> habitArrayList;
    private ArrayList<Event> todayEventList = new ArrayList<>();
    private ArrayList<String> displays;
    private Date todayDate = new Date();
    private ListView listview;
    private Calendar eventDay;
    private Calendar today;
    private ArrayAdapter<String> adapter;



    public EventsTodayFragment() {
   // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.todays_events, container, false);
        listview = v.findViewById(R.id.today_events);
        displays = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.today_events_item, R.id.todayEventText, displays);
        listview.setAdapter(adapter);
        // Load today's events into the arraylist
        eventDay = Calendar.getInstance();
        today = Calendar.getInstance();
        if (getArguments() != null) {
            currentUser = (User) getArguments().getSerializable("UserObj");
            //Event event = new Event();
            //event.setName("It works");
            //event.setCompletionDate(new Date());
            //currentUser.addEvent(0, event);
            habitArrayList = currentUser.getHabits();
            for ( Habit h : habitArrayList) {
                ArrayList<Event> eventArrayList = h.getEvents();
                for (Event e: eventArrayList) {
                    // Using Calendar to compare days, month, year
                    eventDay.setTime(e.getCompletionDate());
                    today.setTime(todayDate);
                    if (eventDay.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                            eventDay.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                            eventDay.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                        todayEventList.add(e);
                        displays.add(e.getName() + "\n" + "Associated Habit: " + h.getName());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
        return v;
    }

    /* This method is invoked once any item of the fragment's is clicked
    listview.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //int position = getAdapter
    }*/

}

