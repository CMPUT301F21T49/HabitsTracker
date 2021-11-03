package com.cmput301f21t49.habitstracker;

import android.media.Image;

import java.io.Serializable;

public class Event implements Serializable {

    private String name;
    private String status;
    private String location;
    private String comment;
    private String image_url;
    private Image image;

    /**
     * Event constructor, on creation has a status of in progress
     * @param name
     *      Name of Event
     */
    public Event(){}
    public Event(String name, String location, String comment, Image image) {
        this.name = name;
        this.location = location;
        this.comment = comment;
        this.image = image;
        status = "In Progress";
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

    /**
     * Set new Image
     * @param newImage
     *      New Image
     */
    public void setImage(Image newImage) {this.image = newImage;}
}
