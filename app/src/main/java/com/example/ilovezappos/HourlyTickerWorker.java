package com.example.ilovezappos;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HourlyTickerWorker extends Worker {

    // API handler object
    private Retrofit retrofit;
    private BitStampAPI bitStampAPI;
    public final static String API_RESULT = "result";

    public HourlyTickerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            // Setup HTTP request handler using Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.bitstamp.net/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            bitStampAPI = retrofit.create(BitStampAPI.class);

            Response<TickerInfo> response = bitStampAPI.getHourlyTicker("btcusd").execute();

            if (response.isSuccessful() && response.body() != null) {

                Data output = new Data.Builder()
                        .putString("last", response.body().getLast())
                        .putString("high", response.body().getHigh())
                        .putString("low", response.body().getLow())
                        .putString("vwap", response.body().getVwap())
                        .putFloat("volume", response.body().getVolume())
                        .putString("bid", response.body().getBid())
                        .putString("ask", response.body().getAsk())
                        .putString("timestamp", response.body().getTimestamp())
                        .putString("open", response.body().getOpen())
                        .build();
                return Result.success(output);
            }
            return Result.retry();
        } catch (Throwable e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

}
