package com.example.ilovezappos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // RecyclerView Components
    private RecyclerView bidAskList;
    private BidAskListAdapter bidAskListAdapter;
    private LinearLayoutManager layoutManager;

    private OrderBook orderBook;

    // error display textview
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // error textview
        errorText = (TextView) findViewById(R.id.network_error_textview);
        errorText.setVisibility(View.INVISIBLE);

        // Setup HTTP request handler using Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.bitstamp.net/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BitStampAPI bitStampAPI = retrofit.create(BitStampAPI.class);

        // order book information getter from BitStamp API
        Call<OrderBook> orderBookResult = bitStampAPI.getOrderBooks();

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
                orderBook = response.body();

            }

            @Override
            public void onFailure(Call<OrderBook> call, Throwable t) {
                String s = t.getMessage();
                errorText.setText(t.getMessage());
                errorText.setVisibility(View.VISIBLE);
                bidAskList.setVisibility(View.INVISIBLE);
            }
        });

        // initialize RecyclerView components
        bidAskList = (RecyclerView) findViewById(R.id.bids_asks_list);

        bidAskListAdapter = new BidAskListAdapter(orderBook);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        bidAskList.setAdapter(bidAskListAdapter);
        bidAskList.setLayoutManager(layoutManager);

    }
}
