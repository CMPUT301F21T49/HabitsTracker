package com.cmput301f21t49.habitstracker;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    private ArrayList<Pair<Integer, Integer>> indices = new ArrayList<>();
    private ManageUser manageUser = ManageUser.getInstance();



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
//            Event test = new Event();
//            test.setName("Testing");
//            test.setStatus(false);
//            test.setCompletionDate(new Date());
//            currentUser.addEvent(0, test);
//            System.out.println(currentUser.getHabits().get(0).getEvents().get(0).getName());
//            System.out.println(currentUser.getHabits().get(0).getEvents().get(0).getStatus());
            habitArrayList = currentUser.getHabits();
            int hIndex = 0;
            for ( Habit h : habitArrayList) {
                ArrayList<Event> eventArrayList = h.getEvents();
                int eIndex = 0;
                for (Event e: eventArrayList) {
                    // Using Calendar to compare days, month, year
                    eventDay.setTime(e.getCompletionDate());
                    today.setTime(todayDate);
                    if (eventDay.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                            eventDay.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                            eventDay.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                        todayEventList.add(e);
                        Pair pair = new Pair(hIndex, eIndex);
                        indices.add(pair);
                        if (e.getStatus() == false) {
                            displays.add(e.getName() + "\n" + "Associated Habit: " + h.getName() + "\t Status: In Progress");
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            displays.add(e.getName() + "\n" + "Associated Habit: " + h.getName() + "\t Status: Completed");
                            adapter.notifyDataSetChanged();
                        }
                        eIndex++;
                    }
                }
                hIndex++;
            }
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int hIndex = indices.get(i).first;
                int eIndex = indices.get(i).second;
                if (currentUser.getHabits().get(hIndex).getEvents().get(eIndex).getStatus() == false) {
                    currentUser.getHabits().get(hIndex).getEvents().get(eIndex).setStatus(true);
                    Habit h = currentUser.getHabits().get(hIndex);
                    Event e = currentUser.getHabits().get(hIndex).getEvents().get(eIndex);
                    displays.set(i, e.getName() + "\n" + "Associated Habit: " + h.getName() + "\t Status: Completed");
                    updateDatabase();
                    adapter.notifyDataSetChanged();
                }
                else if (currentUser.getHabits().get(hIndex).getEvents().get(eIndex).getStatus() == true) {
                    currentUser.getHabits().get(hIndex).getEvents().get(eIndex).setStatus(false);
                    Habit h = currentUser.getHabits().get(hIndex);
                    Event e = currentUser.getHabits().get(hIndex).getEvents().get(eIndex);
                    displays.set(i, e.getName() + "\n" + "Associated Habit: " + h.getName() + "\t Status: In Progress");
                    updateDatabase();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return v;
    }

    public void updateDatabase() {
        manageUser.createOrUpdate(currentUser, new VoidCallback() {
            @Override
            public void onCallback() {

            }
        });
    }

    public void updateUser() {
        manageUser.get(currentUser.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                currentUser = user;
            }
        });
    }




}

