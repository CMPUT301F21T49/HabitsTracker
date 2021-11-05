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

import java.time.LocalDate;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventsTodayFragment extends Fragment {
    private User currentUser;
    private ArrayList<Habit> habitArrayList;
    public ArrayList<Event> todayEventList = new ArrayList<Event>();
    private LocalDate Today = LocalDate.now();
    private ListView listview;
    private EventAdapter adapter;


    public EventsTodayFragment() {
   // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            habitArrayList = currentUser.getHabits();
            for ( Habit h : habitArrayList) {
                ArrayList<Event> eventArrayList = h.getAllEvents();
                for (Event e:
                        eventArrayList) {
                    if (e.getDate() == Today && !(todayEventList.contains(e))) {
                        todayEventList.add(e);
                    }
                }
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.todays_events, container, false);
        listview = v.findViewById(R.id.today_events);
        ArrayList<Event> tempList = new ArrayList<Event>();
        adapter = new EventAdapter(tempList, getContext());
        listview.setAdapter((ListAdapter) adapter);
        return v;
    }

    public ArrayList<Habit> getHabitArrayList() {
        return habitArrayList;
    }
}
