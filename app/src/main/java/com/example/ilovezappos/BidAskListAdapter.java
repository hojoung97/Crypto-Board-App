package com.example.ilovezappos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BidAskListAdapter extends RecyclerView.Adapter<BidAskListAdapter.BidAskListViewHolder> {

    private ArrayList<BidAskItem> bidAskData;
    private static final int PRICE = 0;
    private static final int AMOUNT = 1;

    // Here I will make a nested class called BidAskListViewHolder
    // This class will provide reference to the views of each item
    // It will inherit from RecyclerView.ViewHolder
    public static class BidAskListViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public BidAskListViewHolder(View view) {
            super(view);
            itemView = view;
        }
    }

    public BidAskListAdapter(OrderBook orderBook) {
        bidAskData = new ArrayList<>();

        if (orderBook == null) {
            return;
        }

        for(int i = 0; i < orderBook.getAsks().length; i++) {
            bidAskData.add(new BidAskItem(
                    orderBook.getBids()[i][PRICE],
                    orderBook.getBids()[i][AMOUNT],
                    orderBook.getAsks()[i][PRICE],
                    orderBook.getAsks()[i][AMOUNT]
            ));
        }
    }

    // Create new views (this is invoked by layout manager)
    @NonNull
    @Override
    public BidAskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create new view of a single item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        BidAskListViewHolder viewHolder = new BidAskListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BidAskListViewHolder holder, int position) {

        ((TextView)holder.itemView.findViewById(R.id.bid_price)).setText(
                bidAskData.get(position).getBidPrice());
        ((TextView)holder.itemView.findViewById(R.id.bid_amount)).setText(
                bidAskData.get(position).getBidAmount());
        ((TextView)holder.itemView.findViewById(R.id.ask_price)).setText(
                bidAskData.get(position).getAskPrice());
        ((TextView)holder.itemView.findViewById(R.id.ask_amount)).setText(
                bidAskData.get(position).getAskAmount());
    }

    @Override
    public int getItemCount() {
        return bidAskData.size();
    }
}
