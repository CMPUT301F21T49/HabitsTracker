package com.cmput301f21t49.habitstracker;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/*
 * ManageUser
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
 * A singleton class responsible for Managing the database. We can load, update, create user documents
 * here and callBack methods ensure we can pass objects received from the firebase to calling methods
 * @author Purvi S.
 * @version 1.0
 * @see MainActivity
 * @see LoginActivity
 * @see SignUpActivity
 * @since 1.0
 */

public class ManageUser {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final ManageUser instance = new ManageUser();
    private final String COLLECTION_NAME = "Users";
    private static final String TAG = "Sample";


    private ManageUser() {
    }

    /**
     * This returns a instance of the ManageUser. This instance should be called on to
     * retrieve items and objects
     * @return Return a instance of user dao
     */
    public static ManageUser getInstance() {

        return instance;
    }

    public void createOrUpdate(User user, final VoidCallback onSuccess) {

        db.collection(COLLECTION_NAME).document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        System.out.println(user.getHabits().size());
                        onSuccess.onCallback();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * This queries for a user by the user's Id. A callback method will be called on success.
     * This callback contains a user that can be worked with in the caller class/activity
     *
     * @param userId  This is the Id of user that we want to get data from
     * @param onSuccess This is the function invoked when the user's data is obtained successfully
     */
    public void get(String userId, final UserCallback onSuccess) {
        //Generally speaking, do not pass null values in. This is an exception.
        get(userId, onSuccess, null);
    }

    /**
     * This returns a user object when given a userId of the user. Throws an error if no user is found.
     * Performs a user callback if the user is found.
     *
     * @param userId  This is the name of user that we want to get data from
     * @param onSuccess This is the function invoked when the user's data is obtained successfully
     * @param onFail    This is the function that is called when an error occurs in firestore
     */
    public void get(String userId, final UserCallback onSuccess, final VoidCallback onFail) {
        db.collection(COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc == null || !doc.exists()) {
                                Log.d(TAG, "No documents found");
                                if (onFail != null) {
                                    onFail.onCallback();
                                }
                                return;
                            }

                            // Assumes only one document will be returned
                            User current = doc.toObject(User.class);
                            System.out.println(current.getId());
                            if(current.getHabits() != null){
                                System.out.println(current.getHabits().size());
                            }
                            onSuccess.onCallback(current);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            if (onFail != null) {
                                onFail.onCallback();
                            }
                        }
                    }
                });
    }




}



