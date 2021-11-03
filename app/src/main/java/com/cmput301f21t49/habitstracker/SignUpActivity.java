package com.cmput301f21t49.habitstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

/**
 * Activity responsible for signup actions
 */
public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Button signUpButton;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private ManageUser manageUser = ManageUser.getInstance();
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fAuth = FirebaseAuth.getInstance();


        emailText = findViewById(R.id.editTextEmailAddress);
        passwordText = findViewById(R.id.editTextPassword);
        confirmPasswordText = findViewById(R.id.editTextConfirmPassword);
        signUpButton = findViewById(R.id.buttonSignUp);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();
                if (email.equals("")) {
                    emailText.setError("Email is Empty");
                    return;
                }
                if (password.equals("")) {
                    passwordText.setError("Password is Empty");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    confirmPasswordText.setError("Does not match password.");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                user.setId(fAuth.getUid());
                                //Create a document of the use in the database
                                manageUser.createOrUpdate(user, new VoidCallback() {
                                    @Override
                                    public void onCallback() {
                                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                        //Clear activity history
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
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
                                } else if (e instanceof FirebaseAuthUserCollisionException){
                                    emailText.setError("Email already in use.");
                                    error = "User Already Exists.";
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    error = "Too many requests have been made. Try again later.";
                                }
                                showFailedAuthentication(error);
                            }
                        });
            }
        });
    }

    /**
     * Shows a Snackbar with a message
     * @param message message to display
     */
    private void showFailedAuthentication(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.signUpActivity), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}