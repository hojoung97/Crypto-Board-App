package com.example.ilovezappos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Bottom Navigation Bar
    BottomNavigationView botNavBar;

    // Fragments
    final Fragment homeFragment = new HomeFragment();
    final Fragment transactionsFragment = new TransactionsFragment();
    final Fragment orderBookFragment = new OrderBookFragment();
    final FragmentManager fragManager = getSupportFragmentManager();
    Fragment active = homeFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragManager.beginTransaction().add(R.id.main_fragment_layout, transactionsFragment, "0")
                .hide(transactionsFragment).commit();
        fragManager.beginTransaction().add(R.id.main_fragment_layout, orderBookFragment, "2")
                .hide(orderBookFragment).commit();
        fragManager.beginTransaction().add(R.id.main_fragment_layout, homeFragment, "1").commit();

        // Bottom Navigation Bar
        botNavBar = findViewById(R.id.bot_nav_bar);
        botNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.transaction_nav_icon:
                        fragManager.beginTransaction().hide(active).show(transactionsFragment).commit();
                        active = transactionsFragment;
                        return true;
                    case R.id.home_nav_icon:
                        fragManager.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        return true;
                    case R.id.orderbook_nav_icon:
                        fragManager.beginTransaction().hide(active).show(orderBookFragment).commit();
                        active = orderBookFragment;
                        return true;
                }

                return false;
            }
        });

    }

}
