package com.example.auctionapp;

public class Bid {
    private long id;
    private long auctionItemId;
    private double bidAmount;
    private String biddingUser;
    private String bidTime;
    private String bidDate;

    public Bid() {
        // Default constructor
    }

    public Bid(long auctionItemId, double bidAmount, String biddingUser, String bidTime, String bidDate) {
        this.auctionItemId = auctionItemId;
        this.bidAmount = bidAmount;
        this.biddingUser = biddingUser;
        this.bidTime = bidTime;
        this.bidDate = bidDate;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuctionItemId() {
        return auctionItemId;
    }

    public void setAuctionItemId(long auctionItemId) {
        this.auctionItemId = auctionItemId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBiddingUser() {
        return biddingUser;
    }

    public void setBiddingUser(String biddingUser) {
        this.biddingUser = biddingUser;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }
}
