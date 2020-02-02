package com.example.ilovezappos;

public class OrderBook {
    // API response items
    private long timestamp;
    private float[][] bids;
    private float[][] asks;

    // getter methods
    public long getTimestamp() {
        return timestamp;
    }

    public float[][] getBids() {
        return bids;
    }

    public float[][] getAsks() {
        return asks;
    }
}
