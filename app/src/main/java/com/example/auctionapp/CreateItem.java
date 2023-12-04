package com.example.auctionapp;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CreateItem extends AppCompatActivity {
    //Requesting Image
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    //Requesting Video
    private static final int REQUEST_GALLERY_VIDEO = 3;
    private static final int REQUEST_CAMERA_VIDEO = 4;
    //Image view to show image selected
    private Bitmap imageBitmap; // Variable to store image in database
    //User data
    AuctionUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__createitem);
        //Get user details from AddActivity...
        Intent intent = getIntent();
        if (intent.hasExtra("user_object")) {
            AuctionUser recievedUser = (AuctionUser) intent.getSerializableExtra("user_object");
            user= recievedUser;
        }
        //Set Up Bottom Navigation:
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }
    //Button OnClick Functions
    public void chooseImageOnClick(View view){
        // Check and request permissions
        if (checkAndRequestPermissions()) {
            // Permissions granted, show options to choose from gallery or take a picture
            showImageChooser();
        }
    }
    //Function to start the choosing between gallery and Camera
    // Show options to choose from gallery or take a picture
    private void showImageChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create chooser intent with the gallery and camera options
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        // Start the chooser activity
        startActivityForResult(chooserIntent, REQUEST_CAMERA);
    }
    // Handle the result from the image chooser
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                // Handle the result from the gallery
                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        ImageView imageView = findViewById(R.id.imageDisplay);
                        imageView.setImageBitmap(bitmap);
                        imageBitmap = bitmap;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == REQUEST_CAMERA) {
                // Handle the result from the camera
                if (data != null && data.getExtras() != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ImageView imageView = findViewById(R.id.imageDisplay);
                    imageView.setImageBitmap(bitmap);
                    imageBitmap = bitmap;
                }
            }
        }
    }
    //Permission check
    // Check and request  permissions
    // Check and request necessary permissions
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            // Check if the permissions are granted
            if (cameraPermission != PackageManager.PERMISSION_GRANTED ||
                    storagePermission != PackageManager.PERMISSION_GRANTED) {

                // Request the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_GALLERY);
                return false; // Permissions not granted yet
            }
        }
        return true; // Permissions already granted
    }
// Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_GALLERY) {
            // Check if all permissions are granted
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, show options to choose from gallery or take a picture
                showImageChooser();
            }
        }
    }
    //Logout Function
    public void createLogOutOnClick(){
        finish();
        Intent intent = new Intent(CreateItem.this, LoginActivity.class);
        startActivity(intent);
    }
    //Create Auction Item On Click
    public void createItemOnClick(View view){
        try {
            //Get all inputs
            TextInputEditText title = findViewById(R.id.editTextTitle);
            TextInputEditText description = findViewById(R.id.editTextDescription);
            TextInputEditText location = findViewById(R.id.editTextLocation);
            TextInputEditText startBid = findViewById(R.id.editTextStartBid);
            //Create String to store its text
            String titleText= title.getText().toString();
            String descText= description.getText().toString();
            String locText= location.getText().toString();
            String sbText= startBid.getText().toString();
            if(!titleText.isEmpty() && !descText.isEmpty() && !locText.isEmpty() && !sbText.isEmpty() && imageBitmap != null){
                //Open Database and insert values into database
                try {
                    //Add items to list
                    Context context = this; //Activity context
                    //Open Database to access
                    DatabaseServices dataSource = new DatabaseServices(context); // Initialize the data source
                    dataSource.open();
                    byte[] imageBytes = getBytesFromBitmap(imageBitmap);
                    AuctionItem item = new AuctionItem(titleText,descText,user.getUsername(),locText,sbText,imageBytes);
                     long id= dataSource.addAuctionItem(item);
                    //Make a toast that item created
                    Toast.makeText(getApplicationContext(),"Auction Item Created!", Toast.LENGTH_SHORT).show();
                    dataSource.close();
                    finish();
                    startActivity(new Intent(CreateItem.this,AddActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"All Fields must be filled in!", Toast.LENGTH_SHORT).show();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            finish();
                            intent = new Intent(CreateItem.this, MainActivity.class);
                            intent.putExtra("user_object", user);
                            startActivity(intent);
                            return true;
                        case R.id.action_add:
                            finish();
                            intent = new Intent(CreateItem.this, AddActivity.class);
                            intent.putExtra("user_object", user);
                            startActivity(intent);
                            return true;
                    }
                    return false;
                }
            };

}
