package com.cmput301f21t49.habitstracker;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventsTodayFragment<habitArrayList> extends Fragment {
    private User currentUser;
    private final ArrayList<Habit> habitArrayList = currentUser.getHabits();
    private ArrayList<Event> todayEventList = new ArrayList<Event>();
    private LocalDate Today = LocalDate.now();
    private EventAdapter adapter;
    for (
    private Habit h: habitArrayList) {
        ArrayList<Event> eventArrayList = h.getAllEvents();
        for (Event e:
             eventArrayList) {
            if (e.getDate() == Today && !(todayEventList.contains(e))) {
                todayEventList.add(e);
            }
        }
    }


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
        return inflater.inflate(R.layout.todays_events, container, false);
    }

    public ArrayList<Habit> getHabitArrayList() {
        return habitArrayList;
    }
}
