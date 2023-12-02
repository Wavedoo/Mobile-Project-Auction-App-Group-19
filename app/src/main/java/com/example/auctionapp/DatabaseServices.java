package com.example.auctionapp;

import static com.example.auctionapp.SQLHelper.TABLE_AUCTION_ITEMS;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
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
    //Auction Item Functions:
    //Retrieving Auction Item from Database
    //-> Functions to retrieve the Image/Video
        // Function to retrieve image as Bitmap from BLOB data
    private Bitmap getBitmapFromBlob(byte[] blobData) {
        return BitmapFactory.decodeByteArray(blobData, 0, blobData.length);
    }
    // Function to retrieve video as byte array from BLOB data
    private byte[] getVideoBytesFromBlob(byte[] blobData) {
        return blobData;
    }
    //Get All Auction Items
    public Cursor getAllAuctionItems() {
        return database.query(
                SQLHelper.TABLE_AUCTION_ITEMS,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    //Get Auction Item Details Using Its ID
    public Cursor getAuctionItemDetails(long auctionItemId) {
        String selection = SQLHelper.COLUMN_AUCTION_ITEM_ID + " = ?";
        String[] selectionArgs = { String.valueOf(auctionItemId) };

        return database.query(
                SQLHelper.TABLE_AUCTION_ITEMS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    //Get Auction Item With Media Converted To be Displayed and in a list
    public List<AuctionItem> getAllAuctionItemsWithMedia() {
        List<AuctionItem> items = new ArrayList<>();
        Cursor cursor = database.query(
                SQLHelper.TABLE_AUCTION_ITEMS,
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
                AuctionItem item = cursorToAuctionItemWithMedia(cursor);
                items.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return items;
    }

    @SuppressLint("Range")
    private AuctionItem cursorToAuctionItemWithMedia(Cursor cursor) {
        AuctionItem item = new AuctionItem();
        item.setId(cursor.getLong(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_ID)));
        item.setTitle(cursor.getString(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_TITLE)));
        item.setDescription(cursor.getString(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_DESCRIPTION)));
        item.setStartBid(cursor.getDouble(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_START_BID)));
        item.setLocation(cursor.getString(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_LOCATION)));
        item.setCurrentBid(cursor.getDouble(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_CURRENT_BID)));
        item.setBids(cursor.getInt(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_BIDS)));

        // Retrieve image and video data from BLOB columns
        byte[] imageData = cursor.getBlob(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_IMAGE));
        byte[] videoData = cursor.getBlob(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_VIDEO));

        // Convert BLOB data to appropriate format
        item.setImage(getBitmapFromBlob(imageData));
        item.setVideo(getVideoBytesFromBlob(videoData));

        // Calculate and set the current bid
        long auctionItemId = cursor.getLong(cursor.getColumnIndex(SQLHelper.COLUMN_AUCTION_ITEM_ID));
        double currentBid = calculateCurrentBid(auctionItemId);
        item.setCurrentBid(currentBid);

        return item;
    }

    //Adding Auction item to Database
    public long addAuctionItem(AuctionItem auctionItem) {
        ContentValues values = new ContentValues();
        values.put(SQLHelper.COLUMN_AUCTION_ITEM_TITLE, auctionItem.getTitle());
        values.put(SQLHelper.COLUMN_AUCTION_ITEM_DESCRIPTION, auctionItem.getDescription());
        values.put(SQLHelper.COLUMN_AUCTION_ITEM_START_BID, auctionItem.getStartBid());
        values.put(SQLHelper.COLUMN_AUCTION_ITEM_CURRENT_BID, auctionItem.getCurrentBid());
        values.put(SQLHelper.COLUMN_AUCTION_ITEM_BIDS, auctionItem.getBids());

        // Convert image to byte array (BLOB) before storing in the database
        Bitmap imageBitmap = auctionItem.getImage();
        if (imageBitmap != null) {
            byte[] imageBytes = getBytesFromBitmap(imageBitmap);
            values.put(SQLHelper.COLUMN_AUCTION_ITEM_IMAGE, imageBytes);
        }

        // Store video data directly as BLOB
        values.put(SQLHelper.COLUMN_AUCTION_ITEM_VIDEO, auctionItem.getVideo());

        return database.insert(SQLHelper.TABLE_AUCTION_ITEMS, null, values);
    }

    // Helper method to convert Bitmap to byte array
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    //Function for Bids:
    public long addBid(Bid bid) {
        ContentValues values = new ContentValues();
        values.put(SQLHelper.COLUMN_BID_AUCTION_ITEM_ID, bid.getAuctionItemId());
        values.put(SQLHelper.COLUMN_BID_AMOUNT, bid.getBidAmount());
        values.put(SQLHelper.COLUMN_BID_USER, bid.getBiddingUser());
        values.put(SQLHelper.COLUMN_BID_TIME, bid.getBidTime());
        values.put(SQLHelper.COLUMN_BID_DATE, bid.getBidDate());
        return database.insert(SQLHelper.TABLE_BIDS, null, values);
    }
    public Cursor getAllBidsForAuctionItem(long auctionItemId) {
        String selection = SQLHelper.COLUMN_BID_AUCTION_ITEM_ID + " = ?";
        String[] selectionArgs = { String.valueOf(auctionItemId) };

        return database.query(
                SQLHelper.TABLE_BIDS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    public double calculateCurrentBid(long auctionItemId) {
        String selection = SQLHelper.COLUMN_BID_AUCTION_ITEM_ID + " = ?";
        String[] selectionArgs = { String.valueOf(auctionItemId) };

        Cursor cursor = database.query(
                SQLHelper.TABLE_BIDS,
                new String[]{"SUM(" + SQLHelper.COLUMN_BID_AMOUNT + ")"},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        double currentBid = 0;
        if (cursor.moveToFirst()) {
            currentBid = cursor.getDouble(0);
        }

        cursor.close();
        return currentBid;
    }


}
