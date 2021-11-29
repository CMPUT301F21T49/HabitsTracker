package com.cmput301f21t49.habitstracker;

import static org.junit.Assert.*;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test MainActivity
 * Test Cases: UC5, UC6, UC7, UC8, UC13
 * Latches will wait 2s max.
 * Ensure no-one is logged in to the AVD while testing.\
 * If you are running into any issues with running the whole test. Run each test case individually.
 */
public class MainActivityTest {
    private Solo solo;
    private ManageUser manageUser = ManageUser.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private User user1;
    private ArrayList<String> days = new ArrayList<>(Arrays.asList("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"));

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before each test. Creates solo instance, gets to MainActivity, and gets user data.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //Clear History and Start new Activity. Prevents bugs with thread blocking.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        rule.launchActivity(intent);

        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test1@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");

        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        //Fetch user data synchronously
        final CountDownLatch latchRetrieve = new CountDownLatch(1);
        manageUser.getByEmail("test1@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                user1 = user;
                latchRetrieve.countDown();
            }
        });
        latchRetrieve.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Runs after each test, clear user's events and habits. Sign out.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        //Clear data for habits and events
        for (int i = 0; i < user1.getHabits().size(); i++) {
            Habit h = user1.getHabits().get(i);
            for (int j = 0; j < h.getEvents().size(); j++) {
                h.deleteEvent(j);
            }
            user1.deleteHabit(i);
        }
        final CountDownLatch latchRetrieve = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latchRetrieve.countDown();
            }
        });
        latchRetrieve.await(2000, TimeUnit.MILLISECONDS);
        solo.finishOpenedActivities();
        fAuth.signOut();
    }

    /**
     * This tests clicking the menu bar and navigating to the Habits Activity and then back and sign out
     */
    @Test
    public void ClickMyHabitsTest(){
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
        solo.clickOnImageButton(0);
        solo.clickOnText("My Following");
        solo.sleep(1000);
        solo.assertCurrentActivity("Wrong Activity", UserActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImageButton(0);
        solo.clickOnText("Sign Out");

    }

    /**
     * Follows User Case 6.
     * If an event exists, ensure user is able to view it
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void habitEventTest() throws InterruptedException {
        //Add request to user1
        user1.addHabit(new Habit("TESTNAME", false, new Date(), days, ""));
        user1.addEvent(0, new Event("TESTEVENT", null));
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        solo.clickOnText("My History");
        solo.clickOnText("Events Today");
        solo.waitForText("TESTEVENT");
    }

    /**
     * Follows User Case 5.
     * User is able to create event
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void addHabitEvent() throws InterruptedException {
        //Add request to user1
        user1.addHabit(new Habit("TESTNAME", false, new Date(), days, ""));
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch user1Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });

        solo.clickOnText("My History");
        solo.clickOnText("Events Today");
        solo.clickOnText("Get Events");
        solo.waitForText("TESTNAME");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch fetchLatch = new CountDownLatch(1);
        manageUser.get(user1.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(user.getHabits().get(0).getEvents().get(0).getName().equals("New Event"));
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
    }

    /**
     * Follows User Case 8.
     * User is able to delete an event.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void deleteHabitEvent() throws InterruptedException {
        //Add request to user1
        user1.addHabit(new Habit("TESTNAME", false, new Date(), days, ""));
        Event event = new Event("TESTEVENT", new Date());
        event.setStatus(true);

        user1.addEvent(0, event);
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch user1Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });

        solo.clickOnText("My History");
        solo.clickOnText("TESTEVENT");
        solo.clickOnText("Delete");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch fetchLatch = new CountDownLatch(1);
        manageUser.get(user1.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertFalse(user.getHabits().get(0).getEvents().contains(event));
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
    }

    /**
     * Follows User Case 7.
     * User is able to edit a habit event
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void editHabitEvent() throws InterruptedException {
        //Add request to user1
        user1.addHabit(new Habit("TESTNAME", false, new Date(), days, ""));
        user1.addEvent(0, new Event("TESTEVENT", null));
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch user1Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });

        solo.clickOnText("My History");
        solo.clickOnText("Events Today");
        solo.clickOnText("TESTEVENT");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), "COMMENT HERE");
        solo.clickOnText("Update");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch fetchLatch = new CountDownLatch(1);
        manageUser.get(user1.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(user.getHabits().get(0).getEvents().get(0).getComment().equals("COMMENT HERE"));
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
    }

    /**
     * Follower User Case 13
     * User is able to complete an event
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void completeHabitEvent() throws InterruptedException {
        //Add request to user1
        user1.addHabit(new Habit("TESTNAME", false, new Date(), days, ""));
        user1.addEvent(0, new Event("TESTEVENT", null));
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch user1Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });

        solo.clickOnText("My History");
        solo.clickOnText("Events Today");
        solo.clickOnText("TESTEVENT");
        solo.clickOnButton("Complete");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch fetchLatch = new CountDownLatch(1);
        manageUser.get(user1.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(user.getHabits().get(0).getEvents().get(0).getStatus());
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
        solo.clickOnText("My History");
        solo.waitForText("TESTEVENT");
    }

    /**
     * Follows User Case 7.
     * User is able to update a completed event
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void updateCompleteHabitEvent() throws InterruptedException {
        //Add request to user1
        user1.addHabit(new Habit("TESTNAME", false, new Date(), days, ""));
        Event event = new Event("TESTEVENT", new Date());
        event.setStatus(true);

        user1.addEvent(0, event);
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch user1Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });

        solo.clickOnText("My History");
        solo.clickOnText("TESTEVENT");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), "COMMENT HERE");
        solo.clickOnText("Update");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);

        CountDownLatch fetchLatch = new CountDownLatch(1);
        manageUser.get(user1.getId(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(user.getHabits().get(0).getEvents().get(0).getComment().equals("COMMENT HERE"));
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
    }
}
