package com.example.ilovezappos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BitStampAPI {
    @GET("order_book/{currency_pair}/")
    Call<List<OrderBook>> getOrderBooks(@Path("currency_pair") String currency_pair);
}
