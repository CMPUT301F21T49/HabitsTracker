package com.cmput301f21t49.habitstracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
/*
 * SignUpActivity
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
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFollowersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFollowersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FollowerAdapter adapter;

    // TODO: Rename and change types of parameters
    private User user;
    private ArrayList<String> userList;

    private ListView listView;

    public MyFollowersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyFollowersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFollowersFragment newInstance(User user) {
        MyFollowersFragment fragment = new MyFollowersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM2, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mParam2.addFollower(new User("TEST"));
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_followers, container, false);
        listView = v.findViewById(R.id.followers_list);
        adapter = new FollowerAdapter(getContext(), user.getEmail(), user.getFollowers(), FollowerAdapter.SWITCH);
        listView.setAdapter(adapter);
        return v;
    }
}