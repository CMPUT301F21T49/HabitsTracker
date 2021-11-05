package com.cmput301f21t49.habitstracker;

import java.io.Serializable;
import java.time.LocalDate;
/*
 * Event
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
 * Base Event Class
 * @author team 49
 */

public class Event implements Serializable {

    private String name;
    private String status;
    private String location;
    private String comment;
    private String image_url;
    private LocalDate date;

    /**
     * Empty constructor to invoke event
     */
    public Event(){}

    /**
     * Event constructor, on creation has a status of in progress (temporary)
     * @param name
     *      Name of Event
     * @param location
     *      Location of Event
     * @param comment
     *      Comment under event
     * @param date
     *      Date at which event is meant to occur
     */
    public Event(String name, String location, String comment, LocalDate date) {
        this.name = name;
        this.location = location;
        this.comment = comment;
        this.date = date;
        status = "In Progress";
    }

    /**
     * Get date
     * @return
     *      The event date as a LocalDate var
     */
    public LocalDate getDate(){
        return date;
    }

    /**
     * Set the new event date
     * @param
     *      date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets image url
     * @return
     *      The event image's url as a string
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * Set the image url, leads to event image
     * @param image_url
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
     * Get location of Event
     * @return
     *      Location
     */
    public String getLocation() {return this.location;}

    /**
     * Get comment of event
     * @return
     *      Comment
     */
    public String getComment() {return this.comment;}


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

    /**
     * Update Location
     * @param newLocation
     *      New Location
     */
    public void setLocation(String newLocation) {this.location = newLocation;}

    /**
     * Update Comment
     * @param newComment
     *      New Comment
     */
    public void setComment(String newComment) {this.comment = newComment;}

}
