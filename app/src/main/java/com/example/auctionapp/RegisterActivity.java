package com.example.auctionapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends FragmentActivity  {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        registerButton = findViewById(R.id.registerButton);
    }
    public void handleRegister(View v){
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
        if(!userExists) {
            //Insert user into database
            long auctionUserId = dataSource.signUpNewUser(newAuctionUser);
            //Make a toast since note created
            Toast.makeText(getApplicationContext(),"Welcome " + usernameText, Toast.LENGTH_SHORT).show();
            dataSource.close(); // Close the database when done
            //Transfer the user to Main Page
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("user_object", newAuctionUser);
            startActivity(intent);
        }
        else{
            handleHasAccount();
        }
    }

    public void handleHasAccount(){
        Toast.makeText(getApplicationContext(),"Username already exists, please choose a different username", Toast.LENGTH_SHORT).show();
    }

/*    public void onChanged(View v){
        if(usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null && confirmPasswordEditText.getText().toString() != null){
            registerButton.setEnabled(true);
        }else{
            registerButton.setEnabled(false);
        }
    }*/
}