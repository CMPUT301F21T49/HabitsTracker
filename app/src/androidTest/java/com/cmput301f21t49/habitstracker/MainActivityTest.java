package com.cmput301f21t49.habitstracker;

import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.navigation.NavigationView;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp()throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the activity
     * @throws Exception
     */
    @Test
    public void start()throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * This tests clicking the menu bar and navigating to the Habits Activity and then back and sign out
     */
    @Test
    public void ClickMyHabitsTest(){
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "purvi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "1234567");

        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", MyHabitsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Sign Out");
    }
    /**
     * This tests clicking the menu bar and navigating to the Following/Followers Activity and then back and sign out
     */
    @Test
    public void ClickMyFolllowersTest(){
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "purvi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "1234567");

        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Following");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", UserActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Sign Out");

    }
}
