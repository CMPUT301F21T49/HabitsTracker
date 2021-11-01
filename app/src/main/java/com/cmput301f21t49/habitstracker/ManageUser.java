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
A singleton class responsible for Managing the database. We can load, update, create user documents
here and callBack methods ensure we can pass objects received from the firebase to calling methods
 */

public class ManageUser {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final ManageUser instance = new ManageUser();
    private final String COLLECTION_NAME = "Users";
    public static User user;
    public FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
     * This callback contains a user that can be worked with
     *
     * @param userId  This is the Id of user that we want to get data from
     * @param onSuccess This is the function invoked when the user's data is obtained successfully
     */
    public void get(String userId, final UserCallback onSuccess) {
        //Generally speaking, do not pass null values in. This is an exception, since we're overloading(?).
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
                            onSuccess.onCallback(doc.toObject(User.class));
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


