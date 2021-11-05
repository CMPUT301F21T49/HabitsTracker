package com.cmput301f21t49.habitstracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/*
 * Habit
 *
 * version 1.0
 *
 * November 3, 2021
 *
 *Copyright [2021] CMPUT301F21T49: Purvi Singh, Justin. Saif, Fan Zhu

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 */

/**
 * Base Habit Class
 * @author team 49
 */

public class Habit implements Serializable {

    private String name;
    private int Id;
    private ArrayList<Event> Events = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private double pct= 0;

    /**
     * Empty constructor used normally
     */
    public Habit(){}
    /**
     * Habit Constructor with params for testing (temporary)
     * @param name
     *      Name of Habit
     * @param Id
     *      Unique habit ID
     * @param days
     *      Dates the habit is meant to be done on
     */
    public Habit(String name, int Id, ArrayList<String> days) {
        if (name.length() > 20) {
            name.substring(0, 20);
        }
        this.name = name;
        this.Id = Id;
        this.days = days;
        pct = 0;
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
     * Get specific event linked to Habit
     * @param index
     *      Event index
     * @return
     *      Event selected
     */
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
     * Delete all events linked to this Habit
     */
    public void deleteAllEvents(){
        this.Events = null;
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
     * @param event
     *      Event to be updated
     */
    public void updateEvent(int index, Event event) {
        Events.set(index, event);
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
        double completed = 0;
        for (int i = 0; i < totalEvents; i++) {
            if (Events.get(i).getStatus().equals("Completed")) {
                completed++;
            }
        }
        if (totalEvents != 0) {
            double newPct = completed/totalEvents;
            this.setPct(newPct);
        }
    }

    /**
     * Set Id for Habit
     * @param id
     *      Id to be assigned to Habit
     */
    public void setId(int id) {
        Id = id;
    }

    /**
     * Get all events related to the habit
     * @return
     *      ArrayList of Events
     */
    public ArrayList<Event> getEvents() {
        return Events;
    }

    /**
     * Set Events for the habit
     * @param events
     *      ArrayList of Events
     */
    public void setEvents(ArrayList<Event> events) {
        Events = events;
    }

    /**
     * Get days in which the habit is meant to be done
     * @return
     *      ArrayList of Days
     */
    public ArrayList<String> getDays() {
        return days;
    }

    /**
     * Set the days habit is to be done
     * @param days
     *      ArrayList of Days
     */
    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    /**
     * Set percentage of habit completed
     * @param pct
     *      Percentage
     */
    public void setPct(double pct) {
        this.pct = pct;
    }
}