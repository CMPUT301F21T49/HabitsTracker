package com.cmput301f21t49.habitstracker;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class HabitTest {

    Habit habit;
    Event event;

    @Before
    public void createHabit() {
        habit = new Habit();
        event = new Event();
    }

    // Test adding Habit Event
    @Test
    public void addHabitEventTest() {
        habit.addEvent(event);
        assertEquals(habit.getEvents().size(), 1);
    }

    // Test removing Habit Event
    @Test
    public void removeHabitEventTest() {
        habit.addEvent(event);
        assertEquals(habit.getEvents().size(), 1);
        habit.deleteEvent(0);
        assertEquals(habit.getEvents().size(), 0);
    }

    // Test editing event
    @Test
    public void editEventTest() {
        event.setName("Hello");
        habit.addEvent(event);
        assertEquals(habit.getEvent(0).getName(), "Hello");
        event.setName("Yo");
        assertEquals(habit.getEvent(0).getName(), "Yo");
    }



}
