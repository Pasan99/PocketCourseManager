package com.triangle.pocketcoursemanager.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.triangle.pocketcoursemanager.db.DatabaseConstants;
import com.triangle.pocketcoursemanager.models.Course;

import java.util.ArrayList;

public class CourseDao extends SQLiteOpenHelper {

    public CourseDao(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.CourseEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseConstants.UserCourseEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseConstants.UserEntry.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseConstants.UserEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseConstants.CourseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseConstants.UserCourseEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public long insert(Course course){
        long newRowId = 0;
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.CourseEntry.COLUMN_NAME, course.getName());
            values.put(DatabaseConstants.CourseEntry.COLUMN_CODE, course.getCode());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION, course.getDescription());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DURATION, course.getDuration());
            values.put(DatabaseConstants.CourseEntry.COLUMN_FEE, course.getFee());
            values.put(DatabaseConstants.CourseEntry.COLUMN_START_DATE, course.getStartDate());
            values.put(DatabaseConstants.CourseEntry.COLUMN_END_DATE, course.getEndDate());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DAYS, course.getDays());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME, course.getDayStartTime());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME, course.getDayEndTime());
            values.put(DatabaseConstants.CourseEntry.COLUMN_IS_DELETED, 0);

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insert(DatabaseConstants.CourseEntry.TABLE_NAME, null, values);
        }
        catch (Exception ex){
            return 0;
        }
        return newRowId;
    }

    public ArrayList<Course> getAllCourses(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DatabaseConstants.CourseEntry.COLUMN_NAME,
                DatabaseConstants.CourseEntry.COLUMN_CODE,
                DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION,
                DatabaseConstants.CourseEntry.COLUMN_DURATION,
                DatabaseConstants.CourseEntry.COLUMN_FEE,
                DatabaseConstants.CourseEntry.COLUMN_START_DATE,
                DatabaseConstants.CourseEntry.COLUMN_END_DATE,
                DatabaseConstants.CourseEntry.COLUMN_DAYS,
                DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME,
                DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DatabaseConstants.CourseEntry.COLUMN_IS_DELETED + " = ?";
        String[] selectionArgs = { "0" };

        Cursor cursor = db.query(
                DatabaseConstants.CourseEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<Course> courses = new ArrayList<>();
        while(cursor.moveToNext()) {
            courses.add(
                    new Course(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_CODE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DURATION)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_FEE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_START_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_END_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAYS)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME))
                    )
            );
        }
        cursor.close();
        return courses;
    }

    public ArrayList<Course> getCoursesByUserId(long userId){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DatabaseConstants.CourseEntry.COLUMN_NAME,
                DatabaseConstants.CourseEntry.COLUMN_CODE,
                DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION,
                DatabaseConstants.CourseEntry.COLUMN_DURATION,
                DatabaseConstants.CourseEntry.COLUMN_FEE,
                DatabaseConstants.CourseEntry.COLUMN_START_DATE,
                DatabaseConstants.CourseEntry.COLUMN_END_DATE,
                DatabaseConstants.CourseEntry.COLUMN_DAYS,
                DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME,
                DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME,
        };

        // Filter results WHERE "title" = 'My Title'
        String query = "select * from " + DatabaseConstants.CourseEntry.TABLE_NAME + " where "
                + DatabaseConstants.CourseEntry._ID +" IN  (" + "select "+ DatabaseConstants.UserCourseEntry.COLUMN_COURSE_ID
                +" from "+ DatabaseConstants.UserCourseEntry.TABLE_NAME +" where "
                + DatabaseConstants.UserCourseEntry.COLUMN_USER_ID +" = '"+ userId +"' and "+DatabaseConstants.CourseEntry.COLUMN_IS_DELETED+" = 0)";
        String[] selectionArgs = { };

        Cursor cursor = db.rawQuery(
                query,
                selectionArgs// The sort order
        );

        ArrayList<Course> courses = new ArrayList<>();
        while(cursor.moveToNext()) {
            courses.add(
                new Course(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DURATION)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_FEE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_END_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAYS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME))
                )
            );
        }
        cursor.close();
        return courses;
    }
    public ArrayList<Course> getNotAttendedCoursesByUserId(long userId){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DatabaseConstants.CourseEntry.COLUMN_NAME,
                DatabaseConstants.CourseEntry.COLUMN_CODE,
                DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION,
                DatabaseConstants.CourseEntry.COLUMN_DURATION,
                DatabaseConstants.CourseEntry.COLUMN_FEE,
                DatabaseConstants.CourseEntry.COLUMN_START_DATE,
                DatabaseConstants.CourseEntry.COLUMN_END_DATE,
                DatabaseConstants.CourseEntry.COLUMN_DAYS,
                DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME,
                DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME,
        };

        // Filter results WHERE "title" = 'My Title'
        String query = "select * from " + DatabaseConstants.CourseEntry.TABLE_NAME + " where "
                + DatabaseConstants.CourseEntry._ID +" NOT IN (" + "select "+ DatabaseConstants.UserCourseEntry.COLUMN_COURSE_ID
                +" from "+ DatabaseConstants.UserCourseEntry.TABLE_NAME +" where "
                + DatabaseConstants.UserCourseEntry.COLUMN_USER_ID +" = '"+ userId +"' and "+DatabaseConstants.CourseEntry.COLUMN_IS_DELETED+" = 0)";
        String[] selectionArgs = { };

        Cursor cursor = db.rawQuery(
                query,
                selectionArgs// The sort order
        );

        ArrayList<Course> courses = new ArrayList<>();
        while(cursor.moveToNext()) {
            courses.add(
                    new Course(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_CODE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DURATION)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_FEE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_START_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_END_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAYS)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME))
                    )
            );
        }
        cursor.close();
        return courses;
    }

    public long assignCourseToUser(long courseId, long userId){
        long newRowId = 0;
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.UserCourseEntry.COLUMN_USER_ID, userId);
            values.put(DatabaseConstants.UserCourseEntry.COLUMN_COURSE_ID, courseId);

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insert(DatabaseConstants.UserCourseEntry.TABLE_NAME, null, values);
        }
        catch (Exception ex){
            return 0;
        }
        return newRowId;
    }

    public void update(Course course){
        long newRowId = 0;
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.CourseEntry.COLUMN_NAME, course.getName());
            values.put(DatabaseConstants.CourseEntry.COLUMN_CODE, course.getCode());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DESCRIPTION, course.getDescription());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DURATION, course.getDuration());
            values.put(DatabaseConstants.CourseEntry.COLUMN_FEE, course.getFee());
            values.put(DatabaseConstants.CourseEntry.COLUMN_START_DATE, course.getStartDate());
            values.put(DatabaseConstants.CourseEntry.COLUMN_END_DATE, course.getEndDate());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DAYS, course.getDays());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DAY_START_TIME, course.getDayStartTime());
            values.put(DatabaseConstants.CourseEntry.COLUMN_DAY_END_TIME, course.getDayEndTime());

            // Which row to update, based on the title
            String selection = DatabaseConstants.CourseEntry._ID + " = ?";
            Log.e("Course Id", "" + course.getId());
            String[] selectionArgs = { String.valueOf(course.getId()) };

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.update(
                    DatabaseConstants.CourseEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
        catch (Exception ex){
        }
    }

    public void delete(long courseId){
        SQLiteDatabase db = getWritableDatabase();

        // Define 'where' part of query.
        String selection = DatabaseConstants.CourseEntry._ID + " =?";
        String selection2 = DatabaseConstants.UserCourseEntry._ID + " =?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(courseId) };

        // Issue SQL statement.
        db.delete(DatabaseConstants.UserCourseEntry.TABLE_NAME, selection2, selectionArgs);
        db.delete(DatabaseConstants.CourseEntry.TABLE_NAME, selection, selectionArgs);
    }
}
