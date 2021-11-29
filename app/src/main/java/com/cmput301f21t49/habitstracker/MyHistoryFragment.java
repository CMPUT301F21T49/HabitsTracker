package com.cmput301f21t49.habitstracker;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class MyHistoryFragment extends Fragment implements EditEventOnHistoryFragment.OnFragmentInteractionListener{
    private User currentUser;
    public ArrayList<Event> historyEventList = new ArrayList<Event>();
    private ListView listView;
    private MyHistoryAdapter myHistoryAdapter;
    ManageUser manageUser = ManageUser.getInstance();
    private Event selectedEvent;
    private ArrayList<Pair<Integer, Integer>> indices = new ArrayList<>();
    private int index;
    private int habitIndex;
    private int eventIndex;


    public MyHistoryFragment() {
       // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.my_history_events, container, false);

        if (getArguments() != null) {
            currentUser = (User) this.getArguments().getSerializable("UserObj");
            //updateUser();
        }else{
            System.out.println(currentUser.getId());
        }


        updateList();



        listView = (ListView) v.findViewById(R.id.history_events);
        myHistoryAdapter = new MyHistoryAdapter(getActivity(),R.layout.my_history_item,historyEventList);
        listView.setAdapter(myHistoryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(indices.get(i));
                selectedEvent = (Event) listView.getItemAtPosition(i);
                habitIndex = indices.get(i).first;
                eventIndex = indices.get(i).second;
                index = i;
                new EditEventOnHistoryFragment().newInstance(selectedEvent, habitIndex, eventIndex, index).show(getChildFragmentManager(), "Update");
            }
        });

        return v;
    }

    /**
     * Function that updates database
     */
    public void updateDatabase() {
        manageUser.createOrUpdate(currentUser, new VoidCallback() {
            @Override
            public void onCallback() {

            }
        });
    }

    /**
     * Function that updates the user
     */
    public void updateUser() {
        manageUser.get(currentUser.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                currentUser = user;
            }
        });
    }

    /**
     * Function which updates the list
     */
    public void updateList() {
        indices.clear();
        historyEventList.clear();
        int hIndex = 0;
        for(Habit h: currentUser.getHabits()){
            int eIndex = 0;
            for(Event e: h.getEvents()){
                if(e.getStatus() == Boolean.TRUE){
                    historyEventList.add(e);
                    indices.add(new Pair<Integer, Integer>(hIndex, eIndex));
                }
                eIndex++;
            }
            hIndex++;
        }
    }


    /**
     * Function to be called by listener once event is edited
     * @param e Event being edited
     * @param hI Habit index
     * @param eI Event Index
     * @param index Index on listview
     */
    @Override
    public void onEdit(Event e, int hI, int eI, int index) {
        System.out.println(hI);
        System.out.println(eI);
        currentUser.getHabits().get(hI).updateEvent(eI, e);
        updateDatabase();
        updateUser();
        historyEventList.set(index, e);
        myHistoryAdapter.notifyDataSetChanged();


    }

    /**
     * Function to be called by listener once event is deleted
     * @param e Event being edited
     * @param hI Habit index
     * @param eI Event Index
     * @param index Index on listview
     */
    @Override
    public void onDelete(Event e, int hI, int eI, int index) {
        currentUser.getHabits().get(hI).deleteEvent(eI);
        updateDatabase();
        updateUser();
        updateList();
        myHistoryAdapter.notifyDataSetChanged();


    }

}
