package com.cmput301f21t49.habitstracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UserTest {

    User user;
    Habit habit;
    Event event;

    // Create a usable user for tests
    @Before
    public void createUser() {
        user = new User();
        habit = new Habit();
        event = new Event();
    }

    // Test Setting ID
    @Test
    public void IdTest() {
        user.setId("Test ID");
        assertTrue(user.getId() == "Test ID");
    }

    // Test Adding habits
    @Test
    public void testAddHabits() {
        user.addHabit(habit);
        assertEquals(user.getHabits().size(), 1);
    }

    // Testing adding Events
    @Test
    public void testAddEvent() {
        user.addHabit(habit);
        user.addEvent(0, event);
        assertEquals(user.getHabits().get(0).getEvents().size(), 1);
    }


    // Test for deleting events
    @Test
    public void deleteEventTest() {
        user.addHabit(habit);
        user.addEvent(0, event);
        assertEquals(user.getHabits().get(0).getEvents().size(), 1);
        user.deleteEvent(0,0);
        assertEquals(user.getHabits().get(0).getEvents().size(), 0);
    }

    @Test
    public void deleteHabitTest() {
        user.addHabit(habit);
        assertEquals(user.getHabits().size(), 1);
        user.deleteHabit(0);
        assertEquals(user.getHabits().size(), 0);
    }

    // Test Following
    @Test
    public void followingTest() {
        user.addFollowing("Email.com");
        assertEquals(user.getFollowing().size(), 1);
    }

    // test removing following
    @Test
    public void deleteFollowingTest() {
        user.addFollowing("Email.com");
        assertEquals(user.getFollowing().size(), 1);
        user.removeFollowing("Email.com");
        assertEquals(user.getFollowing().size(), 0);
    }

    // Add follower Test
    @Test
    public void followerTest() {
        user.addFollower("Email.com");
        assertEquals(user.getFollowers().size(), 1);
    }

    // Remove follower Test
    @Test
    public void deleteFollowerTest() {
        user.addFollower("Email.com");
        assertEquals(user.getFollowers().size(), 1);
        user.removeFollower("Email.com");
        assertEquals(user.getFollowers().size(), 0);
    }

    // Test Adding requests
    @Test
    public void addRequestTest() {
        user.addRequest("Email.com");
        assertEquals(user.getRequests().size(), 1);
    }

    // Test removing requests
    @Test
    public void removeRequestTest() {
        user.addRequest("Email.com");
        assertEquals(user.getRequests().size(), 1);
        user.removeRequest("Email.com");
        assertEquals(user.getRequests().size(), 0);
    }

}
