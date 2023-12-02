package com.example.auctionapp;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AuctionItemAdapter extends RecyclerView.Adapter<AuctionItemAdapter.ViewHolder> {

    //Variable to hold all Auctions items
    private List<AuctionItem> auctionItemList;

    public AuctionItemAdapter(List<AuctionItem> auctionItemList) {
        this.auctionItemList = auctionItemList;
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
        holder.currentBidTextView.setText("Current Bid: $" + auctionItem.getCurrentBid());
        holder.locationTextView.setText(auctionItem.getLocation());
        holder.numBidsTextView.setText(("Bids: "+auctionItem.getBids()));
        holder.imageView.setImageBitmap(auctionItem.getImage());

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
        }
        private void openBidModal() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            View bidModalView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.modal, null);

            // Find views in bidModalView
            EditText editTextBidAmount = bidModalView.findViewById(R.id.editBidAmount);
            Button btnSubmitBid = bidModalView.findViewById(R.id.btnConfirmBid);

            btnSubmitBid.setOnClickListener(v -> {
                // Get bid amount from the EditText
                String bidAmountStr = editTextBidAmount.getText().toString();
                if (!TextUtils.isEmpty(bidAmountStr)) {
                    double bidAmount = Double.parseDouble(bidAmountStr);

                    // TODO: Send bid data to the database
                    // You can use an interface or callback to communicate with the activity/fragment

                    //Re render list with updated data
                    notifyDataSetChanged();
                }
                // Dismiss the dialog
                builder.create().dismiss();
            });

            builder.setView(bidModalView);
            builder.show();
        }
    }
}

