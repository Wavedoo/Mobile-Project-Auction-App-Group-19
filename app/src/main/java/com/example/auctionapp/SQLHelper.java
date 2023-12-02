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

    //Column names for Auction Items table:
    public static final String TABLE_AUCTION_ITEMS = "auctionItems";
    public static final String COLUMN_AUCTION_ITEM_ID = "_id";
    public static final String COLUMN_AUCTION_ITEM_TITLE = "title";
    public static final String COLUMN_AUCTION_ITEM_DESCRIPTION = "description";
    public static final String COLUMN_AUCTION_ITEM_LOCATION ="location";
    public static final String COLUMN_AUCTION_ITEM_START_BID = "startBid";
    public static final String COLUMN_AUCTION_ITEM_CURRENT_BID = "currentBid";
    public static final String COLUMN_AUCTION_ITEM_BIDS = "bids";
    public static final String COLUMN_AUCTION_ITEM_IMAGE = "image";
    public static final String COLUMN_AUCTION_ITEM_VIDEO = "video";

    //Column names for Bids Table
    public static final String TABLE_BIDS = "bids";
    public static final String COLUMN_BID_ID = "_id";
    public static final String COLUMN_BID_AUCTION_ITEM_ID = "auctionItemId";
    public static final String COLUMN_BID_AMOUNT = "bidAmount";
    public static final String COLUMN_BID_USER = "biddingUser";
    public static final String COLUMN_BID_TIME = "bidTime";
    public static final String COLUMN_BID_DATE = "bidDate";





    // SQL statement to create the auction users table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_AUCTIONUSERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT" +
                    COLUMN_ISADMIN + "TEXT" +
                    ");";

    //SQL statement to create the auction items table
    private static final String TABLE_AUCTION_ITEMS_CREATE =
            "CREATE TABLE " + TABLE_AUCTION_ITEMS + " (" +
                    COLUMN_AUCTION_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_AUCTION_ITEM_TITLE + " TEXT, " +
                    COLUMN_AUCTION_ITEM_DESCRIPTION + " TEXT, " +
                    COLUMN_AUCTION_ITEM_LOCATION + " TEXT, " +
                    COLUMN_AUCTION_ITEM_START_BID + " REAL, " +
                    COLUMN_AUCTION_ITEM_CURRENT_BID + " REAL, " +
                    COLUMN_AUCTION_ITEM_BIDS + " INTEGER," +
                    COLUMN_AUCTION_ITEM_IMAGE + " BLOB, " +
                    COLUMN_AUCTION_ITEM_VIDEO + " BLOB" +
                    ");";
    //SQL statement to create the bids table
    private static final String TABLE_BIDS_CREATE =
            "CREATE TABLE " + TABLE_BIDS + " (" +
                    COLUMN_BID_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BID_AUCTION_ITEM_ID + " INTEGER, " +
                    COLUMN_BID_AMOUNT + " REAL, " +
                    COLUMN_BID_USER + " TEXT, " +
                    COLUMN_BID_TIME + " TEXT, " +
                    COLUMN_BID_DATE + " TEXT" +
                    ");";


    //Constructor
    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
        database.execSQL(TABLE_AUCTION_ITEMS_CREATE);
        database.execSQL(TABLE_BIDS_CREATE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUCTIONUSERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUCTION_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIDS);
        onCreate(db);
    }
}
