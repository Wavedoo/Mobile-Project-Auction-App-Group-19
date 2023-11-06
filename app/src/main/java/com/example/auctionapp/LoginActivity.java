package com.example.auctionapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

/*    public void onChanged(View v){
        if(usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null){
            loginButton.setEnabled(true);
        }else{
            loginButton.setEnabled(false);
        }
    }*/
}