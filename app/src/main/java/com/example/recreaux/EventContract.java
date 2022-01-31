package com.example.recreaux;

import android.provider.BaseColumns;

public class EventContract {
    public EventContract(){}

    public static abstract class Event implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_iconID = "iconID";
        public static final String COLUMN_eventName = "eventName";
        public static final String COLUMN_eventDate = "eventDate";
        public static final String COLUMN_eventTime= "eventTime";
        public static final String COLUMN_eventDescription = "eventDescription";
        public static final String COLUMN_eventTags = "eventTags";
        public static final String COLUMN_eventLocation = "eventLocation";
        public static final String COLUMN_eventLocationLatitude = "eventLocationLatitude";
        public static final String COLUMN_eventLocationLongitude = "eventLocationLongitude";
        public static final String COLUMN_eventParticipants = "eventParticipants";
        public static final String COLUMN_eventPostReport = "eventPostReport";
        public static final String COLUMN_eventGallery = "eventGallery";
        public static final String COLUMN_eventID = "eventID";
        public static final String COLUMN_eventCreatorID = "eventCreatorID";
    }

}
