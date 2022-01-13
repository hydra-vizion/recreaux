package com.example.recreaux;
import com.example.recreaux.EventContract.Event;
import com.example.recreaux.EventRecords;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventSQLHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "events.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+
            Event.TABLE_NAME+"(" + Event.COLUMN_iconID + " TEXT,"+
            Event.COLUMN_eventName + " TEXT,"+Event.COLUMN_eventDate + " TEXT,"+
            Event.COLUMN_eventTime + " TEXT,"+ Event.COLUMN_eventDescription + " TEXT,"+
            Event.COLUMN_eventTags + " TEXT,"+ Event.COLUMN_eventLocation + " TEXT,"+
            Event.COLUMN_eventLocationLatitude + " TEXT,"+ Event.COLUMN_eventLocationLongitude +
            " TEXT,"+ Event.COLUMN_eventParticipants + " TEXT," + Event.COLUMN_eventPostReport +
            " TEXT,"+ Event.COLUMN_eventGallery+ " TEXT,"+ Event.COLUMN_eventID +" TEXT)";
    private  static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Event.TABLE_NAME;

    private String[] allColumn={
            Event.COLUMN_iconID,
            Event.COLUMN_eventName,
            Event.COLUMN_eventDate,
            Event.COLUMN_eventTime,
            Event.COLUMN_eventDescription,
            Event.COLUMN_eventTags,
            Event.COLUMN_eventLocation,
            Event.COLUMN_eventLocationLatitude,
            Event.COLUMN_eventLocationLongitude,
            Event.COLUMN_eventParticipants,
            Event.COLUMN_eventPostReport,
            Event.COLUMN_eventGallery,
            Event.COLUMN_eventID
    };


    public EventSQLHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        super.onDowngrade(db,oldVersion,newVersion);
    }

    public void insertEvent(EventRecords eventRecords){
        ContentValues values = new ContentValues();
        values.put(Event.COLUMN_iconID,eventRecords.getIconID());
        values.put(Event.COLUMN_eventName,eventRecords.getEventName());
        values.put(Event.COLUMN_eventDate,eventRecords.getEventDate());
        values.put(Event.COLUMN_eventTime,eventRecords.getEventTime());
        values.put(Event.COLUMN_eventDescription,eventRecords.getEventDescription());
        values.put(Event.COLUMN_eventTags,eventRecords.getEventTags());
        values.put(Event.COLUMN_eventLocation,eventRecords.getEventLocation());
        values.put(Event.COLUMN_eventLocationLatitude,eventRecords.getEventLocationLatitude());
        values.put(Event.COLUMN_eventLocationLongitude,eventRecords.getEventLocationLongitude());

        Gson gson = new Gson();
        String eventParticipants= gson.toJson(eventRecords.getEventParticipants());
        values.put(Event.COLUMN_eventParticipants,eventParticipants);
        values.put(Event.COLUMN_eventPostReport,eventRecords.getEventPostReport());
        String eventGallery= gson.toJson(eventRecords.getEventGallery());
        values.put(Event.COLUMN_eventGallery,eventGallery);

        Cursor biggestcursor;
        int biggestindex;

        SQLiteDatabase databaseread = this.getReadableDatabase();
        biggestcursor=databaseread.rawQuery("SELECT MAX(eventID) FROM events", null);
        if (biggestcursor==null){
            biggestindex=0;
        }
        else{
            biggestindex=Integer.valueOf(biggestcursor.getString(12))+1;
        }
        databaseread.close();
        values.put(Event.COLUMN_eventID,biggestindex);
        values.put(Event.COLUMN_eventCreatorID,eventRecords.getEventCreatorID());

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(Event.TABLE_NAME, null,values);
        database.close();
    }

    public List<EventRecords> getAllEvent(){
        List<EventRecords> records = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Event.TABLE_NAME, allColumn, null,null,null,null,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            EventRecords eventRecords = new EventRecords();
            eventRecords.setIconID(Integer.valueOf(cursor.getString(0)));
            eventRecords.setEventName(cursor.getString(1));
            eventRecords.setEventDate(cursor.getString(2));
            eventRecords.setEventTime(cursor.getString(3));
            eventRecords.setEventDescription(cursor.getString(4));
            eventRecords.setEventTags(cursor.getString(5));
            eventRecords.setEventLocation(cursor.getString(6));
            eventRecords.setEventLocationLatitude(cursor.getString(7));
            eventRecords.setEventLocationLongitude(cursor.getString(8));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> eventParticipants = gson.fromJson(cursor.getString(9), type);
            eventRecords.setEventParticipants(eventParticipants);
            eventRecords.setEventPostReport(cursor.getString(10));

            Type type2 = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> eventGallery = gson.fromJson(cursor.getString(11), type2);
            eventRecords.setEventGallery(eventGallery);
            eventRecords.setEventID(Integer.valueOf(cursor.getString(12)));
            eventRecords.setEventCreatorID(Integer.valueOf(cursor.getString(13)));
            records.add(eventRecords);
            cursor.moveToNext();
        }
        database.close();
        return records;
    }

    public void update(EventRecords neweventRecord, EventRecords oldeventRecord){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Event.COLUMN_iconID,neweventRecord.getIconID());
        values.put(Event.COLUMN_eventName,neweventRecord.getEventName());
        values.put(Event.COLUMN_eventDate,neweventRecord.getEventDate());
        values.put(Event.COLUMN_eventTime,neweventRecord.getEventTime());
        values.put(Event.COLUMN_eventDescription,neweventRecord.getEventDescription());
        values.put(Event.COLUMN_eventTags,neweventRecord.getEventTags());
        values.put(Event.COLUMN_eventLocation,neweventRecord.getEventLocation());
        values.put(Event.COLUMN_eventLocationLatitude,neweventRecord.getEventLocationLatitude());
        values.put(Event.COLUMN_eventLocationLongitude,neweventRecord.getEventLocationLongitude());

        Gson gson = new Gson();
        String eventParticipants= gson.toJson(neweventRecord.getEventParticipants());
        values.put(Event.COLUMN_eventParticipants,eventParticipants);
        values.put(Event.COLUMN_eventPostReport,neweventRecord.getEventPostReport());
        String eventGallery= gson.toJson(neweventRecord.getEventGallery());
        values.put(Event.COLUMN_eventGallery,eventGallery);
        values.put(Event.COLUMN_eventID,oldeventRecord.getEventID());
        values.put(Event.COLUMN_eventCreatorID,oldeventRecord.getEventID());

        database.update(Event.TABLE_NAME, values, "eventID = ?", new String[]{String.valueOf(oldeventRecord.getEventID())});
        database.close();
    }

    public void delete(EventRecords eventRecords){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Event.TABLE_NAME, "eventID = ?", new String[]{String.valueOf(eventRecords.getEventID())});
        database.close();
    }


}
