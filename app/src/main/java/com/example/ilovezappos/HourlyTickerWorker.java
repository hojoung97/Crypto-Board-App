package com.example.ilovezappos;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HourlyTickerWorker extends Worker {

    // Retrofit object
    private Retrofit retrofit;
    private BitStampAPI bitStampAPI;

    public HourlyTickerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }

}
