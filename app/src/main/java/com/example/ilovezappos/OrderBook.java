package com.example.ilovezappos;

public class OrderBook {
    // API response items
    private String timestamp;
    private String[][] bids;
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
