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
        assertEquals(user.getHabits().get(0).getAllEvents().size(), 1);
    }

    // Testing ability to edit habits
    @Test
    public void editHabitTest() {
        habit.setName("PlaceHolder");
        user.addHabit(habit);
        assertTrue(user.getHabits().get(0).getName() =="PlaceHolder");
        ArrayList<String> days = new ArrayList<>();
        days.add("Monday");
        user.editHabit(0, "Edited", days);
        assertFalse(user.getHabits().get(0).getName() =="PlaceHolder");
    }

    // Test ability to edit Events
    @Test
    public void editEventTest() {
        user.addHabit(habit);
        event.setName("Hello");
        event.setStatus("In Progress");
        user.addEvent(0, event);
        assertTrue(user.getHabits().get(0).getAllEvents().get(0).getName().equals("Hello"));
        user.editEvent(0,0,"NewName", "NewStatus");
        assertFalse(user.getHabits().get(0).getAllEvents().get(0).getName().equals("PlaceHolder"));
    }

    // Test for deleting events
    @Test
    public void deleteEventTest() {
        user.addHabit(habit);
        user.addEvent(0, event);
        assertEquals(user.getHabits().get(0).getAllEvents().size(), 1);
        user.deleteEvent(0,0);
        assertEquals(user.getHabits().get(0).getAllEvents().size(), 0);
    }

    @Test
    public void deleteHabitTest() {
        user.addHabit(habit);
        assertEquals(user.getHabits().size(), 1);
        user.deleteHabit(0);
        assertEquals(user.getHabits().size(), 0);
    }
}
