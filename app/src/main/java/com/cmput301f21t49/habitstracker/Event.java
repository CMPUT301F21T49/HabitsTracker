package com.cmput301f21t49.habitstracker;

import android.media.Image;

import java.io.Serializable;
import java.util.HashMap;

public class Event implements Serializable {

    private String name;
    private String status;
    private String location;
    private String comment;
    private Image image;
    private HashMap<String, Object> eventData = new HashMap<>();

    /**
     * Event constructor, on creation has a status of in progress
     * @param name
     *      Name of Event
     */
    public Event(String name, String location, String comment) {
        this.name = name;
        this.location = location;
        this.comment = comment;
        //this.image = image;
        status = "In Progress";
        eventData.put("Status", status);
        eventData.put("Location", location);
        eventData.put("Comment", comment);
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
     * Current image
     * @return
     *      Picture
     */
    public Image getImage() {return this.image;}

    /**
     * Get status
     * @return
     *      Current status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Return data for firebase
     * @return
     *      Event data
     */
    public HashMap<String, Object> getEventData() {return eventData;}
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
        eventData.put("Status", status);
    }

    /**
     * Update Location
     * @param newLocation
     *      New Location
     */
    public void setLocation(String newLocation) {
        this.location = newLocation;
        eventData.put("Location", location);
    }

    /**
     * Update Comment
     * @param newComment
     *      New Comment
     */
    public void setComment(String newComment) {
        this.comment = newComment;
        eventData.put("Comment", comment);
    }

    /**
     * Set new Image
     * @param newImage
     *      New Image
     */
    public void setImage(Image newImage) {this.image = newImage;}
}
