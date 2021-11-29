package com.cmput301f21t49.habitstracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
 * FollowerAdapter
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
 * Follower Adapter, extends {@link ArrayAdapter} for Users
 */
public class FollowerAdapter extends ArrayAdapter<String> {

    /**
     * Interface for clicking a list item button
     */
    interface ItemButtonOnClickListener {
        /**
         * Callback when button is clicked
         * @param mainUser Currently logged in user
         * @param user User in the list
         */
        void onClick(User mainUser, User user);
    }

    /**
     * Interface for clicking a list item
     */
    interface ItemOnClickListener {
        /**
         * Callback when list item is clicked
         * @param position Position in list
         * @param userEmailList List of user emails
         */
        void onClick(int position, ArrayList<String> userEmailList);
    }

    private String currentUserEmail;
    private ArrayList<String> userEmailList;
    private ManageUser manageUser = ManageUser.getInstance();
    private boolean showButton1 = false;
    private boolean showButton2 = false;
    private String buttonText1 = null;
    private String buttonText2 = null;
    private ItemButtonOnClickListener listener1 = null;
    private ItemButtonOnClickListener listener2 = null;
    private ItemOnClickListener itemClickListener = null;
    private int button1Color = 0;
    private int button2Color = 0;

    /**
     * Follower Adapter Constructor
     * @param context Context of the adapter
     */
    public FollowerAdapter(Context context, String currentUserEmail, ArrayList<String> userEmailList) {
        super(context, 0, userEmailList);
        this.currentUserEmail = currentUserEmail;
        this.userEmailList = userEmailList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String listUserEmail = userEmailList.get(position);
        View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
            }

            //Get UI
            TextView userName = v.findViewById(R.id.textViewUsername);
            Button button1 = v.findViewById(R.id.button1);
            Button button2 = v.findViewById(R.id.button2);

            //Set user email
            userName.setText(listUserEmail);

            //Set button properties

            if (!showButton1) {
                button1.setClickable(false);
                button1.setVisibility(View.INVISIBLE);
            }

            if (!showButton2) {
                button2.setClickable(false);
                button2.setVisibility(View.INVISIBLE);
            }

            if (buttonText1 != null) {
                button1.setText(buttonText1);
            }

            if (buttonText2 != null) {
                button2.setText(buttonText2);
            }

            if (button1Color != 0) {
                button1.setBackgroundColor(button1Color);
            }

            if(button2Color != 0) {
                button2.setBackgroundColor(button2Color);
            }

            if (listener1 != null) {
                //call listener and update users
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manageUser.getByEmail(currentUserEmail, new UserCallback() {
                            @Override
                            public void onCallback(User user) {
                                User currentUser = user;
                                manageUser.getByEmail(listUserEmail, new UserCallback() {
                                    @Override
                                    public void onCallback(User user) {
                                        User listUser = user;
                                        listener1.onClick(currentUser, listUser);

                                        manageUser.createOrUpdate(currentUser, new VoidCallback() {
                                            @Override
                                            public void onCallback() {
                                                System.out.println("UPDATED MAIN USER");
                                            }
                                        });
                                        manageUser.createOrUpdate(listUser, new VoidCallback() {
                                            @Override
                                            public void onCallback() {
                                                System.out.println("UPDATED LIST USER");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }

            if (listener2 != null) {
                //call listener and update users
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manageUser.getByEmail(currentUserEmail, new UserCallback() {
                            @Override
                            public void onCallback(User user) {
                                final User currentUser = user;
                                manageUser.getByEmail(listUserEmail, new UserCallback() {
                                    @Override
                                    public void onCallback(User user) {
                                        User listUser = user;
                                        listener2.onClick(currentUser, listUser);
                                        manageUser.createOrUpdate(currentUser, new VoidCallback() {
                                            @Override
                                            public void onCallback() {
                                                Log.i("FIREBASE UPDATE", "UPDATED MAIN USER");
                                            }
                                        });
                                        manageUser.createOrUpdate(listUser, new VoidCallback() {
                                            @Override
                                            public void onCallback() {
                                                Log.i("FIREBASE UPDATE", "UPDATED LIST USER");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }

            if (itemClickListener != null) {
                //call listener on item click
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onClick(position, userEmailList);
                    }
                });
            }

        return v;
    }

    /**
     * Toggle buttons 1 and 2, invisible by default
     */
    public void toggleButtons() {
        showButton1 = !showButton1;
        showButton2 = !showButton2;
    }

    /**
     * Toggle Button 1, invisible by default
     */
    public void toggleButton1() {
        showButton1 = !showButton1;
    }

    /**
     * Toggle Button 2, invisible by default
     */
    public void toggleButton2() {
        showButton2 = !showButton2;
    }

    /**
     * Set text for Button 1, "Accept" by default
     * @param text Text for Button 1
     */
    public void setButton1Text(String text) {
        buttonText1 = text;
    }

    /**
     * Set text for Button 2 "Decline" by default
     * @param text Text for Button 2
     */
    public void setButton2Text(String text) {
        buttonText2 = text;
    }

    /**
     * Set color for Button 1, Blue by default
     * @param color Color for Button 1
     */
    public void setButton1Color(int color) {
        button1Color = color;
    }

    /**
     * Set color for Button 2, Blue by default
     * @param color Color for Button 2
     */
    public void setButton2Color(int color) {
        button2Color = color;
    }

    /**
     * Set {@see ItemButtonOnClickListener} for Button 1
     * @param listener Listener for Button 1
     */
    public void setButton1OnClickListener(ItemButtonOnClickListener listener) {
        listener1 = listener;
    }

    /**
     * Set {@see ItemButtonOnClickListener} for Button 2
     * @param listener Listener for Button 2
     */
    public void setButton2OnClickListener(ItemButtonOnClickListener listener) {
        listener2 = listener;
    }

    /**
     * Set {@see ItemOnClickListener} for list item
     * @param itemClickListener Listener for list item
     */
    public void setOnItemClickListener(ItemOnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
