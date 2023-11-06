package com.example.auctionapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends FragmentActivity {
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
        //TODO: Handle registering user to database
        finish();
    }

    public void handleHasAccount(View v){
        finish();
    }

/*    public void onChanged(View v){
        if(usernameEditText.getText().toString() != null && passwordEditText.getText().toString() != null && confirmPasswordEditText.getText().toString() != null){
            registerButton.setEnabled(true);
        }else{
            registerButton.setEnabled(false);
        }
    }*/
}