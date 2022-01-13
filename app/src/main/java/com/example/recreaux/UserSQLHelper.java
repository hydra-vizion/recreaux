package com.example.recreaux;
import com.example.recreaux.UserContract.User;
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

public class UserSQLHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "users.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+
            User.TABLE_NAME+"(" + User.COLUMN_userID + " TEXT,"+
            User.COLUMN_userIconID + " TEXT,"+User.COLUMN_userNickname + " TEXT,"+
            User.COLUMN_userResidence + " TEXT,"+ User.COLUMN_userInterests + " TEXT,"+
            User.COLUMN_userFullName + " TEXT,"+ User.COLUMN_userBio + " TEXT,"+
            User.COLUMN_userPhoneNumber;
    private  static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    private String[] allColumn={
            User.COLUMN_userID,
            User.COLUMN_userIconID,
            User.COLUMN_userNickname,
            User.COLUMN_userResidence,
            User.COLUMN_userInterests,
            User.COLUMN_userFullName,
            User.COLUMN_userBio,
            User.COLUMN_userPhoneNumber,
    };


    public UserSQLHelper(Context context){
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


    public void update(UserRecords newUserRecord, UserRecords oldUserRecord){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.COLUMN_userID,newUserRecord.getUserID());
        values.put(User.COLUMN_userIconID,newUserRecord.getUserIconID());
        values.put(User.COLUMN_userNickname,newUserRecord.getUserNickname());
        values.put(User.COLUMN_userResidence,newUserRecord.getUserResidence());
        values.put(User.COLUMN_userInterests,newUserRecord.getUserInterests());
        values.put(User.COLUMN_userFullName,newUserRecord.getUserFullName());
        values.put(User.COLUMN_userBio,newUserRecord.getUserBio());
        values.put(User.COLUMN_userPhoneNumber,newUserRecord.getUserPhoneNumber());

        database.update(User.TABLE_NAME, values, "UserID = ?", new String[]{String.valueOf(oldUserRecord.getUserID())});
        database.close();
    }


}

