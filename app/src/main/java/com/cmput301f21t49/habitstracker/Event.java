package com.cmput301f21t49.habitstracker;

import java.io.Serializable;

public class Event implements Serializable {

    private String name;
    private String status;

    /**
     * Event constructor, on creation has a status of in progress
     * @param name
     *      Name of Event
     */
    public Event(String name) {
        this.name = name;
        status = "In Progress";
    }

    /**
     * Get name of the Event
     * @return
     *      Name of Event
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the Status of Event
     * @return
     *      Status of Event
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Set new Name
     * @param newName
     *      New name of Event
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Update Status of Event
     * @param newStatus
     *      Either Completed or In progress
     */
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
