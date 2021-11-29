package com.cmput301f21t49.habitstracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/*
 * MyFollowersFragment
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
 * A {@link Fragment} subclass. Shows user's followers.
 * Use the {@link MyFollowersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFollowersFragment extends Fragment {

    private User user;
    private ListView listView;
    private FollowerAdapter adapter;

    public MyFollowersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param user User currently logged in
     *
     * @return A new instance of fragment MyFollowersFragment.
     */
    public static MyFollowersFragment newInstance(User user) {
        MyFollowersFragment fragment = new MyFollowersFragment();
        Bundle args = new Bundle();
        args.putSerializable(User.SERIALIZED, user);
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
        View v = inflater.inflate(R.layout.fragment_my_followers, container, false);
        listView = v.findViewById(R.id.followers_list);

        // Set FollowerAdapter
        adapter = new FollowerAdapter(getContext(), user.getEmail(), user.getFollowers());
        listView.setAdapter(adapter);
        return v;
    }
}