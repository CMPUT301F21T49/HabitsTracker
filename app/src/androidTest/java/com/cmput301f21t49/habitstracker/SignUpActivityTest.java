package com.cmput301f21t49.habitstracker;

import static org.junit.Assert.*;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test SignUpActivity
 * Test Cases: UC15
 */
public class SignUpActivityTest {

    private Solo solo;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Rule
    public ActivityTestRule<SignUpActivity> rule =
            new ActivityTestRule<>(SignUpActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    /**
     * Follows User Case 15 Normal Case
     */
    @Test
    public void validSignUp() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "delete@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "123456");

        solo.clickOnButton("SIGN UP");
        solo.waitForActivity(MainActivity.class, 2000);
        //Ensure firebase acknowledges login
        assertNotNull(fAuth.getCurrentUser());
        //Have to logout, firebase will remain logged in even when test ends
        FirebaseAuth.getInstance().getCurrentUser().delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        assertFalse(true);//Fail Test case
                        fAuth.signOut();
                    }
                });
    }

    /**
     * Follows User Case 15 Exception 1.0.E.0
     * User already exists, email in use.
     */
    @Test
    public void userConflict() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "123456");

        solo.clickOnButton("SIGN UP");

        //Ensure activity has not changed
        solo.waitForActivity(SignUpActivity.class, 2000);
    }

    /**
     * Follows User Case 15 Exception 1.0.E.0
     * Invalid email formatting
     */
    @Test
    public void invalidEmail() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "123456");

        solo.clickOnButton("SIGN UP");

        //Ensure activity has not changed
        solo.waitForActivity(SignUpActivity.class, 2000);
    }

    /**
     * Follows User Case 15 Exception 1.0.E.0
     * No Email Input
     */
    @Test
    public void emptyEmail() {
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "123456");

        solo.clickOnButton("SIGN UP");

        //Ensure activity has not changed
        solo.waitForActivity(SignUpActivity.class, 2000);
    }

    /**
     * Follows User Case 15 Exception 1.0.E.0
     * Invalid Password formatting for firebase
     */
    @Test
    public void invalidPassword() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "777"); //Firebase requires 6 or more characters
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "777");

        solo.clickOnButton("SIGN UP");

        //Ensure activity has not changed
        solo.waitForActivity(SignUpActivity.class, 2000);
    }

    /**
     * Follows User Case 15 Exception 1.0.E.0
     * No Password Input
     */
    @Test
    public void emptyPassword() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "123456");

        solo.clickOnButton("SIGN UP");

        //Ensure activity has not changed
        solo.waitForActivity(SignUpActivity.class, 2000);
    }

    /**
     * Follows User Case 15 Exception 1.0.E.0
     * Passwords do not match
     */
    @Test
    public void invalidMatchingPassword() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "654321");

        solo.clickOnButton("SIGN UP");

        //Ensure activity has not changed
        solo.waitForActivity(SignUpActivity.class, 2000);
    }

    /**
     * Follows User Case 15 Exception 1.0.E.0
     * No Matching Password Input
     */
    @Test
    public void emptyMatchingPassword() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");

        solo.clickOnButton("SIGN UP");

        //Ensure activity has not changed
        solo.waitForActivity(SignUpActivity.class, 2000);
    }

    /**
     * Ensure user is able to go back to LoginActivity
     */
    @Test
    public void backToLoginActivity() {
        solo.clickOnActionBarHomeButton();

        solo.waitForActivity(LoginActivity.class, 2000);
    }
}
