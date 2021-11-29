package com.cmput301f21t49.habitstracker;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class EventsTodayFragment extends Fragment implements EditHabitEventFragment.OnFragmentInteractionListener{
    private User currentUser;
    private ArrayList<Habit> habitArrayList;
    private ArrayList<Event> eventList;
    private ArrayList<String> displays;
    private Date todayDate;
    private ListView listview;
    private Calendar eventDay;
    private Calendar today;
    private Calendar habitStartDate;
    private ArrayAdapter<String> adapter;
    private ArrayList<Pair<Integer, Integer>> indices = new ArrayList<>();
    private ManageUser manageUser = ManageUser.getInstance();
    private Button get_events;




    public EventsTodayFragment() {
   // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = (User) getArguments().getSerializable("UserObj");
        habitArrayList = currentUser.getHabits();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.todays_events, container, false);
        get_events = (Button) v.findViewById(R.id.button6);
        listview = v.findViewById(R.id.today_events);
        displays = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.today_events_item, R.id.todayEventText, displays);
        listview.setAdapter(adapter);
        habitStartDate = Calendar.getInstance();
        eventDay = Calendar.getInstance();
        today = Calendar.getInstance();
        todayDate = new Date();
        updateList(currentUser);


        get_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hIndex = 0;
                for ( Habit h : habitArrayList) {
                    ArrayList<String> days = h.getDays();
                    habitStartDate.setTime(h.getStartDate());
                    today.setTime(todayDate);
                    // Check if after habit start date
                    if (today.get(Calendar.YEAR) > habitStartDate.get(Calendar.YEAR) ||
                            (today.get(Calendar.YEAR) == habitStartDate.get(Calendar.YEAR) && today.get(Calendar.MONTH) > habitStartDate.get(Calendar.MONTH)) ||
                            (today.get(Calendar.YEAR) == habitStartDate.get(Calendar.YEAR) && today.get(Calendar.MONTH) == habitStartDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) >= habitStartDate.get(Calendar.DAY_OF_MONTH))) {
                        // Now check if day = a day in habit
                        String day = getDay(today.get(Calendar.DAY_OF_WEEK));
                        for (int i = 0; i < days.size(); i++) {
                            if (day.equals(days.get(i))) {
                                Event e = new Event();
                                e.setName("New Event");
                                e.setStatus(false);
                                currentUser.addEvent(hIndex, e);
                                updateDatabase();
                                updateUser();
                                break;

                            }
                        }
                    }
                    hIndex++;
                }
                adapter.clear();
                updateList(currentUser);
            }
        });




       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int hIndex = indices.get(i).first;
                int eIndex = indices.get(i).second;

                Event event = currentUser.getHabits().get(hIndex).getEvent(eIndex);
                new EditHabitEventFragment().newInstance(event, hIndex, eIndex, i).show(getChildFragmentManager(), "Update");
            }
        });
        return v;
    }


    /**
     * Function to update database
     */
    public void updateDatabase() {
        manageUser.createOrUpdate(currentUser, new VoidCallback() {
            @Override
            public void onCallback() {

            }
        });
    }

    /**
     * Function which updates the currentuser
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
     * Function to be called by listener once event is edited
     * @param e Event being edited
     * @param hI Habit index
     * @param eI Event Index
     * @param index Index on listview
     */
    public void onEdit(Event e, int hI, int eI, int index) {
        System.out.println(hI);
        System.out.println(eI);
        currentUser.getHabits().get(hI).updateEvent(eI, e);
        updateDatabase();
        updateUser();
        Habit h = currentUser.getHabits().get(hI);
        displays.set(index, e.getName() + "\n" + "Associated Habit: " + h.getName());
        adapter.notifyDataSetChanged();
    }

    /**
     * Function to be called by listener once event is completed
     * @param e Event being edited
     * @param hI Habit index
     * @param eI Event Index
     * @param index Index on listview
     */
    @Override
    public void onComplete(Event e, int hI, int eI, int index) {
        currentUser.getHabits().get(hI).updateEvent(eI, e);
        updateDatabase();
        updateUser();
        displays.remove(index);
        indices.remove(index);
        adapter.notifyDataSetChanged();
    }

    /**
     * Function that updates list view
     * @param currentUser current user
     */
    private void updateList(User currentUser) {
        indices.clear();
        habitArrayList = currentUser.getHabits();
        int hIndex = 0;
        for ( Habit h : habitArrayList) {
            ArrayList<String> days = h.getDays();
            habitStartDate. setTime(h.getStartDate());
            today.setTime(todayDate);
            // Check if after habit start date
            if (today.get(Calendar.YEAR) >= habitStartDate.get(Calendar.YEAR) &&
                    today.get(Calendar.MONTH) >= habitStartDate.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) >= habitStartDate.get(Calendar.DAY_OF_MONTH)) {
                // Now check if day = a day in habit
                String day = getDay(today.get(Calendar.DAY_OF_WEEK));
                for (int i = 0; i < days.size(); i++) {
                    if (day.equals(days.get(i))) {
                        eventList = currentUser.getHabits().get(hIndex).getEvents();
                        int eIndex = 0;
                        for (Event e: eventList) {
                            // Add one event from each habit until event is complete
                            if (e.getStatus() == false) {
                                displays.add(e.getName() + "\n" + "Associated Habit: " + h.getName());
                                indices.add(new Pair<Integer, Integer>(hIndex, eIndex));
                                adapter.notifyDataSetChanged();

                            }
                            eIndex++;
                        }
                    }
                }
            }
            hIndex++;
        }

    }


    /**
     * Function that converts number returned by calendar into a day
     * @param num The number to be converted (1-7)
     * @return Strimg of day
     */
    private String getDay(int num) {
        if (num == 1) {
            return "Sun";
        }
        else if (num == 2) {
            return "Mon";
        }
        else if (num == 3) {
            return "Tue";
        }
        else if (num == 4) {
            return "Wed";
        }
        else if (num == 5) {
            return "Thur";
        }
        else if (num == 6) {
            return "Fri";
        }
        else {
            return "Sat";
        }
    }



}

