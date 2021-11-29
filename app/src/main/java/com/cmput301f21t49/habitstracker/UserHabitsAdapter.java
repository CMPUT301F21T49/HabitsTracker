package com.cmput301f21t49.habitstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
 * UserHabitsAdapter
 *
 * version 1.1
 *
 * November 28, 2021
 *
 *Copyright [2021] CMPUT301F21T49: Purvi Singh, Justin. Saif, Fan Zhu

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 */

/**
 * Adapter for Other User Habits, extends {@link ArrayAdapter}
 */
public class UserHabitsAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habitList;
    private TextView textViewTitle;
    private TextView textViewDays;
    private TextView textViewDate;
    private TextView textViewReason;
    private TextView textViewPercentage;
    private TextView textViewPrivate;
    private ProgressBar progressBar;

    /**
     * Constructor for UserHabitsAdapter
     * @param context Context of adapter
     * @param habitList List of Habits
     */
    public UserHabitsAdapter(@NonNull Context context, ArrayList<Habit> habitList) {
        super(context, 0, habitList);
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.user_habit_item, parent, false);
        }

        // Get habit
        Habit habit = habitList.get(position);

        textViewTitle = v.findViewById(R.id.habit_title);
        textViewDate = v.findViewById(R.id.habit_date);
        textViewDays = v.findViewById(R.id.textViewDays);
        textViewReason = v.findViewById(R.id.habit_reason);
        textViewPercentage = v.findViewById(R.id.habit_pct);
        textViewPrivate = v.findViewById(R.id.privateTextView);
        progressBar = v.findViewById(R.id.habit_progressBar);


        // Set list item text
        // By default date is NaN
        String date = "NaN";
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").format(habit.getStartDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        textViewDays.setText(habit.getDays().toString());
        textViewTitle.setText(habit.getName());
        textViewDate.setText(date);
        textViewReason.setText(habit.getReason());
        textViewPrivate.setText(habit.getPrivateHabit() ? "(Private)" : "(Public)");
        textViewPercentage.setText(String.format("%.2f%%", habit.getPct()*100));
        progressBar.setProgress((int)(habit.getPct()*100));

        return v;
    }
}
