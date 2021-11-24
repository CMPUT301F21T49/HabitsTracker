package com.cmput301f21t49.habitstracker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRequestsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private User user;

    private FollowerAdapter adapter;
    private ListView listView;

    public MyRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRequestsFragment newInstance(User param1) {
        MyRequestsFragment fragment = new MyRequestsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_requests, container, false);
        listView = v.findViewById(R.id.requests_list);
        adapter = new FollowerAdapter(getContext(), user.getEmail(), user.getRequests(), FollowerAdapter.BUTTON);
        adapter.toggleButtons();
        adapter.setButton1Text("Accept");
        adapter.setButton1Color(Color.BLUE);
        adapter.setButton1OnClickListener(new FollowerAdapter.ItemButtonOnClickListener() {
            @Override
            public void onClick(User mainUser, User user) {
                mainUser.removeRequest(user.getEmail());
                mainUser.addFollower(user.getEmail());
                user.addFollowing(mainUser.getEmail());
//                mainUser.addFollower("T");
//                mainUser.addFollowing("T");
//                mainUser.addRequest("T");
                adapter.remove(user.getEmail());
            }
        });

        adapter.setButton2Text("Decline");
        adapter.setButton2Color(Color.RED);
        adapter.setButton2OnClickListener(new FollowerAdapter.ItemButtonOnClickListener() {
            @Override
            public void onClick(User mainUser, User user) {
//                user.addFollower("TEST");
//                user.addRequest("TEST");
//                user.addFollowing("TEST");
                mainUser.removeRequest(user.getEmail());
                adapter.remove(user.getEmail());
            }
        });

        listView.setAdapter(adapter);
        return v;
    }
}