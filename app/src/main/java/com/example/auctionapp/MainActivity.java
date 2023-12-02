package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Global User Object
    AuctionUser user;
    private Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get user details from Login...
        Intent intent = getIntent();
        if (intent.hasExtra("user_object")) {
            AuctionUser recievedUser = (AuctionUser) intent.getSerializableExtra("user_object");
            user= recievedUser;
        }
        TextView welcome = findViewById(R.id.textViewToolbarTitle);
        welcome.setText("Welcome " + user.getUsername());


        //Add items to list
        // Assume you have a list of AuctionItems from your database
        List<AuctionItem> auctionItemList = getAuctionItemsFromDatabase();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAuctionItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        adapter = new AuctionItemAdapter(auctionItemList);
        recyclerView.setAdapter(adapter);

    }


    public void handleHomePage(View view) {
        homeButton = findViewById(R.id.homeButton);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}