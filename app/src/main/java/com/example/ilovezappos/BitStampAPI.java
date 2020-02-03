package com.example.ilovezappos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BitStampAPI {
    // transaction history
    @GET("transactions/{currency_pair}/")
    Call<List<TransactionItem>> getTransactions(@Path("currency_pair") String currency_pair);

    // order book
    @GET("order_book/{currency_pair}/")
    Call<OrderBook> getOrderBooks(@Path("currency_pair") String currency_pair);

    // hourly ticker
    @GET("ticker_hour/{currency_pair}/")
    Call<TickerInfo> getHourlyTicker(@Path("currency_pair") String currency_pair);
}
