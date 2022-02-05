package com.example.recreaux;

import java.io.Serializable;
import java.util.ArrayList;

public class EventRecords implements Serializable {
    private byte[] iconID;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventDescription;
    private String eventTags;
    private String eventLocation;
    private String eventLocationLatitude;
    private String eventLocationLongitude;
    private String eventPostReport;
    private int eventID;
    private String eventCreatorID;
    private boolean eventstatus;

    public boolean getEventstatus() {
        return eventstatus;
    }

    public void setEventstatus(boolean eventstatus) {
        this.eventstatus = eventstatus;
    }

    public String getEventCreatorID() {
        return eventCreatorID;
    }

    public void setEventCreatorID(String eventCreatorID) {
        this.eventCreatorID = eventCreatorID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public byte[] getIconID() {
        return iconID;
    }
    public void setIconID(byte[] iconID) {
        this.iconID = iconID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventTags() {
        return eventTags;
    }

    public void setEventTags(String eventTags) {
        this.eventTags = eventTags;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventLocationLatitude() {
        return eventLocationLatitude;
    }

    public void setEventLocationLatitude(String eventLocationLatitude) {
        this.eventLocationLatitude = eventLocationLatitude;
    }

    public String getEventLocationLongitude() {
        return eventLocationLongitude;
    }

    public void setEventLocationLongitude(String eventLocationLongitude) {
        this.eventLocationLongitude = eventLocationLongitude;
    }

    public String getEventPostReport() {
        return eventPostReport;
    }

    public void setEventPostReport(String eventPostReport) {
        this.eventPostReport = eventPostReport;
    }

}
