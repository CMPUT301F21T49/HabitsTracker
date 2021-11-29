package com.cmput301f21t49.habitstracker;

import static org.junit.Assert.*;

import android.content.Intent;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test UserActivity
 * Test Cases: UC9, UC10, UC11
 * Latches will wait 2s max.
 * Ensure no-one is logged in to the AVD while testing.
 * If you are running into any issues with running the whole test. Run each test case individually.
 */
public class UserActivityTest {

    private Solo solo;
    private ManageUser manageUser = ManageUser.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private User user1;
    private User user2;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, false);

    /**
     * Runs before each test. Get to UserActivity and get user data.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //Clear History and Start new Activity. Prevents bugs with thread blocking.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        rule.launchActivity(intent);

        solo.enterText((EditText) solo.getView(R.id.editTextEmailAddress), "test1@admin.com");
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "123456");

        //Get to UserActivity
        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class, 2000);
        solo.clickOnImageButton(0);
        solo.clickOnText("My Following");

        //Fetch user data synchronously
        final CountDownLatch latchRetrieve = new CountDownLatch(2);
        manageUser.getByEmail("test1@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                user1 = user;
                latchRetrieve.countDown();
            }
        });
        manageUser.getByEmail("test2@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                user2 = user;
                latchRetrieve.countDown();
            }
        });
        latchRetrieve.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Runs after each test. Clear data for users and logout.
     * @throws Exception Latch is interrupted
     */
    @After
    public void tearDown() throws Exception {
        String[] tempList;
        tempList = user1.getFollowers().toArray(new String[0]);
        for (String str : tempList) {
            user1.removeFollower(str);
        }
        tempList = user1.getFollowing().toArray(new String[0]);
        for (String str : tempList) {
            user1.removeFollowing(str);
        }
        tempList = user1.getRequests().toArray(new String[0]);
        for (String str : tempList) {
            user1.removeRequest(str);
        }
        for (int i = 0; i < user1.getHabits().size(); i++) {
            user1.deleteHabit(i);
        }
        tempList = user2.getFollowers().toArray(new String[0]);
        for (String str : tempList) {
            user2.removeFollower(str);
        }
        tempList = user2.getFollowing().toArray(new String[0]);
        for (String str : tempList) {
            user2.removeFollowing(str);
        }
        tempList = user2.getRequests().toArray(new String[0]);
        for (String str : tempList) {
            user2.removeRequest(str);
        }
        for (int i = 0; i < user2.getHabits().size(); i++) {
            user2.deleteHabit(i);
        }
        final CountDownLatch latchRetrieve = new CountDownLatch(2);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latchRetrieve.countDown();
            }
        });

        manageUser.createOrUpdate(user2, new VoidCallback() {
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
     * Follows User Case 9.
     * Sends a valid follow request.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void sendRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2); //Is called twice, once upon creation, once upon update
        ListenerRegistration lr = fStore.collection("Users").document(user2.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    latch.countDown();
                }
            }
        });
        solo.enterText((EditText) solo.getView(R.id.editTextFollowName), "test2@admin.com");
        solo.clickOnView(solo.getView(R.id.sendButton));

        //Wait for update call
        latch.await(2000, TimeUnit.MILLISECONDS);
        //Ensure correct data
        CountDownLatch latch2 = new CountDownLatch(1);
        manageUser.getByEmail("test2@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(user.getRequests().contains("test1@admin.com"));
                latch2.countDown();
            }
        });
        latch2.await(2000, TimeUnit.MILLISECONDS);
        lr.remove();
    }

    /**
     * Follows User Case 9.
     * Attempts to send invalid request (oneself).
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void sendRequestInvalid() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1); //Is called twice, once upon creation, once upon update
        ListenerRegistration lr = fStore.collection("Users").document(user2.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    if (latch.getCount() <= 0) {
                        assertFalse(true); //ensure user is not updated
                    }
                    latch.countDown();
                }
            }
        });
        solo.enterText((EditText) solo.getView(R.id.editTextFollowName), "test1@admin.com"); //oneself
        solo.clickOnView(solo.getView(R.id.sendButton));

        lr.remove();
    }

    /**
     * Follows User Case 10.
     * Accepts Follow Request.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void acceptRequest() throws InterruptedException {
        //Add request to user1
        user1.addRequest("test2@admin.com");
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        //Ensure user1 and user2 are updated
        CountDownLatch user1Latch = new CountDownLatch(2);
        CountDownLatch user2Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });
        ListenerRegistration lr2 = fStore.collection("Users").document(user2.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user2Latch.countDown();
                }
            }
        });

        solo.clickOnText("Requests");
        solo.clickOnText("Accept");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);
        user2Latch.await(2000, TimeUnit.MILLISECONDS);

        //Ensure correct data
        CountDownLatch fetchLatch = new CountDownLatch(2);
        manageUser.getByEmail("test1@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(user.getFollowers().contains("test2@admin.com"));
                fetchLatch.countDown();
            }
        });

        manageUser.getByEmail("test2@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(user.getFollowing().contains("test1@admin.com"));
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
        lr2.remove();
    }

    /**
     * Follows User Case 10.
     * Declines Follow Request.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void declineRequest() throws InterruptedException {
        //Add follow request to user1
        user1.addRequest("test2@admin.com");
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        //Ensure user1 is updated
        CountDownLatch user1Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });


        solo.clickOnText("Requests");
        solo.clickOnText("Decline");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);
        //Ensure data for both user1 and user2 are correct
        CountDownLatch fetchLatch = new CountDownLatch(2);
        manageUser.getByEmail("test1@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(!user.getRequests().contains("test2@admin.com"));
                assertTrue(!user.getFollowers().contains("test2@admin.com"));
                fetchLatch.countDown();
            }
        });

        manageUser.getByEmail("test2@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(!user.getFollowing().contains("test1@admin.com"));
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
    }

    /**
     * Ability to see Follower. No User Case.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void followerTest() throws InterruptedException {
        //Add follower to user1
        user1.addFollower("test2@admin.com");
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });

        latch.await(2000, TimeUnit.MILLISECONDS);
        //Ensure follower is visible
        solo.clickOnText("Following");
        solo.clickOnText("Follower");
        solo.waitForText("test2@admin.com", 1, 2000);
    }

    /**
     * No Test Case.
     * Unfollows as user.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void unfollowTest() throws InterruptedException {
        //Update user1 and user2 to have following and follower
        user1.addFollowing("test2@admin.com");
        user2.addFollower("test1@admin.com");
        CountDownLatch latch = new CountDownLatch(2);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        manageUser.createOrUpdate(user2, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);

        //Ensure user1 and user2 are updated
        CountDownLatch user1Latch = new CountDownLatch(2);
        CountDownLatch user2Latch = new CountDownLatch(2);
        ListenerRegistration lr1 = fStore.collection("Users").document(user1.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user1Latch.countDown();
                }
            }
        });
        ListenerRegistration lr2 = fStore.collection("Users").document(user2.getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user2Latch.countDown();
                }
            }
        });

        solo.clickOnText("Following");
        solo.clickOnText("Unfollow");

        user1Latch.await(2000, TimeUnit.MILLISECONDS);
        user2Latch.await(2000, TimeUnit.MILLISECONDS);

        //Ensure correct data in user1 and user2
        CountDownLatch fetchLatch = new CountDownLatch(2);
        manageUser.getByEmail("test1@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(!user.getFollowers().contains("test2@admin.com"));
                fetchLatch.countDown();
            }
        });

        manageUser.getByEmail("test2@admin.com", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertTrue(!user.getFollowing().contains("test1@admin.com"));
                fetchLatch.countDown();
            }
        });
        fetchLatch.await(2000, TimeUnit.MILLISECONDS);
        lr1.remove();
        lr2.remove();
    }

    /**
     * No User Case.
     * Ensure user can see following list.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void followingTest() throws InterruptedException {
        //Add following to user1
        user1.addFollowing("test2@admin.com");
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);
        //Ensure following user is visible
        solo.clickOnText("Following");
        solo.waitForText("test2@admin.com");
    }

    /**
     * Follows User Case 11.
     * User is able to see their following public habits
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void followingHabitsTest() throws InterruptedException {
        //Add following to user1 and habit to user2
        user1.addFollowing("test2@admin.com");
        user2.addHabit(new Habit("TESTNAME", false, new Date(), new ArrayList<>(), ""));
        CountDownLatch latch = new CountDownLatch(2);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        manageUser.createOrUpdate(user2, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);
        //Ensure user2's habit is visible to user2
        solo.clickOnText("Following");
        solo.waitForText("test2@admin.com");
        solo.clickOnText("VIEW");
        solo.waitForText("TESTNAME");
    }

    /**
     * No User Case.
     * Ensure requests are visible to user.
     * @throws InterruptedException Latch is interrupted
     */
    @Test
    public void requestsTest() throws InterruptedException {
        //Add request to user1
        user1.addRequest("test2@admin.com");
        CountDownLatch latch = new CountDownLatch(1);
        manageUser.createOrUpdate(user1, new VoidCallback() {
            @Override
            public void onCallback() {
                latch.countDown();
            }
        });
        latch.await(2000, TimeUnit.MILLISECONDS);
        //Ensure request is visible
        solo.clickOnText("Requests");
        solo.waitForText("test2@admin.com");
    }

    /**
     * No User Case.
     * Ensure user is able to go back to MainActivity
     */
    @Test
    public void backToMainActivity() {
        solo.goBack();
        solo.waitForActivity(MainActivity.class);
    }
}

