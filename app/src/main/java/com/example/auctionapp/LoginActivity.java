package com.example.auctionapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.registerButton);
    }

    public void handleSignUp(View v) {
        // Handle the click somehow
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    //Function to handle sign in button
    public void handleSignIn(View v){
        Context context = this; //Activity context
        //Open Database to access
        DatabaseServices dataSource = new DatabaseServices(context); // Initialize the data source
        dataSource.open(); // Open the database
        //Get Username and password
        String usernameText = usernameEditText.getText().toString();
        String passwordText= passwordEditText.getText().toString();
        //Create Auction User
        AuctionUser newAuctionUser = new AuctionUser(usernameText,passwordText);
        //Check if account exists in database
        boolean userExists = dataSource.doesAuctionUserExists(newAuctionUser);
        dataSource.close(); // Close the database when done
        if(userExists) {
            //Make a toast since user exists
            Toast.makeText(getApplicationContext(),"Welcome " + usernameText, Toast.LENGTH_SHORT).show();
            //Transfer the user to Main Page
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user_object", newAuctionUser);
            startActivity(intent);
        }
        else{
            //Make a toast since user not exists
            Toast.makeText(getApplicationContext(),"Username does not exist", Toast.LENGTH_SHORT).show();
        }
    }

/*    public void onChanged(View v){
        if(usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null){
            loginButton.setEnabled(true);
        }else{
            loginButton.setEnabled(false);
        }
    }*/
}