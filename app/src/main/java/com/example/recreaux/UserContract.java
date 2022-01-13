package com.example.recreaux;

import android.provider.BaseColumns;

public class UserContract {
    public UserContract(){}

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_userID = "userID";
        public static final String COLUMN_userIconID = "userIconID";
        public static final String COLUMN_userNickname = "userNickname";
        public static final String COLUMN_userResidence = "userResidence";
        public static final String COLUMN_userInterests= "userInterests";
        public static final String COLUMN_userFullName = "userFullName";
        public static final String COLUMN_userBio = "userBio";
        public static final String COLUMN_userPhoneNumber = "userPhoneNumber";
    }

}
