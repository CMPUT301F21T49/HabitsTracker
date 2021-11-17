package com.cmput301f21t49.habitstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
 * FollwerAdapter
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
 * Follower Adapter, extends {@link ArrayAdapter} for Users
 */
public class FollowerAdapter extends ArrayAdapter<User> {

    private ArrayList<User> userList;
    private boolean buttonToggle = false;

    /**
     * Follower Adapter Constructor
     * @param context Context of the adapter
     * @param userList list of users
     */
    public FollowerAdapter(Context context, ArrayList<User> userList) {
        super(context, 0, userList);
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //super.getView(position, convertView, parent);
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }
        //TODO: GET USER INFORMATION
        //userList.get()

        TextView userName = v.findViewById(R.id.textViewUsername);
        Button acceptButton = v.findViewById(R.id.button_accept);

        userName.setText("USER NAME HERE");

        if (!buttonToggle) {
            acceptButton.setClickable(false);
            acceptButton.setVisibility(View.INVISIBLE);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    /**
     * Toggle buttons on views
     * @param b state of button
     */
    public void setButtonToggle(boolean b) {
        buttonToggle = b;
    }
}
