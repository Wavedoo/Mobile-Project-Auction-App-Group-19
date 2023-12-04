package com.example.auctionapp;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AuctionItemAdapter extends RecyclerView.Adapter<AuctionItemAdapter.ViewHolder> {

    //Variable to hold all Auctions items
    private List<AuctionItem> auctionItemList;
    private Context context;
    private AuctionUser user;
    public AuctionItemAdapter(Context context,List<AuctionItem> auctionItemList, AuctionUser user) {
        this.auctionItemList = auctionItemList;
        this.context=context;
        this.user=user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get Auction Item Data Object
        AuctionItem auctionItem = auctionItemList.get(position);
        // Bind Data to the Layout
        holder.titleTextView.setText(auctionItem.getTitle());
        holder.descriptionTextView.setText(auctionItem.getDescription());
        holder.locationTextView.setText(auctionItem.getLocation());
        holder.numBidsTextView.setText(("Bids: "+auctionItem.getBids()));
        byte[] byteArray= auctionItem.getImage();
        Bitmap imageBitmap= BitmapFactory.decodeByteArray(auctionItem.getImage(), 0, byteArray.length);
        holder.imageView.setImageBitmap(imageBitmap);
        // Set the AuctionItem for the ViewHolder
        holder.setAuctionItem(auctionItem);
        if(auctionItem.getBids().equals("0")){
            holder.currentBidTextView.setText("Staring Bid: $" + auctionItem.getStartBid());
        }
        else{
            holder.currentBidTextView.setText("Current Bid: $" + auctionItem.getCurrentBid());
        }
    }

    @Override
    public int getItemCount() {
        return auctionItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Text Views:
        TextView titleTextView;
        TextView descriptionTextView;
        TextView currentBidTextView;
        TextView locationTextView;
        TextView numBidsTextView;
        //Buttons
        Button bidButton;
        //Image:
        ImageView imageView;
        ImageView logoLocation;
        // AuctionItem
        private AuctionItem auctionItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Find textviews:
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            currentBidTextView = itemView.findViewById(R.id.textViewCurrentBid);
            locationTextView = itemView.findViewById(R.id.textViewLocation);
            numBidsTextView = itemView.findViewById(R.id.textViewNumBids);
            //Find buttons
            bidButton = itemView.findViewById(R.id.btnBid);
            //-> Functions for button
            bidButton.setOnClickListener(v -> openBidModal());
            //Find Imageview
            imageView = itemView.findViewById(R.id.imageViewAuctionItem);
            //Set on Click Function for the Location Icon
            logoLocation = itemView.findViewById(R.id.imageViewLocation);
            logoLocation.setOnClickListener(v -> openGoogleMapsDialog());

        }
        // Setter method to set the AuctionItem
        public void setAuctionItem(AuctionItem auctionItem) {
            this.auctionItem = auctionItem;
        }
        private void openBidModal() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            View bidModalView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.modal, null);
            // Find views in bidModalView
            EditText editTextBidAmount = bidModalView.findViewById(R.id.editBidAmount);
            TextView error = bidModalView.findViewById(R.id.textViewError);
            Button btnSubmitBid = bidModalView.findViewById(R.id.btnConfirmBid);
            Button btnCancel= bidModalView.findViewById(R.id.btnCancelBid);
            //Create Dialog
            builder.setView(bidModalView);
            AlertDialog dialog = builder.create(); // Create the dialog instance
            dialog.show();
            //onClick Functions
            btnSubmitBid.setOnClickListener(v -> {
                // Get bid amount from the EditText
                String bidAmountStr = editTextBidAmount.getText().toString();
                if (!bidAmountStr.isEmpty()) {
                    double bidAmount = Double.parseDouble(bidAmountStr);
                    double currentBid =Double.parseDouble(auctionItem.getCurrentBid());
                    // Compare bid amount with the current bid
                    if (bidAmount > currentBid) {

                        //Add bid to database and update the current bid of the auction item
                        try{
                            DatabaseServices db = new DatabaseServices(context);
                            db.open();
                            // Get Time and date now
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            // Format the date using a date formatter
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String formattedDate = currentDateTime.format(dateFormatter);
                            // Format the time using a time formatter
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            String formattedTime = currentDateTime.format(timeFormatter);
                            //Create Bid Object
                            Bid bid = new Bid(auctionItem.getId(),bidAmount,user.getUsername(),formattedTime,formattedDate);
                            db.addBid(bid);
                            //Update Auction Item with new details
                            AuctionItem updateItem = auctionItem;
                            int bids= Integer.parseInt(auctionItem.getBids());
                            bids+=1;
                            updateItem.setBids(bids+"");
                            //-> update current bid
                            updateItem.setCurrentBid(bidAmountStr);
                            //Update the item in db
                            long id = db.updateAuctionItem(updateItem);
                            db.close();
                            //Make a toast that Bid Created
                            Toast.makeText(context,"Bid has been placed!", Toast.LENGTH_SHORT).show();

                        }catch(SQLException e){
                            Log.e("YourTag", "Error message", e);
                        }
                        //Re render list with updated data
                        notifyDataSetChanged();
                        // Dismiss the dialog
                        dialog.dismiss();
                    } else {
                        // Show an error message
                        error.setText("Bid must be greater than the current bid.");
                        error.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Show an error message for empty bid amount
                    error.setText("Please enter a bid amount.");
                    error.setVisibility(View.VISIBLE);
                }
            });
            btnCancel.setOnClickListener(v -> {
                // Dismiss the dialog
                dialog.dismiss();
            });


        }
        private void openGoogleMapsDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            View mapModalView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.mapmodal, null);
            Button btnClose = mapModalView.findViewById(R.id.btnClose);
            // Get the location from the TextView
            String location = locationTextView.getText().toString();
            // Find the MapView
            com.google.android.gms.maps.MapView mapView = mapModalView.findViewById(R.id.mapView);
            mapView.onCreate(new Bundle());
            mapView.getMapAsync(googleMap -> {
                // Add a marker for the specified location
                LatLng latLng = getLocationFromAddress(itemView.getContext(), location);
                if (latLng != null) {
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            });
            builder.setView(mapModalView);
            //builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            btnClose.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }
        private LatLng getLocationFromAddress(Context context, String strAddress) {
            Geocoder coder = new Geocoder(context);
            List<Address> address;
            LatLng p1 = null;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return p1;
        }
    }
}

