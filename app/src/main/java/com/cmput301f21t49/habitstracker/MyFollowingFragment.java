package com.cmput301f21t49.habitstracker;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFollowingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<User> mParam1;
    private String mParam2;

    private ListView listView;
    private ArrayAdapter<User> arrayAdapter;
    //Temporary List for prototype

    public MyFollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyFollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFollowingFragment newInstance(ArrayList<User> userList) {
        MyFollowingFragment fragment = new MyFollowingFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, userList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (ArrayList<User>)getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_following, container, false);
        listView = v.findViewById(R.id.following_list);
        ArrayList<User> tempList = new ArrayList<>();
        tempList.add(new User("User 3"));
        tempList.add(new User("User 4"));
        arrayAdapter = new FollowerAdapter(getContext(), tempList);
        listView.setAdapter(arrayAdapter);
        return v;
    }
}