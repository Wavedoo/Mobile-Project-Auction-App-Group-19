package com.example.auctionapp;

import java.io.Serializable;

public class AuctionUser implements Serializable {
    private String username;
    private String password;

    private long id;

    private String displayName;
    private boolean isAdmin = false;

    //Constructor
    public AuctionUser(String username,String password){
        this.username = username;
        this.password = password;
    }

    public AuctionUser(){
        //default
    }

    //Functions
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
