package com.example.auctionapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseServices {
    private SQLiteDatabase database;
    private SQLHelper dbHelper;

    public DatabaseServices(Context context) {
        dbHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //Database Functions/Services
    //Function to insert new userdetails into database
    public long signUpNewUser(AuctionUser user) {
        ContentValues values = new ContentValues();
        values.put(SQLHelper.COLUMN_USERNAME, user.getUsername());
        values.put(SQLHelper.COLUMN_PASSWORD, user.getPassword());
        return database.insert(SQLHelper.TABLE_AUCTIONUSERS, null, values);
    }
    //Function to check if user exists in database
    public boolean doesAuctionUserExists(AuctionUser user) {
        String query = "SELECT * FROM " + SQLHelper.TABLE_AUCTIONUSERS +
                " WHERE " + SQLHelper.COLUMN_USERNAME + " = ?" +
                " AND " + SQLHelper.COLUMN_PASSWORD + " = ?";

        Cursor cursor = database.rawQuery(query, new String[] { user.getUsername(), user.getPassword() });

        boolean exists = cursor.getCount() > 0; // Check if the query returned any rows

        cursor.close();

        return exists;
    }
    //Function to get all users in database
    public List<AuctionUser> getAllAuctionUsers() {
        List<AuctionUser> users = new ArrayList<>();
        Cursor cursor = database.query(
                SQLHelper.TABLE_AUCTIONUSERS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                AuctionUser user = cursorToUser(cursor);
                users.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return users;
    }
    @SuppressLint("Range")
    private AuctionUser cursorToUser(Cursor cursor) {
        AuctionUser user = new AuctionUser();
        user.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.COLUMN_ID)));
        user.setUsername(cursor.getString(cursor.getColumnIndex(SQLHelper.COLUMN_USERNAME)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(SQLHelper.COLUMN_PASSWORD)));
        return user;
    }
}
