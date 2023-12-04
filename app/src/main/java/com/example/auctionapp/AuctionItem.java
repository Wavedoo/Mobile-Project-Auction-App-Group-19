package com.example.auctionapp;

import android.graphics.Bitmap;

public class AuctionItem {
    private long id;
    private String title;
    private String description;
    private String createdUser;
    private String startBid;
    private String currentBid;
    private String bids;

    //Location variable
    private String location;

    //Image and Video of Item
    private byte[] image;  // New property for image data
    private byte[] video;


    public AuctionItem() {
        // Default constructor
    }


    public AuctionItem(long id, String title, String description, String createdUser, String location, String startBid, String currentBid, String bids, byte[] image, byte[] video) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdUser=createdUser;
        this.location=location;
        this.startBid = startBid;
        this.currentBid = currentBid;
        this.bids = bids;
        this.image = image;
        this.video = video;
    }
    public AuctionItem(String title, String description, String createdUser, String location, String startBid, byte[] image) {
        this.title = title;
        this.description = description;
        this.createdUser=createdUser;
        this.location=location;
        this.startBid = startBid;
        this.image = image;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    // Getters and Setters
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartBid() {
        return startBid;
    }

    public void setStartBid(String startBid) {
        this.startBid = startBid;
    }

    public String getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(String currentBid) {
        this.currentBid = currentBid;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

}

