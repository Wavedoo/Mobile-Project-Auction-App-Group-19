package com.example.auctionapp;

import android.graphics.Bitmap;

public class AuctionItem {
    private long id;
    private String title;
    private String description;
    private double startBid;
    private double currentBid;
    private int bids;

    //Location variable
    private String location;

    //Image and Video of Item
    private Bitmap image;  // New property for image data
    private byte[] video;


    public AuctionItem() {
        // Default constructor
    }


    public AuctionItem(long id, String title, String description, String location, double startBid, double currentBid, int bids, Bitmap image, byte[] video) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location=location;
        this.startBid = startBid;
        this.currentBid = currentBid;
        this.bids = bids;
        this.image = image;
        this.video = video;
    }

    // Getters and Setters
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
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

    public double getStartBid() {
        return startBid;
    }

    public void setStartBid(double startBid) {
        this.startBid = startBid;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public int getBids() {
        return bids;
    }

    public void setBids(int bids) {
        this.bids = bids;
    }

}

