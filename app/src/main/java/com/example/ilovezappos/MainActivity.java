package com.example.ilovezappos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // Bottom Navigation Bar
    BottomNavigationView botNavBar;

    // Fragments
    Fragment homeFragment = new HomeFragment();
    Fragment transactionsFragment = new TransactionsFragment();
    Fragment orderBookFragment = new OrderBookFragment();
    FragmentManager fragManager = getSupportFragmentManager();
    Fragment active = homeFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the three main fragments
        fragManager.beginTransaction().add(R.id.main_fragment_layout, transactionsFragment, "transaction")
                .hide(transactionsFragment).commit();
        fragManager.beginTransaction().add(R.id.main_fragment_layout, orderBookFragment, "order")
                .hide(orderBookFragment).commit();
        fragManager.beginTransaction().add(R.id.main_fragment_layout, homeFragment, "home").commit();

        // Bottom Navigation Bar
        botNavBar = findViewById(R.id.bot_nav_bar);
        botNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
