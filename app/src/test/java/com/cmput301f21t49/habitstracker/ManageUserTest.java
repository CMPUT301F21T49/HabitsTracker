package com.cmput301f21t49.habitstracker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.fail;

import android.util.Log;

import org.junit.Test;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
/*
This test is not working right now. Gradle problems related to this test.
App is running though and manual Database tests for these functions ran fine.
 */

public class ManageUserTest {
    private static ManageUser manageUser = ManageUser.getInstance();
    /*
    Reference taken from:
    https://stackoverflow.com/questions/2321829/android-asynctask-testing-with-android-test-framework
     */
    @Test
    public void createUserObject() throws InterruptedException {
        /* Signal uses a lock to prevent the test from finishing until the test is done.
         * Should not use this technique to force things to be synchronous.
         */
        final CountDownLatch signal = new CountDownLatch(1);

        manageUser.createOrUpdate(new User("Testing_Id"), new VoidCallback() {
            @Override
            public void onCallback() {
                signal.countDown();
            }
        });

        signal.await();
    }

    /**
     * Retrieve an existing User object
     *
     * @throws InterruptedException
     */
    @Test
    public void getUserObject() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        manageUser.get("AZc5GIbBL5MiHN5zuQJMffAsLOl1", new UserCallback() {
            @Override
            public void onCallback(User user) {
                assertEquals(user.getId(), "AZc5GIbBL5MiHN5zuQJMffAsLOl1");
                signal.countDown();
            }
        }, new VoidCallback() {
            @Override
            public void onCallback() {
                fail();
                signal.countDown();
            }
        });
        signal.await();
    }
}
