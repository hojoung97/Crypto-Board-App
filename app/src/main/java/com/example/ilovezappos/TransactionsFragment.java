package com.example.ilovezappos;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {

    // Bar Chart
    private BarChart transactionChart;

    // Error TextView
    TextView errorText;

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

        // Error textView
        errorText = view.findViewById(R.id.transaction_frag_error_text);
        errorText.setVisibility(View.INVISIBLE);

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
                    errorText.setText("Code: " + response.code());
                    errorText.setVisibility(View.VISIBLE);
                    transactionChart.setVisibility(View.INVISIBLE);
                    return;
                }

                transactions = response.body();

                // Two BarEntry object: Buy transactions and Sell transactions
                ArrayList<BarEntry> buyEntries = new ArrayList<>();
                ArrayList<BarEntry> sellEntries = new ArrayList<>();

                int numberOfTransactions = transactions.size();

                // Extra information such as price, ID, and date are gathered to show on MarkerView
                String[] prices = new String[numberOfTransactions];
                String[] ids = new String[numberOfTransactions];
                String[] dates = new String[numberOfTransactions];


                for(int i = 0; i < numberOfTransactions; i++) {
                    // Buy transactions and Sell transactions are separated out to have different color
                    if (transactions.get(numberOfTransactions-1-i).getType().equals("0")){
                        buyEntries.add(new BarEntry(i,
                                Float.parseFloat(transactions.get(numberOfTransactions-1-i).getAmount())));
                    } else if (transactions.get(numberOfTransactions-1-i).getType().equals("1")){
                        sellEntries.add(new BarEntry(i,
                                Float.parseFloat(transactions.get(numberOfTransactions-1-i).getAmount())));
                    }

                    prices[i] = transactions.get(numberOfTransactions-1-i).getPrice();
                    ids[i] = transactions.get(numberOfTransactions-1-i).getTid();

                    // convert timestamp to date string
                    long timestamp = Long.parseLong(transactions.get(numberOfTransactions-1-i).getDate());
                    Date date = new Date(timestamp*1000);

                    // convert Date object into String
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
                    String strDate = df.format(date);

                    dates[i] = strDate;
                }

                // Custom MarkerView to display ID, price, and transaction date
                CustomMarkerView markerView = new CustomMarkerView(getView().getContext(), R.layout.bar_marker,
                        prices, ids, dates);
                markerView.setOffset(0, 0);

                // Prepare BarDataSet object with all the BarEntry objects
                BarDataSet buyData = new BarDataSet(buyEntries, "Buy");
                buyData.setColor(Color.GREEN);
                buyData.setValueTextSize(12);
                BarDataSet sellData = new BarDataSet(sellEntries, "Sell");
                sellData.setColor(Color.RED);
                sellData.setValueTextSize(12);

                // chart configurations
                BarData chartData = new BarData(buyData);
                chartData.addDataSet(sellData);
                transactionChart.setMarker(markerView);
                transactionChart.setData(chartData);
                transactionChart.setDrawGridBackground(false);
                transactionChart.setDrawBarShadow(false);
                transactionChart.setDrawValueAboveBar(true);
                transactionChart.setFitBars(true);
                transactionChart.setDrawBorders(false);
                transactionChart.getDescription().setEnabled(false);

                transactionChart.setTouchEnabled(true);
                transactionChart.setDragEnabled(true);
                transactionChart.setScaleEnabled(true);
                transactionChart.setScaleXEnabled(true);
                transactionChart.setScaleYEnabled(true);
                transactionChart.setPinchZoom(true);
                transactionChart.setDoubleTapToZoomEnabled(true);
                transactionChart.setVisibleXRangeMaximum(10);

                transactionChart.invalidate();
                transactionChart.animateY(5000);

            }

            @Override
            public void onFailure(Call<List<TransactionItem>> call, Throwable t) {
                errorText.setText(t.getMessage());
                errorText.setVisibility(View.VISIBLE);
                transactionChart.setVisibility(View.INVISIBLE);
            }
        });
    }

}
