package com.cmput301f21t49.habitstracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {

    String Id;
    ArrayList<Habit> Habits;

    public User(){}
    /**
     * Constructor for User
     * @param Id
     *      Unique user ID
     */
    public User(String Id) {
        this.Id = Id;
        Habits = new ArrayList<>();
    }

    /**
     * Method to get User ID
     * @return
     *      User ID
     */
    public String getId() {
        return Id;
    }
    public void setId(String Id){this.Id =Id;}

    /**
     * Getter method to return list of Habits for User
     * @return
     *      Habits ArrayList
     */
    public ArrayList<Habit> getHabits() {
        return Habits;
    }

    /**
     * Method to add a Habit
     * @param habit
     *      Habit to add
     */
    public void addHabit(Habit habit) {
        Habits.add(habit);
    }

    /**
     * Edit a Habit
     * @param index
     *      The index of Habit being accessed
     * @param newName
     *      New Name of the habit
     * @param Days
     *      New dates
     */
    public void editHabit(int index, String newName, ArrayList<String> Days) {
        Habit habit  = Habits.get(index);
        habit.setName(newName);
        habit.setDays(Days);
        Habits.set(index, habit);
    }

    /**
     * Adding an Event for a habit
     * @param index
     *      Habit index
     * @param event
     *      Event to be added
     */
    public void addEvent(int index, Event event) {
        Habit habit = Habits.get(index);
        habit.addEvent(event);
    }

    /**
     * Method to edit event
     * @param habitIndex
     *      Habit Index
     * @param eventIndex
     *      Event index to access event from Habit
     * @param newName
     *      Updated name of Eevent
     * @param newStatus
     *      Updated Status of Event
     */
    public void editEvent(int habitIndex, int eventIndex, String newName, String newStatus) {
        Habit habit = Habits.get(habitIndex);
        Event event = habit.getEvent(eventIndex);
        event.setName(newName);
        if (newStatus == "In Progress" || newStatus == "Completed") {
            event.setStatus(newStatus);
        }
        habit.updateEvent(eventIndex, event);
        habit.updateCompletion();
    }

    /**
     * Method to remove a Habit
     * @param index
     *      Index of Habit
     */
    public void deleteHabit(int index) {
        Habits.remove(index);
    }

    /**
     * Method for deleting an event
     * @param habitIndex
     *      Habit index
     * @param eventIndex
     *      Event Index
     */
    public void deleteEvent(int habitIndex, int eventIndex) {
        Habit habit = Habits.get(habitIndex);
        habit.deleteEvent(eventIndex);
        habit.updateCompletion();
    }
}
