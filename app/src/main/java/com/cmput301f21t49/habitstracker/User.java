package com.cmput301f21t49.habitstracker;

import java.io.Serializable;
import java.util.ArrayList;
/*
 * User
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
 * Base User Class
 * @author team 49
 */

public class User implements Serializable {

    String email;
    String id;
    ArrayList<Habit> habits = new ArrayList<>();
    ArrayList<User> following = new ArrayList<>();
    ArrayList<User> followers = new ArrayList<>();
    ArrayList<String> requests = new ArrayList<>();

    static final public String SERIALIZED= "USER_CLASS"; //Key for Serialized Users


    /**
     * Empty Constructor used to invoke User
     */
    public User(){}

    /**
     * Temporary constructor with parameter
     * @param id
     *      Id of User
     */
    public User(String id) {
        this.id = id;
    }

    /**
     * Method to get User ID
     * @return
     *      User ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get User email
     * @return
     *      Email as String
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set User email
     * @param email
     *      User email stored as string
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Method to set ID
     * @param Id
     *      User assigned ID
     */
    public void setId(String Id){this.id =Id;}

    /**
     * Getter method to return list of Habits for User
     * @return
     *      Habits ArrayList
     */
    public ArrayList<Habit> getHabits() {
        return habits;
    }

    /**
     * Method to add a Habit
     * @param habit
     *      Habit to add
     */
    public void addHabit(Habit habit) {
        if (this.habits == null){
            this.habits = new ArrayList<Habit>();
        }
        this.habits.add(habit);
    }

    /**
     * Edit a Habit
     * @param index
     *      The index of Habit being accessed
     * @param editedHabit
     */
    public void editHabit(int index, Habit editedHabit) {
        this.habits.set(index, editedHabit);
    }

    /**
     * Adding an Event for a habit
     * @param index
     *      Habit index
     * @param event
     *      Event to be added
     */
    public void addEvent(int index, Event event) {
        Habit habit = this.habits.get(index);
        habit.addEvent(event);
    }

    /**
     * Method to edit event
     * @param habitIndex
     *      Habit Index
     * @param eventIndex
     *      Event index to access event from Habit
     * @param newName
     *      Updated name of Event
     * @param newStatus
     *      Updated Status of Event
     */
    public void editEvent(int habitIndex, int eventIndex, String newName, String newStatus) {
        Habit habit = this.habits.get(habitIndex);
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
        habits.remove(index);
    }

    /**
     * Method for deleting an event
     * @param habitIndex
     *      Habit index
     * @param eventIndex
     *      Event Index
     */
    public void deleteEvent(int habitIndex, int eventIndex) {
        Habit habit = this.habits.get(habitIndex);
        habit.deleteEvent(eventIndex);
        habit.updateCompletion();
    }

    /**
     * Add a follower if follow request accepted
     * @param user
     *      User that is following current user
     */
    public void addFollower(User user) {
        followers.add(user);
    }

    /**
     * Get List of all followers
     * @return
     *      Followers ArrayList
     */
    public ArrayList<User> getFollowers() {
        return this.followers;
    }

    /**
     * Add following if a user accepts sent follow request
     * @param user
     *      User current user is following
     */
    public void addFollowing(User user) {
        following.add(user);
    }

    /**
     * List of Following users
     * @return
     *      ArrayList of users that current user is following
     */
    public ArrayList<User> getFollowing() {
        return this.following;
    }

    /**
     * Method that adds a request if another user sends
     * @param email
     *      Email of user that sent the request
     */
    public void addRequest(String email) {
        requests.add(email);
    }

    /**
     * Once request is answered, remove request from list
     * @param index
     *      Index of request to be removed
     */
    public void onRequestAnswered(int index) {
        requests.remove(index);
    }

    /**
     * Overloaded method in case want to remove by email
     * @param email
     *      email of request removed
     */
    public void onRequestAnswered(String email) {
        requests.remove(email);
    }
}
