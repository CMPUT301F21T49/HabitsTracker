package com.cmput301f21t49.habitstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;

/*
 * FollowerAdapter
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
public class FollowerAdapter extends ArrayAdapter<String> {
    public static final int BUTTON = 1;
    public static final int SWITCH = 2;
    interface ItemButtonOnClickListener {
        void onClick(User mainUser, User user);
    }

    interface ItemOnClickListener {
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

    private int type = 0;

    /**
     * Follower Adapter Constructor
     * @param context Context of the adapter
     */
    public FollowerAdapter(Context context, String currentUserEmail, ArrayList<String> userEmailList, int type) {
        super(context, 0, userEmailList);
        this.currentUserEmail = currentUserEmail;
        this.userEmailList = userEmailList;
        this.type = type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //super.getView(position, convertView, parent);
        String listUserEmail = userEmailList.get(position);
        View v = convertView;
        if (type == BUTTON) {
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
            }

            TextView userName = v.findViewById(R.id.textViewUsername);
            Button button1 = v.findViewById(R.id.button1);
            Button button2 = v.findViewById(R.id.button2);

            userName.setText(listUserEmail);

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
                                        System.out.println(currentUser.getFollowers());
                                        System.out.println(currentUser.getFollowing());
                                        System.out.println(currentUser.getRequests());

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
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manageUser.getByEmail(currentUserEmail, new UserCallback() {
                            @Override
                            public void onCallback(User user) {
                                final User currentUser = user;
                                System.out.println(user);
                                manageUser.getByEmail(listUserEmail, new UserCallback() {
                                    @Override
                                    public void onCallback(User user) {
                                        User listUser = user;
//                                    System.out.println(currentUser);
                                        listener2.onClick(currentUser, listUser);
//                                    System.out.println(currentUser);
//                                    System.out.println(currentUser.getFollowers());
//                                    System.out.println(currentUser.getFollowing());
//                                    System.out.println(currentUser.getRequests());
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

            if (itemClickListener != null) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onClick(position, userEmailList);
                    }
                });
            }

        } else if (type == SWITCH) {
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.user_item_switch, parent, false);
            }
            TextView userName = v.findViewById(R.id.textViewUsername);
            SwitchCompat switchCompat = v.findViewById(R.id.privateSwitch);

            userName.setText(listUserEmail);
            manageUser.getByEmail(currentUserEmail, new UserCallback() {
                @Override
                public void onCallback(User user) {
                    switchCompat.setChecked(user.getAllowPrivate().contains(listUserEmail));
                    switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                user.addAllowPrivate(listUserEmail);
                            } else  {
                                user.removeAllowPrivate(listUserEmail);
                            }
                            manageUser.createOrUpdate(user, new VoidCallback() {
                                @Override
                                public void onCallback() {

                                }
                            });
                        }
                    });
                }
            });





        }


        return v;
    }

    public void toggleButtons() {
        showButton1 = !showButton1;
        showButton2 = !showButton2;
    }

    public void toggleButton1() {
        showButton1 = !showButton1;
    }

    public void toggleButton2() {
        showButton2 = !showButton2;
    }

    public void setButton1Text(String text) {
        buttonText1 = text;
    }

    public void setButton2Text(String text) {
        buttonText2 = text;
    }

    public void setButton1Color(int color) {
        button1Color = color;
    }

    public void setButton2Color(int color) {
        button2Color = color;
    }

    public void setButton1OnClickListener(ItemButtonOnClickListener listener) {
        listener1 = listener;
    }

    public void setButton2OnClickListener(ItemButtonOnClickListener listener) {
        listener2 = listener;
    }

    public void setOnItemClickListener(ItemOnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
