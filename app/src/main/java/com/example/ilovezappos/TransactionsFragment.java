package com.example.ilovezappos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {

    // Bar Chart
    private BarChart transactionChart;

    // API data
    private List<TransactionItem> transactions;

    // Retrofit object
    private Retrofit retrofit;
    private BitStampAPI bitStampAPI;

    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        // Transaction History BarChart
        transactionChart = view.findViewById(R.id.transaction_chart);

        // Setup HTTP request handler using Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.bitstamp.net/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bitStampAPI = retrofit.create(BitStampAPI.class);

        getTransactions();

        return view;
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

}
