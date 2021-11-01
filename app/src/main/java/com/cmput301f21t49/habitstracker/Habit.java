package com.cmput301f21t49.habitstracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Habit implements Serializable {

    private String name;
    private int Id;
    private ArrayList<Event> Events;
    private ArrayList<Date> dates;
    private HashMap<String, Object> habitData = new HashMap<>();
    private double pct;

    /**
     * Habit Constructor
     * @param name
     *      Name of Habit
     * @param Id
     *      Unique habit ID
     * @param dates
     *      Dates the habit is meant to be done on
     */
    public Habit(String name, int Id, ArrayList<Date> dates) {
        if (name.length() > 20) {
            name.substring(0, 20);
        }
        this.name = name;
        this.Id = Id;
        this.dates = dates;
        habitData.put("ID", Id);
        habitData.put("Dates", dates);
        pct = 0;
        habitData.put("Percentage", pct);
        Events = new ArrayList<>();
    }

    /**
     * Get name of Habit
     * @return
     *      Habit name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get Unique habit Id
     * @return
     *      Habit ID
     */
    public int getId() {
        return this.Id;
    }

    /**
     * Get start date of habit
     * @return
     *      Start date
     */
    public ArrayList<Date> getDates() {
        return this.dates;
    }

    /**
     * Get specific event linked to Habit
     * @param index
     *      Event index
     * @return
     *      Event selected
     */

    /**
     * Returns data for firebase
     * @return
     *      Habit Data
     */
    public HashMap<String, Object> getHabitData() {return habitData; }

    public Event getEvent(int index){
        return Events.get(index);
    }

    /**
     * Get Completion status
     * @return
     *      Percentage of completion
     */
    public double getPct() {
        return this.pct;
    }

    /**
     * Get all Events linked to habit
     * @return
     *      ArrayList of all events
     */
    public ArrayList<Event> getAllEvents() {
        return this.Events;
    }

    /**
     * Set new Habit name
     * @param newName
     *      The new Name of the habit
     */
    public void setName(String newName) {
        if (newName.length() > 20) {
            newName.substring(0, 20);
        }
        this.name = newName;
    }

    /**
     * Set a new Start Date
     * @param newDates
     *      Updated new dates
     */
    public void setDates(ArrayList<Date> newDates) {
        this.dates = newDates;
        habitData.put("Dates", this.dates);
    }

    /**
     * Add Event to relate to Habit
     * @param event
     *      Event to add
     */
    public void addEvent(Event event) {
        Events.add(event);
    }

    /**
     * Update an Event
     * @param index
     *      Event index
     */
    public void updateEvent(int index, String newLocation, String newComment, String newName) {
        Event event = Events.get(index);
        event.setComment(newComment);
        event.setLocation(newLocation);
        event.setName(newName);
    }

    /**
     * Delete an event
     * @param index
     *      Event index
     */
    public void deleteEvent(int index) {
        Events.remove(index);
    }

    /**
     * Calculates new completion rate after change in status
     */
    public void updateCompletion() {
        int totalEvents = Events.size();
        int completed = 0;
        for (int i = 0; i < totalEvents; i++) {
            if (Events.get(i).getStatus() == "Completed") {
                completed = completed + 1;
            }
        }
        double newPct = completed/totalEvents;
        this.pct = newPct;
        habitData.put("Percentage", pct);
    }




}
