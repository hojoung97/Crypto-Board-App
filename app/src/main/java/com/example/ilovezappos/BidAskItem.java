package com.example.ilovezappos;

public class BidAskItem {
    private String bidPrice;
    private String bidAmount;
    private String askPrice;
    private String askAmount;
    
    public BidAskItem(String bidPrice,
                      String bidAmount,
                      String askPrice,
                      String askAmount) {

        this.bidPrice = bidPrice;
        this.bidAmount = bidAmount;
        this.askPrice = askPrice;
        this.askAmount = askAmount;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public String getAskAmount() {
        return askAmount;
    }

    public String getAskPrice() {
        return askPrice;
    }
}
