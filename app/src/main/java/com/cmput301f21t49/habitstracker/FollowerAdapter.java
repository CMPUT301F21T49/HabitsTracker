package com.cmput301f21t49.habitstracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FollowerAdapter extends ArrayAdapter<User> {

    private ArrayList<User> userList;
    private boolean buttonToggle = false;

    public FollowerAdapter(Context context, ArrayList<User> userList) {
        super(context, 0, userList);
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //super.getView(position, convertView, parent);
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }
        //TODO: GET USER INFORMATION
        //userList.get()

        TextView userName = v.findViewById(R.id.textViewUsername);
        Button acceptButton = v.findViewById(R.id.button_accept);

        userName.setText("USER NAME HERE");

        if (!buttonToggle) {
            acceptButton.setClickable(false);
            acceptButton.setVisibility(View.INVISIBLE);
        }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    public void setButtonToggle(boolean b) {
        buttonToggle = b;
    }
}
