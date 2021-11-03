package com.cmput301f21t49.habitstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * Activity responsible for showing user information in addition to their followers/following.
 */
public class UserActivity extends AppCompatActivity {
    private ImageView userImageView;
    private TextView usernameTextView;
    private TextView uidTextView;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private EditText editFollowName;
    private Button sendButton;

    private ArrayList<User> tempList = new ArrayList<User>();
    private User currentUser;

    private MyFollowersFragment followersFragment = MyFollowersFragment.newInstance(tempList);
    private MyFollowingFragment followingFragment = MyFollowingFragment.newInstance(tempList);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        currentUser = (User) getIntent().getSerializableExtra("CurrentUserObj");

        userImageView = findViewById(R.id.userImageView);
        usernameTextView = findViewById(R.id.username);
        uidTextView = findViewById(R.id.userid);
        tabLayout = findViewById(R.id.tabs);
        frameLayout = findViewById(R.id.frame);
        editFollowName = findViewById(R.id.editTextFollowName);
        sendButton = findViewById(R.id.sendButton);

        //TODO: Set username and uid
        usernameTextView.setText(currentUser.getId());
        uidTextView.setText("@special_uid_here");

        TabLayout.Tab followingTab = tabLayout.newTab();
        TabLayout.Tab followersTab = tabLayout.newTab();

        followingTab.setText("Following");
        followersTab.setText("Followers");
        tabLayout.addTab(followingTab);
        tabLayout.addTab(followersTab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = followingFragment;
                        break;
                    case 1:
                        fragment = followersFragment;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String followName = editFollowName.getText().toString();
                Toast.makeText(getApplicationContext(), followName, Toast.LENGTH_SHORT).show();
                editFollowName.setText("");
            }
        });

        //Set initial fragment to MyFollowingFragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame, followingFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

    }
}