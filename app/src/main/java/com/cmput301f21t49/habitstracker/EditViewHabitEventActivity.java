package com.cmput301f21t49.habitstracker;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EditViewHabitEventActivity extends AppCompatActivity {

    Button Save;
    Button Escape;
    public Habit currentHabit;
    List<String> eventNameList = new ArrayList<>();
    ArrayList<Event> eventArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_event);

        currentHabit = (Habit) getIntent().getSerializableExtra("CurrentHabitObj");
        if (currentHabit != null && currentHabit.getEvents()!= null){
            for (Event e : eventArrayList = currentHabit.getEvents()) {
                eventNameList.add(e.getName());
            }

        }


    }
}
