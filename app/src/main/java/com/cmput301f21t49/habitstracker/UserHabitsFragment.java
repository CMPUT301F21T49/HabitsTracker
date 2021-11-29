package com.cmput301f21t49.habitstracker;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
/*
 * UserHabitsFragment
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
 * A {@link DialogFragment} subclass. Show followed user's habit lists.
 * Use the {@link UserHabitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHabitsFragment extends DialogFragment {

    private static final String CURRENTUSER = "current_user";
    private static final String OTHERUSER = "other_user";

    private String userEmail;
    private String currentUserEmail;
    private ManageUser manageUser = ManageUser.getInstance();

    public UserHabitsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param currentUserEmail String of current user email
     * @param userEmail String of the user requested
     *
     * @return A new instance of fragment UserHabitsFragment.
     */
    public static UserHabitsFragment newInstance(String currentUserEmail, String userEmail) {
        UserHabitsFragment fragment = new UserHabitsFragment();
        Bundle args = new Bundle();
        args.putString(CURRENTUSER, currentUserEmail);
        args.putString(OTHERUSER, userEmail);
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            currentUserEmail = getArguments().getString(CURRENTUSER);
            userEmail = getArguments().getString(OTHERUSER);
        }

        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.fragment_user_habits);
        // Set layout to fill screen
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ListView listView = (ListView) d.findViewById(R.id.habits_list);
        TextView textViewEmail = (TextView) d.findViewById(R.id.user_email);
        Button button = (Button) d.findViewById(R.id.button);

        textViewEmail.setText(userEmail);
        //On Click, dismiss fragment
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        manageUser.getByEmail(userEmail, new UserCallback() {
            @Override
            public void onCallback(User user) {
                ArrayList<Habit> publicHabits = new ArrayList<>();
                // Get all public habits
                for (Habit h : user.getHabits()) {
                    if (!h.getPrivateHabit()) {
                        publicHabits.add(h);
                    }
                }
                UserHabitsAdapter adapter = new UserHabitsAdapter(getContext(), publicHabits);
                listView.setAdapter(adapter);
            }
        });
        return d;
    }
}