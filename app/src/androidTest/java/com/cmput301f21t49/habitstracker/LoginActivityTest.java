package com.cmput301f21t49.habitstracker;

import static org.junit.Assert.*;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test LoginActivity
 * Test Cases: UC14
 */
public class LoginActivityTest {

    private Solo solo;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    //Note: "Login" is used as R.id.loginButton method failed.

    //TODO: Change credentials to environment variables

    /**
     * Follows User Case 14 Normal Case
     */
    @Test
    public void validLogin() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");

        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        //Ensure firebase acknowledges login
        assertNotNull(fAuth.getCurrentUser());
        //Have to logout, firebase will remain logged in even when test ends
        fAuth.signOut();
    }

    /**
     * Follows User Case 14 Exception 1.0.E.0
     * Invalid Email Formatting
     */
    @Test
    public void invalidEmail() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");

        solo.clickOnButton("Login");

        //Ensure activity has not changed
        solo.waitForActivity(LoginActivity.class, 2000);
    }

    /**
     * Follows User Case 14 Exception 1.0.E.0
     * No Email Inputted
     */
    @Test
    public void emptyEmail() {
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");

        solo.clickOnButton("Login");

        //Ensure activity has not changed
        solo.waitForActivity(LoginActivity.class, 2000);
    }

    /**
     * Follows User Case 14 Exception 1.0.E.0
     * Invalid password for a user that exists
     */
    @Test
    public void invalidPassword() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "77777777");

        solo.clickOnButton("Login");

        //Ensure activity has not changed
        solo.waitForActivity(LoginActivity.class, 2000);
    }

    /**
     * Follows User Case 14 Exception 1.0.E.0
     * No Password Input
     */
    @Test
    public void emptyPassword() {
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test@admin.com");

        solo.clickOnButton("Login");

        //Ensure activity has not changed
        solo.waitForActivity(LoginActivity.class, 2000);
    }

    /**
     * Test user is able to go to SignUpActivity
     */
    @Test
    public void gotoSignUpActivity() {
        solo.clickOnText("SIGN UP");

        solo.waitForActivity(SignUpActivity.class, 2000);
    }
}
