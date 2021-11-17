package com.cmput301f21t49.habitstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

/*
 * LoginActivity
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
 * Activity responsible for logging in. Links to MainActivity and SignUpActivity. If a user is already
 * signed in, user will automatically be sent to the MainActivity.
 * @see SignUpActivity
 * @see MainActivity
 */

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Button loginButton;
    private EditText emailText;
    private EditText passwordText;
    private TextView signUpText;
    private ManageUser manageUser = ManageUser.getInstance();
    private User currentUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.editTextEmailAddress);
        passwordText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        signUpText = findViewById(R.id.textViewSignUp);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                //preprocess email and password
                if (email.equals("")) {
                    emailText.setError("Email is Empty");
                    return;
                }
                if (password.equals("")) {
                    passwordText.setError("Password is Empty");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                manageUser.get(fAuth.getCurrentUser().getUid(), new UserCallback() {
                                    @Override
                                        public void onCallback(User user) {
                                            System.out.println(user.getId());
                                            //Have access to the current user's object here
                                            //Can pass this through activities
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra(User.SERIALIZED, user);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Authentication", e.toString());
                                    String error = "Authentication Failed";
                                    if (e instanceof FirebaseAuthInvalidUserException) {
                                        emailText.setError("No user exists");
                                        passwordText.setError("No user exists");
                                        error = e.getMessage();
                                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        emailText.setError("Invalid Credentials");
                                        passwordText.setError("Invalid Credentials");
                                        error = e.getMessage();
                                    } else if (e instanceof FirebaseTooManyRequestsException) {
                                        error = "Too many requests have been made. Try again later.";
                                    }
                                    showFailedAuthentication(error);
                                }
                            });
                }
            });

            //On click "SIGN UP", start sign up activity
            signUpText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            });
        };

    @Override
    protected void onStart() {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = fAuth.getCurrentUser();
        // If user is logged in, send to MainActivity
        if (currentUser != null) {
            manageUser.get(fAuth.getCurrentUser().getUid(), new UserCallback() {
                @Override
                public void onCallback(User user) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(User.SERIALIZED, user);
                    startActivity(intent);
                }
            });

        }
    }

    /**
     * Shows a Snackbar with a message
     * @param message message to display
     */
    private void showFailedAuthentication(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.loginActivity), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}