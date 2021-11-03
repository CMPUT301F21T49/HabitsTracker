package com.cmput301f21t49.habitstracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFollowersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFollowersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FollowerAdapter adapter;

    // TODO: Rename and change types of parameters
    private ArrayList<User> mParam1;
    private String mParam2;

    private ListView listView;

    public MyFollowersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyFollowersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFollowersFragment newInstance(ArrayList<User> userList) {
        MyFollowersFragment fragment = new MyFollowersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, userList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (ArrayList<User>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_followers, container, false);
        listView = v.findViewById(R.id.followers_list);
        ArrayList<User> tempList = new ArrayList<>();
        tempList.add(new User("User 1"));
        tempList.add(new User("User 2"));
        adapter = new FollowerAdapter(getContext(), tempList);
        adapter.setButtonToggle(true);
        listView.setAdapter(adapter);
        return v;
    }
}