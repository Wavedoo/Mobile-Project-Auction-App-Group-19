package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Global User Object
    AuctionUser user;
    private Button homeButton;
    //Variables
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

        //Set Up Bottom Navigation:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        List<AuctionItem> auctionItemList= new ArrayList<>();
        try {
            //Add items to list
            Context context = this; //Activity context
            //Open Database to access
            DatabaseServices dataSource = new DatabaseServices(context); // Initialize the data source
            dataSource.open();
            // Assume you have a list of AuctionItems from your database
            auctionItemList = dataSource.getAllAuctionItemsWithImage();
            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Initialize TextView
        TextView noItemsTextView = findViewById(R.id.noItemsTextView);
        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAuctionItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Check if there are no auction items
        if (auctionItemList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noItemsTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noItemsTextView.setVisibility(View.GONE);
            // Initialize Adapter
            AuctionItemAdapter adapter = new AuctionItemAdapter(MainActivity.this,auctionItemList,user);
            recyclerView.setAdapter(adapter);
        }
    }
    //Logout Function
    public void logOutOnClick(){
        finish();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    //Function for navbar
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            // Do Nothing since already on this page
                            return true;
                        case R.id.action_add:
                            finish();
                            intent = new Intent(MainActivity.this, AddActivity.class);
                            intent.putExtra("user_object", user);
                            startActivity(intent);
                            return true;
                    }
                    return false;
                }
    };
}