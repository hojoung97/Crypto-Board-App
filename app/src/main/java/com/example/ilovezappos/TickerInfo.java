package com.example.ilovezappos;

public class TickerInfo {
    // API response items
    private String last;
    private String high;
    private String low;
    private String vwap;
    private float volume;
    private String bid;
    private String ask;
    private String timestamp;
    private String open;

    public String getLast() {
        return last;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getVwap() {
        return vwap;
    }

    public float getVolume() {
        return volume;
    }

    public String getBid() {
        return bid;
    }

    public String getAsk() {
        return ask;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getOpen() {
        return open;
    }
}
