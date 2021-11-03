package com.cmput301f21t49.habitstracker;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EditViewHabitEventActivity extends AppCompatActivity {

    Button Save;
    Button Escape;
    public User currentUser;
    List<String> habitNameList = new ArrayList<>();
    ArrayList<Habit> habitArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_event);

        currentUser = (User) getIntent().getSerializableExtra("CurrentUserObj");
        if (currentUser != null && currentUser.getHabits()!= null){
            for (Habit h : habitArrayList = currentUser.getHabits()) {
                habitNameList.add(h.getName());
            }

        }
        

    }
}
