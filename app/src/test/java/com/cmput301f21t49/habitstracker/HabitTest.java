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

    @Test
    public void testUpdateCompletion() {
        event.setStatus("In progress");
        habit.addEvent(event);
        habit.updateCompletion();
        assertEquals((long) habit.getPct(), 0);
        Event event2 = new Event();
        event2.setStatus("Completed");
        habit.addEvent(event2);
        habit.updateCompletion();
        assertEquals((long) habit.getPct(), 50);
    }
}
