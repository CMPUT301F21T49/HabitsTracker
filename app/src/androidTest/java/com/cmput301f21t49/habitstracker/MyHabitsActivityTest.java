package com.cmput301f21t49.habitstracker;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MyHabitsActivityTest {

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
     * This tests clicking the add button
     */
    @Test
    public void ClickAddButtonTest(){
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "purvi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "1234567");

        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", MyHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.floatingActionButton2));
        solo.clickOnText("Cancel");
        solo.assertCurrentActivity("Wrong Activity", MyHabitsActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Sign Out");

    }

    /**
     * This tests adding a new habit
     */
    @Test
    public void AddHabitTest(){
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "purvi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "1234567");

        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", MyHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.floatingActionButton2));
        //Access First value (editfiled) and putting firstNumber value in it
        EditText FirsteditText = (EditText) solo.getView(R.id.title_edittext);
        solo.enterText(FirsteditText, String.valueOf("test"));
        solo.clickOnText("OK");
        solo.assertCurrentActivity("Wrong Activity", MyHabitsActivity.class);
        solo.searchText("test");

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Sign Out");

    }
    /**
     * This tests editing an existing habit
     */
    @Test
    public void EditHabitTest(){
        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "purvi@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "1234567");

        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Habits");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", MyHabitsActivity.class);

        solo.clickOnText("another");
        //Access First value (editfiled) and putting firstNumber value in it
        EditText Text = (EditText) solo.getView(R.id.reason_edittext);
        solo.clearEditText(Text);
        solo.enterText(Text, String.valueOf("This is a test"));
        solo.clickOnText("OK");
        solo.assertCurrentActivity("Wrong Activity", MyHabitsActivity.class);

        solo.clickOnText("another");
        solo.searchText("This is a test");
        solo.clickOnText("Cancel");

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Sign Out");

    }


}
