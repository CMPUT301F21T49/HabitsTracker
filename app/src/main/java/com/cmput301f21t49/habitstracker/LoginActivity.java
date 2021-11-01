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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

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
                //ensure not null
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                manageUser.get(fAuth.getCurrentUser().getUid(), new UserCallback() {
                                    @Override
                                    public void onCallback(User user) {
                                        System.out.println(user.getId());
                                        //Have access to the current user's object here
                                        //Can pass this through activitities
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Authentication", e);
                                if (e instanceof FirebaseAuthInvalidUserException) {
                                    emailText.setError("No user exists");
                                    passwordText.setError("No user exists");
                                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    emailText.setError("Invalid Email Formatting");
                                }
                                Snackbar snackbar = Snackbar
                                        .make(findViewById(R.id.loginActivity), "Authentication Failed", Snackbar.LENGTH_SHORT);
                                snackbar.show();
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
    }

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
                    System.out.println(user.getId());
                    //Have access to the current user's object here
                    //Can pass this through activitities

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            });

        }
    }


}