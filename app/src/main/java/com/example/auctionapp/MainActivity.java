package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Global User Object
    AuctionUser user;
    private Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.hasExtra("user_object")) {
            AuctionUser recievedUser = (AuctionUser) intent.getSerializableExtra("user_object");
            user= recievedUser;
        }

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome " + user.getUsername());

        TextView openMessage = findViewById(R.id.openingMessage);
        openMessage.setText("What would you like to do today?");
    }


    public void handleHomePage(View view) {
        homeButton = findViewById(R.id.homeButton);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}