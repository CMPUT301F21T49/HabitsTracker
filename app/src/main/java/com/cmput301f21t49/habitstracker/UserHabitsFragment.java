package com.cmput301f21t49.habitstracker;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHabitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHabitsFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String userEmail;
    private String currentUserEmail;
    private User user;
    private ManageUser manageUser = ManageUser.getInstance();

    public UserHabitsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment UserHabitsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHabitsFragment newInstance(String currentUserEmail, String userEmail) {
        UserHabitsFragment fragment = new UserHabitsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userEmail);
        args.putString(ARG_PARAM2, currentUserEmail);
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            userEmail = getArguments().getString(ARG_PARAM1);
            currentUserEmail = getArguments().getString(ARG_PARAM2);
        }

        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.fragment_user_habits);

        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) d.findViewById(R.id.habits_list);
        TextView textViewEmail = (TextView) d.findViewById(R.id.user_email);
        Button button = (Button) d.findViewById(R.id.button);

        textViewEmail.setText(userEmail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        manageUser.getByEmail(userEmail, new UserCallback() {
            @Override
            public void onCallback(User user) {
                ArrayList<Habit> publicHabits = new ArrayList<>();
                System.out.println(user.getAllowPrivate());
                System.out.println(currentUserEmail);
                for (Habit h : user.getHabits()) {
                    if (!h.getPrivateHabit() || user.getAllowPrivate().contains(currentUserEmail)) {
                        publicHabits.add(h);
                    }
                }
                UserHabitsAdapter adapter = new UserHabitsAdapter(getContext(), publicHabits);
                listView.setAdapter(adapter);
            }
        });
        return d;
    }
}