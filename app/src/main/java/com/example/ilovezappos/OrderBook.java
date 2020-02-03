package com.example.ilovezappos;

import com.google.gson.annotations.SerializedName;

public class OrderBook {
    // API response items
    //@SerializedName("timestamp")
    private String timestamp;
    //@SerializedName("bids")
    private String[][] bids;
    //@SerializedName("asks")
    private String[][] asks;

    // getter methods
    public String getTimestamp() {
        return timestamp;
    }

    public String[][] getBids() {
        return bids;
    }

    public String[][] getAsks() {
        return asks;
    }
}
