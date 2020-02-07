package com.example.ilovezappos;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    // notification card
    CardView notificationCard;

    // Retrofit object
    private Retrofit retrofit;
    private BitStampAPI bitStampAPI;

    // Notification Setting Result Flag
    private static final int NOTIFICATION_SET_REQUEST = 0;

    // Notification variables
    String notificationSetValue = null;
    public static int NOTIFICATION_ID = 0;

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

        // notification cardview
        notificationCard = view.findViewById(R.id.notification_card);
        notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent gotoNotificationSettingPage = new Intent(v.getContext(), NotificationActivity.class);
                startActivityForResult(gotoNotificationSettingPage, NOTIFICATION_SET_REQUEST);
            }
        }); // onActivityResult() method will handle the result upon returning to MainActivity

        // Setup HTTP request handler using Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.bitstamp.net/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bitStampAPI = retrofit.create(BitStampAPI.class);

        getHourlyTicker();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest hitApi = new PeriodicWorkRequest.Builder(HourlyTickerWorker.class, 1, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(hitApi.getId())
                .observe(getViewLifecycleOwner(), info -> {
                    if (info != null && info.getState().isFinished()) {

                        String last = info.getOutputData().getString("last");
                        String high = info.getOutputData().getString("high");
                        String low = info.getOutputData().getString("low");
                        String vwap = info.getOutputData().getString("vwap");
                        float volume = info.getOutputData().getFloat("volume", 0f);
                        String bid = info.getOutputData().getString("bid");
                        String ask = info.getOutputData().getString("ask");
                        String timestamp = info.getOutputData().getString("timestamp");
                        String open = info.getOutputData().getString("open");

                        // set the result
                        highPriceValue.setText(high);
                        lowPriceValue.setText(low);
                        lastBtcValue.setText(last);
                        firstBtcValue.setText(open);
                        highBuyValue.setText(bid);
                        lowSellValue.setText(ask);
                        vwapValue.setText(vwap);

                        double volumestr = Math.round(volume * 100.0) / 100.0;
                        volumeValue.setText(Double.toString(volumestr));

                        // Get the timestamp into string
                        long timestampLong = Long.parseLong(timestamp);
                        Date date = new Date(timestampLong * 1000);

                        // convert Date object into String
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
                        String strDate = df.format(date);

                        lastUpdateDate.append(strDate);

                        issueNotification(getNotification());

                        if (notificationSetValue != null) {
                            Double notiValueNum = Double.parseDouble(notificationSetValue);
                            Double lastbtcvalue = Double.parseDouble(last);
                            if (notiValueNum < lastbtcvalue) {
                                issueNotification(getNotification());
                            }
                        }
                    }
                });

        return view;
    }

    // Wrapper method requesting hourly ticker info and putting those data into CardViews
    private void getHourlyTicker() {
        final Call<TickerInfo> tickerResult = bitStampAPI.getHourlyTicker("btcusd");

        tickerResult.enqueue(new Callback<TickerInfo>() {
            @Override
            public void onResponse(Call<TickerInfo> call, Response<TickerInfo> response) {
                if (!response.isSuccessful()) {
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
                Date date = new Date(timestamp * 1000);

                // convert Date object into String
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
                String strDate = df.format(date);

                lastUpdateDate.append(strDate);

                issueNotification(getNotification());

                if (notificationSetValue != null) {
                    Double notiValueNum = Double.parseDouble(notificationSetValue);
                    Double lastbtcvalue = Double.parseDouble(tickerInfo.getLast());
                    if (notiValueNum < lastbtcvalue) {
                        issueNotification(getNotification());
                    }
                }

            }

            @Override
            public void onFailure(Call<TickerInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NOTIFICATION_SET_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                notificationSetValue = data.getStringExtra("setting");
                notificationValue.setText(notificationSetValue);
            }
        }
    }

    // Notification Related Methods
    private Notification getNotification() {
        Notification.Builder builder;

        // checking for Android OS version
        // when working with Android 8.0 (API level 26) or higher, must implement at least one
        // notification channels
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getView().getContext(), "my_channel_01");
        } else {
            builder = new Notification.Builder(getView().getContext());
        }
        builder.setContentTitle("ILoveZappos");
        builder.setContentText("The price has fallen below!");
        builder.setSmallIcon(R.drawable.zappos_logo);
        return builder.build();
    }

    private void issueNotification(Notification notification) {

        NotificationManager notificationManager =
                (NotificationManager) getView().getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "price_alert_id";
            CharSequence name = "price_alert_channel";
            String Description = "price alert notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        // issue the notification
        notificationManager.notify(NOTIFICATION_ID++, notification);

    }
}
