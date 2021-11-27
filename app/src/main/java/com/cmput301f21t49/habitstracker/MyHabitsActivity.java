package com.cmput301f21t49.habitstracker;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 * MyHabitsActivity
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
 * This is the My Habits activity responsible for displaying habits, handling reordering, deletion, editing, addition
 * @author Purvi S.
 * @version 1.0
 * @see RecyclerAdapter
 * @see MainActivity
 * @since 1.0
 */

public class MyHabitsActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener,RecyclerAdapter.ListItemClickListener{

    public User currentUser;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<String> habitNameList = new ArrayList<>();
    ArrayList<Habit> habitArrayList = new ArrayList<>();
    ManageUser manageUser = ManageUser.getInstance();
    public FirebaseAuth fAuth;
    Habit newHabit = new Habit();
    Habit editH = new Habit();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhabits);


        currentUser = (User) getIntent().getSerializableExtra(User.SERIALIZED);
        //updateUser();
        if (currentUser != null && currentUser.getHabits() != null && currentUser.getHabits().size() > 0){
            System.out.println("Retrieve Habits");
            System.out.println(currentUser.getHabits().size());
            for (Habit h : habitArrayList = currentUser.getHabits()) {
                habitNameList.add(h.getName());
            }

        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(habitNameList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Button to add a new habit
        final FloatingActionButton add_button = findViewById(R.id.floatingActionButton2);
        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //call the Fragment
                newHabit = null; //make the med object that is clicked null
                new AddHabitFragment().show(getSupportFragmentManager(),"ADD_Habit");

            }
        });


    }
    /** To add a new medicine in the list
     * @param  newHabit
     * */

    @Override
    public void onNewPressed(Habit newHabit){

        habitNameList.add(newHabit.getName());
        currentUser.addHabit(newHabit);
        updateDatabase();
        recyclerAdapter.notifyDataSetChanged();

    }
    public void onEditPressed(Habit editHabit, int i){
        /* To add an edited habit in the list
         * Inputs: Habit object, position in the list
         * Return: None
         * */

        habitNameList.set(i,editHabit.getName());
        for(int j = 0; j < currentUser.getHabits().size(); j ++){
            if (currentUser.getHabits().get(j).getName().equals(editHabit.getName())){
                currentUser.editHabit(j,editHabit);
            }
        }
        updateDatabase();
        recyclerAdapter.notifyDataSetChanged();
    }

    /**
     * ItemTouch helper method with omMoved and onSwiped methods that handles these operations
     * @param UP ItemTouchHelper
     * @param DOWN ItemTouchHelper
     * @param LEFT ItemTouchHelper
     */
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN| ItemTouchHelper.LEFT | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getBindingAdapterPosition();
            int toPosition = target.getBindingAdapterPosition();

            Collections.swap(habitNameList,fromPosition,toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            habitNameList.remove(position);
            if (currentUser != null && currentUser.getHabits() != null && currentUser.getHabits().size() > 0){
                System.out.println(currentUser.getHabits().get(position).getName());
                currentUser.getHabits().get(position).deleteEvents(); //delete all events associated with this habit
                currentUser.deleteHabit(position); //delete the habit
                updateDatabase();

            }
            recyclerView.getAdapter().notifyItemRemoved(position);


        }
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    };

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

    public void updateDatabase() {
        manageUser.createOrUpdate(currentUser, new VoidCallback() {
            @Override
            public void onCallback() {

            }
        });
    }

    public void updateUser() {
        manageUser.get(currentUser.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                currentUser = user;
            }
        });
    }

    @Override
    public void onListItemClick(int position) {
        String hName = habitNameList.get(position);

        for(Habit h: currentUser.getHabits()){
            if (h.getName().equals(hName)){
                editH = h;
            }
        }
        System.out.println(editH.getName());
        if(editH!=null) {
            new AddHabitFragment().newInstance(editH, position).show(getSupportFragmentManager(), "EDIT_HABIT");
            editH = null;
        }

    }
}