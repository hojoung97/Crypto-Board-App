package com.example.ilovezappos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    // API data
    private OrderBook orderBook;
    private List<TransactionItem> transactions;

    // Bar Chart
    BarChart transactionChart;

    // error display textview
    TextView errorText;

    // Retrofit object
    Retrofit retrofit;
    BitStampAPI bitStampAPI;

    // Bottom Navigation Bar
    BottomNavigationView botNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // error textview
        errorText = (TextView) findViewById(R.id.network_error_textview);
        errorText.setVisibility(View.INVISIBLE);

        // Transaction History BarChart
        transactionChart = findViewById(R.id.transaction_chart);

        // Setup HTTP request handler using Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.bitstamp.net/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bitStampAPI = retrofit.create(BitStampAPI.class);
        getTransactions();
        getOrderBookData();

        // Bottom Navigation Bar
        botNavBar = findViewById(R.id.bot_nav_bar);
        botNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.transaction_nav_icon:
                        break;
                    case R.id.home_nav_icon:
                        break;
                    case R.id.orderbook_nav_icon:
                        break;
                }

                return true;
            }
        });

    }

    // Wrapper method requesting transaction history data
    public void getTransactions() {
        Call<List<TransactionItem>> transactionResult = bitStampAPI.getTransactions("btcusd");

        transactionResult.enqueue(new Callback<List<TransactionItem>>() {
            @Override
            public void onResponse(Call<List<TransactionItem>> call, Response<List<TransactionItem>> response) {
                if(!response.isSuccessful()) {

                    return;
                }

                transactions = response.body();

                // Each BarEntry object is a single y value which is 'amount' value
                ArrayList<BarEntry> entries = new ArrayList<>();

                int numberOfTransactions = transactions.size();

                // Each element of ArrayList is the corresponding x-Axis label
                String[] xData = new String[numberOfTransactions];

                for(int i = 0; i < numberOfTransactions; i++) {
                    // Y-Axis values which are 'Amount' value
                    entries.add(new BarEntry(numberOfTransactions - 1 - i,
                            Float.parseFloat(transactions.get(i).getAmount())));

                    // X-Axis labels which are 'date' value
                    long timestamp = Long.parseLong(transactions.get(i).getDate());
                    Date date = new Date(timestamp*1000);

                    // convert Date object into String
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
                    String strDate = df.format(date);

                    xData[numberOfTransactions-1] = strDate;
                }

                // Prepare BarDataSet object with all the BarEntry objects
                BarDataSet yData = new BarDataSet(entries, "Amount");

                BarData chartData = new BarData(yData);
                transactionChart.setData(chartData);

                /*
                XAxis xAxis = transactionChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                YAxis yAxis = transactionChart.getAxisLeft();
                yAxis.setAxisMinimum(0f);
                yAxis.setAxisMaximum(3f);

                transactionChart.setVisibleXRangeMaximum(5);

                 */

                transactionChart.invalidate();
                //transactionChart.animateY(5000);

            }

            @Override
            public void onFailure(Call<List<TransactionItem>> call, Throwable t) {

            }
        });
    }

    // Wrapper method requesting order book data and putting those data into a RecyclerView
    public void getOrderBookData() {
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
                bidAskList = (RecyclerView) findViewById(R.id.bids_asks_list);

                bidAskListAdapter = new BidAskListAdapter(response.body());
                layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

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
