package com.cmput301f21t49.habitstracker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/*
 * MyRequestsFragment
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
 * A {@link Fragment} subclass. Shows user's requests.
 * Use the {@link MyRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRequestsFragment extends Fragment {

    private User user;
    private FollowerAdapter adapter;
    private ListView listView;

    public MyRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRequestsFragment newInstance(User param1) {
        MyRequestsFragment fragment = new MyRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(User.SERIALIZED, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(User.SERIALIZED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_requests, container, false);
        listView = v.findViewById(R.id.requests_list);
        // Set adapter
        adapter = new FollowerAdapter(getContext(), user.getEmail(), user.getRequests());
        adapter.toggleButtons();
        // Button 1 Accepts incoming request
        adapter.setButton1Text("Accept");
        adapter.setButton1Color(Color.BLUE);
        adapter.setButton1OnClickListener(new FollowerAdapter.ItemButtonOnClickListener() {
            @Override
            public void onClick(User mainUser, User user) {
                mainUser.removeRequest(user.getEmail());
                mainUser.addFollower(user.getEmail());
                user.addFollowing(mainUser.getEmail());
                adapter.remove(user.getEmail());
            }
        });

        // Button 2 Declines incoming request
        adapter.setButton2Text("Decline");
        adapter.setButton2Color(Color.RED);
        adapter.setButton2OnClickListener(new FollowerAdapter.ItemButtonOnClickListener() {
            @Override
            public void onClick(User mainUser, User user) {
                mainUser.removeRequest(user.getEmail());
                adapter.remove(user.getEmail());
            }
        });

        listView.setAdapter(adapter);
        return v;
    }
}