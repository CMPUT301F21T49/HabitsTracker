package com.cmput301f21t49.habitstracker;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User implements Serializable {

    FirebaseFirestore db;
    ArrayList<Habit> Habits;
    HashMap<String, Object> userdata = new HashMap<>();
    HashMap<Integer, Object> habitRefs = new HashMap<>();
    DocumentReference userRef;
    String email;

    /**
     * Constructor for User, Adds user to firebase
     */
    public User(String email) {
        this.email = email;
        userdata.put("Email", email);
        Habits = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users");
        userRef = db.collection("Users").document();
        userRef.set(userdata);
        userRef.collection("Habits");
    }

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
        HashMap<String, Object> habitData = habit.getHabitData();
        habitData.put("ID", habit.getId());
        habitData.put("Dates", habit.getDates());
        userRef.collection("Habits").document(habit.getName()).set(habitData);

    }

    /**
     * Edit a Habit
     * @param index
     *      The index of Habit being accessed
     * @param newName
     *      New Name of the habit
     * @param newDates
     *      New dates
     */
    public void editHabit(int index, String newName, ArrayList<Date> newDates) {
        Habit habit  = Habits.get(index);
        String oldName = habit.getName();
        habit.setName(newName);
        habit.setDates(newDates);
        HashMap<String, Object> habitData = habit.getHabitData();
        Habits.set(index, habit);
        if (oldName == newName) {
            userRef.collection("Habits").document(oldName).set(habitData);
        }
        else {
            userRef.collection("Habits").document(oldName).delete();
            userRef.collection("Habits").document(newName).set(habitData);
        }
        //data.put("Habits", Habits);
        //userRef.set(data);
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
        HashMap<String, Object> eventData = event.getEventData();
        userRef.collection("Habits").document(habit.getName()).collection("Events").document(event.getName()).set(eventData);
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
    public void editEvent(int habitIndex, int eventIndex, String newName, String newStatus, String newLocation, String newComment) {
        Habit habit = Habits.get(habitIndex);
        Event event = habit.getEvent(eventIndex);
        String oldName = event.getName();
        event.setName(newName);
        event.setComment(newComment);
        event.setLocation(newLocation);
        event.setName(newName);
        if (newStatus == "In Progress" || newStatus == "Completed") {
            event.setStatus(newStatus);
        }
        HashMap<String, Object> eventData = event.getEventData();
        if (oldName == newName) {
            userRef.collection("Habits").document(habit.getName()).collection("Events").document(oldName).set(eventData);
        }
        else {
            userRef.collection("Habits").document(habit.getName()).collection("Events").document(oldName).delete();
            userRef.collection("Habits").document(habit.getName()).collection("Events").document(newName).set(eventData);
        }
        habit.updateCompletion();
    }

    /**
     * Method to remove a Habit
     * @param index
     *      Index of Habit
     */
    public void deleteHabit(int index) {
        Habit habit = Habits.get(index);
        userRef.collection("Habits").document(habit.getName()).delete();
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
        Event event = habit.getEvent(eventIndex);
        userRef.collection("Habits").document(habit.getName()).collection("Events").document(event.getName()).delete();
        habit.deleteEvent(eventIndex);
        habit.updateCompletion();
    }

    /**
     * User reference to get stuff from firebase
     * @return
     *      User ref
     */
    public DocumentReference getUserRef() {return userRef;}
}
