package com.example.auctionapp;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    AuctionUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //Get user details from MainActivity...
        Intent intent = getIntent();
        if (intent.hasExtra("user_object")) {
            AuctionUser recievedUser = (AuctionUser) intent.getSerializableExtra("user_object");
            user= recievedUser;
        }

        //Set Up Bottom Navigation:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.action_add);
        //Get All Auction Items associated with current user
       List<AuctionItem> auctionItemList= new ArrayList<>();
        try {
            //Add items to list
            Context context = this; //Activity context
            //Open Database to access
            DatabaseServices dataSource = new DatabaseServices(context); // Initialize the data source
            dataSource.open();
            // Assume you have a list of AuctionItems from your database
            auctionItemList = dataSource.getAllAuctionItemsWithImageForUser(user.getUsername());
            dataSource.close();
        } catch (SQLException e) {
            Log.e("YourTag", "Error message", e);
        }
        //Initialize TextView
        TextView noItemsTextView = findViewById(R.id.noItemsTextView);
        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUserAuctionItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Check if there are no auction items
        if (auctionItemList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noItemsTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noItemsTextView.setVisibility(View.GONE);
            // Initialize Adapter
            UserAuctionItemAdapter adapter = new UserAuctionItemAdapter(AddActivity.this,auctionItemList,user);
            recyclerView.setAdapter(adapter);
        }
    }
    public void newItemFABOnClick(View view) {
        Intent intent = new Intent(AddActivity.this, CreateItem.class);
        intent.putExtra("user_object", user);
        startActivity(intent);
    }
    //Logout Function
    public void addLogOutOnClick(){
        finish();
        Intent intent = new Intent(AddActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            finish();
                            intent = new Intent(AddActivity.this, MainActivity.class);
                            intent.putExtra("user_object", user);
                            startActivity(intent);
                            return true;
                        case R.id.action_add:
                            //Do nothing on this page
                            return true;
                    }
                    return false;
                }
            };
}
