package com.example.ilovezappos;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderBookFragment extends Fragment {

    // RecyclerView Components
    private RecyclerView bidAskList;
    private BidAskListAdapter bidAskListAdapter;
    private LinearLayoutManager layoutManager;

    // error display textview
    private TextView errorText;

    // Retrofit object
    private Retrofit retrofit;
    private BitStampAPI bitStampAPI;

    public OrderBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_book, container, false);

        // error textview
        errorText = view.findViewById(R.id.network_error_textview);
        errorText.setVisibility(View.INVISIBLE);

        // Setup HTTP request handler using Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.bitstamp.net/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bitStampAPI = retrofit.create(BitStampAPI.class);

        getOrderBookData(view);

        return view;
    }

    // Wrapper method requesting order book data and putting those data into a RecyclerView
    private void getOrderBookData(final View view) {
        // order book information getter from BitStamp API
        Call<OrderBook> orderBookResult = bitStampAPI.getOrderBooks("btcusd");

        // start network request
        orderBookResult.enqueue(new Callback<OrderBook>() {
            @Override
            public void onResponse(Call<OrderBook> call, Response<OrderBook> response) {
                if(!response.isSuccessful()) {
                    errorText.setText("Code: " + response.code());
                    errorText.setVisibility(View.VISIBLE);
                    bidAskList.setVisibility(View.INVISIBLE);
                    return;
                }
                // initialize RecyclerView components
                bidAskList = view.findViewById(R.id.bids_asks_list);

                bidAskListAdapter = new BidAskListAdapter(response.body());
                layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

                bidAskList.setAdapter(bidAskListAdapter);
                bidAskList.setLayoutManager(layoutManager);
            }

            @Override
            public void onFailure(Call<OrderBook> call, Throwable t) {
                errorText.setText(t.getMessage());
                errorText.setVisibility(View.VISIBLE);
                bidAskList.setVisibility(View.INVISIBLE);
            }
        });
    }

}
