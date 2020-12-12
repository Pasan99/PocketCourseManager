package com.triangle.pocketcoursemanager.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.triangle.pocketcoursemanager.db.DatabaseConstants;
import com.triangle.pocketcoursemanager.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao extends SQLiteOpenHelper {

    public UserDao(Context context) {
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
    public long insert(User user){
        long newRowId = 0;
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.UserEntry.COLUMN_NAME, user.getName());
            values.put(DatabaseConstants.UserEntry.COLUMN_EMAIL, user.getEmail());
            values.put(DatabaseConstants.UserEntry.COLUMN_PASSWORD, user.getPassword());
            values.put(DatabaseConstants.UserEntry.COLUMN_DESCRIPTION, user.getDescription());
            values.put(DatabaseConstants.UserEntry.COLUMN_USER_TYPE, user.getUserType());

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insert(DatabaseConstants.UserEntry.TABLE_NAME, null, values);
        }
        catch (Exception ex){
            return 0;
        }
        return newRowId;
    }

    public ArrayList<User> getAllUsers(){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DatabaseConstants.UserEntry.COLUMN_NAME,
                DatabaseConstants.UserEntry.COLUMN_EMAIL,
                DatabaseConstants.UserEntry.COLUMN_PASSWORD,
                DatabaseConstants.UserEntry.COLUMN_DESCRIPTION,
                DatabaseConstants.UserEntry.COLUMN_USER_TYPE
        };

        Cursor cursor = db.query(
                DatabaseConstants.UserEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        ArrayList<User> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            User newUser = new User(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_USER_TYPE))
            );
            items.add(newUser);
        }
        cursor.close();
        return items;
    }

    public User getUser(String email, String password){
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DatabaseConstants.UserEntry.COLUMN_NAME,
                DatabaseConstants.UserEntry.COLUMN_EMAIL,
                DatabaseConstants.UserEntry.COLUMN_PASSWORD,
                DatabaseConstants.UserEntry.COLUMN_DESCRIPTION,
                DatabaseConstants.UserEntry.COLUMN_USER_TYPE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DatabaseConstants.UserEntry.COLUMN_EMAIL + " = ? and " + DatabaseConstants.UserEntry.COLUMN_PASSWORD + " = ? ";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(
                DatabaseConstants.UserEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        User user = null;
        while(cursor.moveToNext()) {
            user = new User(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConstants.UserEntry.COLUMN_USER_TYPE))
            );
        }
        cursor.close();
        return user;
    }

    public long update(User user){
        long newRowId = 0;
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.UserEntry.COLUMN_NAME, user.getName());
            values.put(DatabaseConstants.UserEntry.COLUMN_EMAIL, user.getEmail());
            values.put(DatabaseConstants.UserEntry.COLUMN_PASSWORD, user.getPassword());
            values.put(DatabaseConstants.UserEntry.COLUMN_DESCRIPTION, user.getDescription());
            values.put(DatabaseConstants.UserEntry.COLUMN_USER_TYPE, user.getUserType());

            // Which row to update, based on the title
            String selection = DatabaseConstants.UserEntry._ID + " = ?";
            String[] selectionArgs = { String.valueOf(user.getId()) };

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.update(
                    DatabaseConstants.UserEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
        catch (Exception ex){
            return 0;
        }
        return newRowId;
    }
}
