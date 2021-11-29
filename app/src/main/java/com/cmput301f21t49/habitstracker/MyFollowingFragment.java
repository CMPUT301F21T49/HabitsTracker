package com.cmput301f21t49.habitstracker;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
/*
 * MyFollowingFragment
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
 * A {@link Fragment} subclass. Shows user's following list.
 * Use the {@link MyFollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFollowingFragment extends Fragment {


    private User user;
    private ListView listView;
    private FollowerAdapter adapter;

    public MyFollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param user User currently logged in
     *
     * @return A new instance of fragment MyFollowingFragment.
     */
    public static MyFollowingFragment newInstance(User user) {
        MyFollowingFragment fragment = new MyFollowingFragment();

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
        View v = inflater.inflate(R.layout.fragment_my_following, container, false);
        listView = v.findViewById(R.id.following_list);

        // Set adapter
        adapter = new FollowerAdapter(getContext(), user.getEmail(), user.getFollowing());
        // Button 1 allows to view that user's public habit list
        adapter.toggleButton1();
        adapter.setButton1Text("VIEW");
        adapter.setButton1OnClickListener(new FollowerAdapter.ItemButtonOnClickListener() {
            @Override
            public void onClick(User mainUser, User user) {
                UserHabitsFragment.newInstance(mainUser.getEmail(), user.getEmail()).show(getChildFragmentManager(), "FOLLOWING_HABITS");
            }
        });
        // Button 2 unfollows that user
        adapter.toggleButton2();
        adapter.setButton2Color(Color.RED);
        adapter.setButton2Text("Unfollow");
        adapter.setButton2OnClickListener(new FollowerAdapter.ItemButtonOnClickListener() {
            @Override
            public void onClick(User mainUser, User user) {
                mainUser.removeFollowing(user.getEmail());
                user.removeFollower(mainUser.getEmail());
                adapter.remove(user.getEmail());
            }
        });

        listView.setAdapter(adapter);
        return v;
    }
}