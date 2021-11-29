package com.cmput301f21t49.habitstracker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/*
 * Event
 *
 * version 1.0
 *
 * November 3, 2021
 *
 *Copyright [2021] CMPUT301F21T49: Purvi Singh, Justin, Saif, Fan Zhu

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
    private boolean status;
    private String comment;
    private Date completionDate;

    /**
     * Empty constructor to invoke event
     */

    public Event(){
    }
    public Event(String name, Date d){
        this.name = name;
        this.completionDate = d;
    }


    /**
     * Get date
     * @return
     *      The event completionDate
     */
    public Date getCompletionDate() {
        return completionDate;
    }


    /**
     * Set the new event date
     * @param completionDate
     */
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
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
    public Boolean getStatus() {
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
    public void setStatus(Boolean newStatus) {
        this.status = newStatus;
    }



    /**
     * Update Comment
     * @param newComment
     *      New Comment
     */
    public void setComment(String newComment) {this.comment = newComment;}

}
