package com.example.recreaux;

import com.example.recreaux.UserContract;

import java.io.Serializable;
import java.util.ArrayList;

public class UserRecords implements Serializable {
    private int userIconID;
    private String userNickname;
    private String userResidence;
    private String userInterests;
    private String userFullName;
    private String userBio;
    private String userPhoneNumber;
    private int userID;


    public int getUserIconID() {
        return userIconID;
    }

    public void setUserIconID(int userIconID) {
        this.userIconID = userIconID;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserResidence() {
        return userResidence;
    }

    public void setUserResidence(String userResidence) {
        this.userResidence = userResidence;
    }

    public String getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(String userInterests) {
        this.userInterests = userInterests;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String toString(){
        return UserContract.User.COLUMN_userID+": "+this.userID+","+
                UserContract.User.COLUMN_userIconID+": "+this.userIconID+","+
                UserContract.User.COLUMN_userNickname+": "+this.userNickname+","+
                UserContract.User.COLUMN_userResidence+": "+this.userResidence+","+
                UserContract.User.COLUMN_userInterests+": "+this.userInterests+","+
                UserContract.User.COLUMN_userFullName+": "+this.userFullName+","+
                UserContract.User.COLUMN_userBio+": "+this.userBio+","+
                UserContract.User.COLUMN_userPhoneNumber+": "+this.userPhoneNumber;
    }
}
