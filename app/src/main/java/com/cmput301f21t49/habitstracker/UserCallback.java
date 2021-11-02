package com.cmput301f21t49.habitstracker;
import com.cmput301f21t49.habitstracker.User;

/**
 * This interface contains a general callback method for ManageUsers, one that provides an input user for other objects to use.
 * Source from https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method
 */
public interface UserCallback {
    void onCallback(User user);
}

