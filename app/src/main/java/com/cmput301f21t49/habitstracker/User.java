package com.cmput301f21t49.habitstracker;

import androidx.annotation.Nullable;

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
    ArrayList<String> following = new ArrayList<>();
    ArrayList<String> followers = new ArrayList<>();
    ArrayList<String> requests = new ArrayList<>();
    ArrayList<String> allowPrivate = new ArrayList<>();

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
     * @param userEmail
     *      User that is following current user
     */
    public void addFollower(String userEmail) {
        followers.add(userEmail);
    }


    /**
     * Remove a follower
     * @param userEmail
     *      Follower to remove
     */
    public void removeFollower(String userEmail) {followers.remove(userEmail);}

    /**
     * Get List of all followers
     * @return
     *      Followers ArrayList
     */
    public ArrayList<String> getFollowers() {
        return followers;
    }

    /**
     * Add following if a user accepts sent follow request
     * @param userEmail
     *      User current user is following
     */
    public void addFollowing(String userEmail) {
        following.add(userEmail);
    }

    /**
     * List of Following users
     * @return
     *      ArrayList of users that current user is following
     */
    public ArrayList<String> getFollowing() {
        return this.following;
    }

    /**
     * Remove Following
     * @param userEmail User to unfollow
     */
    public void removeFollowing(String userEmail) {
        following.remove(userEmail);
    }
    /**
     * Method that adds a request if another user sends
     * @param userEmail
     *      User that sent the request
     */
    public void addRequest(String userEmail) {
        requests.add(userEmail);
    }

    /**
     * Method that removes a request
     * @param userEmail
     *      User to be removed
     */
    public void removeRequest(String userEmail) {
        requests.remove(userEmail);
    }

    /**
     * Method to return all requests
     * @return
     *      Users that have made a request
     */
    public ArrayList<String> getRequests() {
        return requests;
    }


    public void addAllowPrivate(String userEmail) {
        allowPrivate.add(userEmail);
    }

    public void removeAllowPrivate(String userEmail) {
        allowPrivate.remove(userEmail);
    }

    public ArrayList<String> getAllowPrivate() {
        return allowPrivate;
    }

    //Override equals
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        User user = (User) obj;

        return email.equals(user.getEmail()) && id.equals(user.getId());
    }
}
