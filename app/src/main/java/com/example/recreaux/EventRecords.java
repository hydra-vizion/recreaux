package com.example.recreaux;

import java.io.Serializable;
import java.util.ArrayList;

public class EventRecords implements Serializable {
    private int iconID;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventDescription;
    private String eventTags;
    private String eventLocation;
    private String eventLocationLatitude;
    private String eventLocationLongitude;
    private ArrayList<String> eventParticipants;
    private String eventPostReport;
    private ArrayList<String> eventGallery;
    private int eventID;
    private int eventCreatorID;

    public int getEventCreatorID() {
        return eventCreatorID;
    }

    public void setEventCreatorID(int eventCreatorID) {
        this.eventCreatorID = eventCreatorID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getIconID() {
        return iconID;
    }
    public void setIconID(int iconID) {
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

    public ArrayList<String> getEventParticipants() {return eventParticipants; }


    public void setEventParticipants(ArrayList<String> eventParticipants) {

        this.eventParticipants = eventParticipants;
    }

    public String getEventPostReport() {
        return eventPostReport;
    }

    public void setEventPostReport(String eventPostReport) {
        this.eventPostReport = eventPostReport;
    }

    public ArrayList<String> getEventGallery() {
        return eventGallery;
    }

    public void setEventGallery(ArrayList<String> eventGallery) {
        this.eventGallery = eventGallery;
    }

    @Override
    public String toString(){
        return com.example.recreaux.EventContract.Event.COLUMN_iconID+": "+this.iconID+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventName+": "+this.eventName+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventDate+": "+this.eventDate+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventTime+": "+this.eventTime+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventDescription+": "+this.eventDescription+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventTags+": "+this.eventTags+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventLocation+": "+this.eventLocation+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventLocationLatitude+": "+this.eventLocationLatitude+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventLocationLongitude+": "+this.eventLocationLongitude+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventParticipants+": "+this.eventParticipants+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventPostReport+": "+this.eventPostReport+","+
                com.example.recreaux.EventContract.Event.COLUMN_eventGallery+": "+this.eventGallery;
    }
}
