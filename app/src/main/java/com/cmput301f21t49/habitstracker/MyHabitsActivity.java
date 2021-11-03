package com.cmput301f21t49.habitstracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyHabitsActivity extends AppCompatActivity {

    public User currentUser;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<String> habitNameList = new ArrayList<>();
    ArrayList<Habit> habitArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhabits);

        currentUser = (User) getIntent().getSerializableExtra("CurrentUserObj");
        if (currentUser != null && currentUser.getHabits()!= null){
            for (Habit h : habitArrayList = currentUser.getHabits()) {
                habitNameList.add(h.getName());
            }

        }else{
            System.out.println(currentUser.getId()); 
            habitNameList.add("testing1");
            habitNameList.add("testing2");
            habitNameList.add("testing3");
            habitNameList.add("testing4");
            habitNameList.add("testing5");
            habitNameList.add("testing6");

        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(habitNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN| ItemTouchHelper.START | ItemTouchHelper.END, 0) {
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
            final int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Habit selectedHabit = habitArrayList.get(position);
                    Intent intent = new Intent(MyHabitsActivity.this, EditViewHabitEventActivity.class);
                    intent.putExtra("CurrentHabitObj", selectedHabit);
                    startActivity(intent);
                    break;
            }

        }
    };
}
