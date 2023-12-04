package com.example.auctionapp;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserAuctionItemAdapter extends RecyclerView.Adapter<UserAuctionItemAdapter.ViewHolder>{
    //Variable to hold all Auctions items
    private List<AuctionItem> auctionItemList;
    private Context context;
    private AuctionUser user;
    public UserAuctionItemAdapter(Context context,List<AuctionItem> auctionItemList, AuctionUser user) {
        this.auctionItemList = auctionItemList;
        this.context=context;
        this.user=user;
    }

    @NonNull
    @Override
    public UserAuctionItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new UserAuctionItemAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserAuctionItemAdapter.ViewHolder holder, int position) {
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
        Button winButton;
        //Image:
        ImageView imageView;
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
            imageView= itemView.findViewById(R.id.imageViewAuctionItem);
            //Find buttons
            winButton = itemView.findViewById(R.id.btnWinner);
            winButton.setOnClickListener(v -> makeWinner());

        }
        // Setter method to set the AuctionItem
        public void setAuctionItem(AuctionItem auctionItem) {
            this.auctionItem = auctionItem;
        }
        public void makeWinner(){
            //The user who has the current bid is the winner of this auction item
            //Get the username of the user where currentBid = bidAmount in Bid table
            try{
                //OPen Database and get winnner
                DatabaseServices db = new DatabaseServices(context);
                db.open();
                String winnerUsername;
                double winningBid= Double.parseDouble(auctionItem.getCurrentBid());
                winnerUsername= db.getWinnerOfAuctionItem(auctionItem.getId(),winningBid);
                //Delete the auction item
                db.deleteAuctionItem(auctionItem.getId());
                //Make Toast
                Toast.makeText(context,winnerUsername+" has won the auction item!", Toast.LENGTH_SHORT).show();
                db.close();
                notifyDataSetChanged();

            }catch (SQLException e) {

            }
        }
    }
}
