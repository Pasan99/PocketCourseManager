package com.triangle.pocketcoursemanager.db;

import android.provider.BaseColumns;

public class DatabaseConstants {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CourseManager.db";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseConstants() {}

    /* Inner class that defines the table contents */
    public static class CourseEntry implements BaseColumns {
        private CourseEntry(){}

        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_FEE = "fee";
        public static final String COLUMN_START_DATE = "startDate";
        public static final String COLUMN_END_DATE = "endDate";
        public static final String COLUMN_DAYS = "days";
        public static final String COLUMN_DAY_START_TIME = "dayStartTime";
        public static final String COLUMN_DAY_END_TIME = "dayEndTime";
        public static final String COLUMN_IS_DELETED = "isDeleted";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + CourseEntry.TABLE_NAME + " (" +
                        CourseEntry._ID + " INTEGER PRIMARY KEY," +
                        CourseEntry.COLUMN_NAME + " TEXT," +
                        CourseEntry.COLUMN_CODE + " TEXT," +
                        CourseEntry.COLUMN_DESCRIPTION + " TEXT," +
                        CourseEntry.COLUMN_DURATION + " TEXT," +
                        CourseEntry.COLUMN_FEE + " REAL," +
                        CourseEntry.COLUMN_START_DATE + " DATE," +
                        CourseEntry.COLUMN_END_DATE + " DATE," +
                        CourseEntry.COLUMN_DAYS + " TEXT," +
                        CourseEntry.COLUMN_DAY_START_TIME + " TEXT," +
                        CourseEntry.COLUMN_IS_DELETED + " INTEGER," +
                        CourseEntry.COLUMN_DAY_END_TIME + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME;
    }

    public static class UserEntry implements BaseColumns {
        private UserEntry(){}

        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_USER_TYPE = "userType";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                        UserEntry._ID + " INTEGER PRIMARY KEY," +
                        UserEntry.COLUMN_NAME + " TEXT," +
                        UserEntry.COLUMN_EMAIL + " TEXT," +
                        UserEntry.COLUMN_DESCRIPTION + " TEXT," +
                        UserEntry.COLUMN_USER_TYPE + " TEXT," +
                        UserEntry.COLUMN_PASSWORD + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
    }

    public static class UserCourseEntry implements BaseColumns {
        private UserCourseEntry(){}

        public static final String TABLE_NAME = "user_course";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_USER_ID = "user_id";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserCourseEntry.TABLE_NAME + " (" +
                        UserCourseEntry._ID + " INTEGER PRIMARY KEY," +
                        UserCourseEntry.COLUMN_COURSE_ID + " TEXT," +
                        UserCourseEntry.COLUMN_USER_ID + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + UserCourseEntry.TABLE_NAME;
    }
}
