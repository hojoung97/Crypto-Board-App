package com.example.ilovezappos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BidAskListAdapter extends RecyclerView.Adapter<BidAskListAdapter.BidAskListViewHolder> {
    // private .... some type to hold my order book data

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

    public BidAskListAdapter() {

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
