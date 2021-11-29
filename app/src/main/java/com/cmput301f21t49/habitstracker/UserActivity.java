package com.cmput301f21t49.habitstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
/*
 * UserActivity
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
 * Activity responsible for showing user information in addition to their followers/following.
 */

public class UserActivity extends AppCompatActivity {
    private ImageView userImageView;
    private TextView usernameTextView;
    private TextView uidTextView;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private EditText editFollowName;
    private Button sendButton;

    private User currentUser;
    private ManageUser manageUser = ManageUser.getInstance();

    private Fragment currentFragment;
    private MyFollowersFragment followersFragment;
    private MyFollowingFragment followingFragment;
    private MyRequestsFragment requestsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Retrieve User
        currentUser = (User) getIntent().getSerializableExtra(User.SERIALIZED);

        userImageView = findViewById(R.id.userImageView);
        usernameTextView = findViewById(R.id.username);
        uidTextView = findViewById(R.id.userid);
        tabLayout = findViewById(R.id.tabs);
        frameLayout = findViewById(R.id.frame);
        editFollowName = findViewById(R.id.editTextFollowName);
        sendButton = findViewById(R.id.sendButton);

        // Set user email and uid
        usernameTextView.setText(currentUser.getEmail());
        uidTextView.setText(currentUser.getId());

        // Create fragments
        followersFragment = MyFollowersFragment.newInstance(currentUser);
        followingFragment = MyFollowingFragment.newInstance(currentUser);
        requestsFragment = MyRequestsFragment.newInstance(currentUser);

        // Set new tabs. First tab is followers. Second Tab is for Following.
        // Third Tab is for Requests.
        TabLayout.Tab followingTab = tabLayout.newTab();
        TabLayout.Tab followersTab = tabLayout.newTab();
        TabLayout.Tab requestsTabs = tabLayout.newTab();

        followersTab.setText("Followers");
        followingTab.setText("Following");
        requestsTabs.setText("Requests");
        tabLayout.addTab(followersTab);
        tabLayout.addTab(followingTab);
        tabLayout.addTab(requestsTabs);

        // On tab change, change fragment.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                manageUser.get(currentUser.getId(), new UserCallback() {
                    @Override
                    public void onCallback(User user) {
                        currentUser = user;
                        switch (tab.getPosition()) {
                            case 0:
                                currentFragment = MyFollowersFragment.newInstance(currentUser);
                                break;
                            case 1:
                                currentFragment = MyFollowingFragment.newInstance(currentUser);
                                break;
                            case 2:
                                currentFragment = MyRequestsFragment.newInstance(currentUser);
                                break;
                        }
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame, currentFragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                    }
                });

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Nothing to implement
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Nothing to implement
            }
        });

        // Send Request to a user
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String followName = editFollowName.getText().toString();
                // user cannot follow self
                if (followName.equals(currentUser.getEmail())) {
                    Toast.makeText(getApplicationContext(), "Cannot follow self!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // clear text box
                editFollowName.setText("");
                manageUser.getByEmail(followName, new UserCallback() {
                    @Override
                    public void onCallback(User user) {
                        if (user.getFollowers().contains(currentUser.getEmail())) {
                            Toast.makeText(getApplicationContext(), "Cannot follow someone you are already following!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        user.addRequest(currentUser.getEmail());
                        manageUser.createOrUpdate(user, new VoidCallback() {
                            @Override
                            public void onCallback() {
                                Toast.makeText(getApplicationContext(), "Request sent!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, new VoidCallback() {
                    @Override
                    public void onCallback() {
                        Toast.makeText(getApplicationContext(), "Request could not be send. Double-Check user email!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Set initial fragment to MyFollowersFragment
        currentFragment = followersFragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame, currentFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    /**
     * https://stackoverflow.com/questions/6554317/savedinstancestate-is-always-null
     * @param item menu item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}