package com.cmput301f21t49.habitstracker;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditViewHabitEventActivity extends AppCompatActivity {

    Button Save;
    Button Escape;
    public Event currentEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_event);

        currentEvent = (Event) getIntent().getSerializableExtra("CurrentEventObj");
        if (currentHabit != null && currentHabit.getEvents()!= null){
            

        }


    }
}

