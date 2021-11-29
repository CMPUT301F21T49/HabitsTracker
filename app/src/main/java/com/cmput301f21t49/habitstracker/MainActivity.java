package com.cmput301f21t49.habitstracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/*
 *MainActivity
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
 * This is the Main Activity page that displays the user events and has a menu to traverse to other activities
 * @author team 49
 * @version 1.0
 * @see User
 * @see LoginActivity
 * @see SignUpActivity
 * @since 1.0
 */

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth fAuth;
    public User currentUser;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nv;
    private ManageUser manageUser = ManageUser.getInstance();
    private Fragment fragment;

    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();

        fAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        currentUser = (User) getIntent().getSerializableExtra(User.SERIALIZED);
//        manageUser.get(currentUser.getId(), new UserCallback() {
//            @Override
//            public void onCallback(User user) {
//                currentUser = user;
//                System.out.println("Updated Main User");
//            }
//        });
        System.out.println("CALL TO USER");
        if(currentUser.getHabits() != null){
            System.out.println(currentUser.getHabits().size());
        }
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                manageUser.get(currentUser.getId(), new UserCallback() {
                    @Override
                    public void onCallback(User user) {
                        currentUser = user;
                        Intent intent;
                        switch(id)
                        {
                            case R.id.habits:
                                drawerLayout.closeDrawers();
                                intent = new Intent(MainActivity.this, MyHabitsActivity.class);
                                intent.putExtra(User.SERIALIZED, currentUser);
                                startActivity(intent);
                                break;

                            case R.id.following:
                                drawerLayout.closeDrawers();
                                intent = new Intent(MainActivity.this, UserActivity.class);
                                intent.putExtra(User.SERIALIZED, currentUser);
                                startActivity(intent);
                                break;

                            case R.id.signout:
                                fAuth.signOut();

                            default:
                                break;
                        }

                    }
                });
                return true;
            }
        });

        // get the reference of FrameLayout and TabLayout
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // Create a new Tab named "First"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Events Today"); // set the Text for the first Tab

        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
        // Create a new Tab named "Second"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("My History"); // set the Text for the second Tab

        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
        // perform setOnTabSelectedListener event on TabLayout

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            // get the current selected tab's position and replace the fragment accordingly
                fragment = null;
                manageUser.get(currentUser.getId(), new UserCallback() {
                    @Override
                    public void onCallback(User user) {
                        switch (tab.getPosition()) {
                            case 0:
                                Bundle etoday = new Bundle();
                                etoday.putSerializable("UserObj", user);
                                fragment = new EventsTodayFragment();
                                fragment.setArguments(etoday);
                                break;
                            case 1:
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("UserObj", user);
                                // Set Fragment class Arguments
                                fragment = new MyHistoryFragment();
                                fragment.setArguments(bundle);
                                break;
                        }
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.simpleFrameLayout, fragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                    }
                });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    // On resume method to be called when program returns to main activity
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        manageUser.get(currentUser.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                currentUser = user;
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserObj", currentUser);
                Fragment startFragment = new EventsTodayFragment();
                startFragment.setArguments(bundle);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, startFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUser() {
        manageUser.get(currentUser.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                currentUser = user;
            }
        });
    }


}