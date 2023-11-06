package com.example.auctionapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLHelper extends SQLiteOpenHelper {
    //Auction User Table
    private static final String DATABASE_NAME = "auctionUsers.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_AUCTIONUSERS = "auctionUsers";
    //Columns names of Auction User Table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME  = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ISADMIN = "isAdmin";

    // SQL statement to create the auction users table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_AUCTIONUSERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT" +
                    COLUMN_ISADMIN + "TEXT" +
                    ");";

    //Constructor
    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
        //add more execSQL for other databases
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUCTIONUSERS);
        onCreate(db);
    }


}
