package com.example.ilovezappos;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    // All the TextViews holding the information
    private TextView highPriceValue;
    private TextView lowPriceValue;
    private TextView lastBtcValue;
    private TextView firstBtcValue;
    private TextView highBuyValue;
    private TextView lowSellValue;
    private TextView vwapValue;
    private TextView volumeValue;
    private TextView notificationValue;
    private TextView lastUpdateDate;


    // Retrofit object
    private Retrofit retrofit;
    private BitStampAPI bitStampAPI;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find all the TextView with findViewByID
        highPriceValue = view.findViewById(R.id.high_price_value);
        lowPriceValue = view.findViewById(R.id.low_price_value);
        lastBtcValue = view.findViewById(R.id.last_btc_value);
        firstBtcValue = view.findViewById(R.id.first_price_value);
        highBuyValue = view.findViewById(R.id.high_buy_value);
        lowSellValue = view.findViewById(R.id.low_sell_value);
        vwapValue = view.findViewById(R.id.vwap_value);
        volumeValue = view.findViewById(R.id.volume_value);
        notificationValue = view.findViewById(R.id.notification_value);
        lastUpdateDate = view.findViewById(R.id.last_update_info);

        // Setup HTTP request handler using Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.bitstamp.net/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bitStampAPI = retrofit.create(BitStampAPI.class);

        getHourlyTicker();

        return view;
    }

    // Wrapper method requesting hourly ticker info and putting those data into CardViews
    private void getHourlyTicker() {
        Call<TickerInfo> tickerResult = bitStampAPI.getHourlyTicker("btcusd");

        tickerResult.enqueue(new Callback<TickerInfo>() {
            @Override
            public void onResponse(Call<TickerInfo> call, Response<TickerInfo> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                TickerInfo tickerInfo = response.body();

                highPriceValue.setText(tickerInfo.getHigh());
                lowPriceValue.setText(tickerInfo.getLow());
                lastBtcValue.setText(tickerInfo.getLast());
                firstBtcValue.setText(tickerInfo.getOpen());
                highBuyValue.setText(tickerInfo.getBid());
                lowSellValue.setText(tickerInfo.getAsk());
                vwapValue.setText(tickerInfo.getVwap());

                //volumeValue.setText(tickerInfo.getVolume());
                // round the volume for a better look
                double volume = tickerInfo.getVolume();
                volume = Math.round(volume * 100.0) / 100.0;
                volumeValue.setText(Double.toString(volume));

                // Get the timestamp into string
                long timestamp = Long.parseLong(tickerInfo.getTimestamp());
                Date date = new Date(timestamp*1000);

                // convert Date object into String
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
                String strDate = df.format(date);

                lastUpdateDate.append(strDate);
            }

            @Override
            public void onFailure(Call<TickerInfo> call, Throwable t) {

            }
        });
    }

}
