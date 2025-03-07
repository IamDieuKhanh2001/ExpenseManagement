package com.khanh.expensemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khanh.expensemanagement.budget.BudgetFragment;
import com.khanh.expensemanagement.home.HomeFragment;
import com.khanh.expensemanagement.naiji.NaijiFragment;
import com.khanh.expensemanagement.settings.SettingsFragment;
import com.khanh.expensemanagement.trans_mainte.TransRegisterActivity;
import com.khanh.expensemanagement.util.FragmentUtil;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

//        FragmentUtil.replaceFragment(getSupportFragmentManager(), new HomeFragment());
        FragmentUtil.replaceFragment(getSupportFragmentManager(), new NaijiFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {

                FragmentUtil.replaceFragment(getSupportFragmentManager(), new HomeFragment());
            } else if (item.getItemId() == R.id.budget) {

                FragmentUtil.replaceFragment(getSupportFragmentManager(), new BudgetFragment());
            } else if (item.getItemId() == R.id.subscriptions) {

                FragmentUtil.replaceFragment(getSupportFragmentManager(), new NaijiFragment());
            } else if (item.getItemId() == R.id.settings) {

                FragmentUtil.replaceFragment(getSupportFragmentManager(), new SettingsFragment());
            }

            return true;
        });

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), TransRegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        String onStartfragmentName;
        // get on start fragment on main
        if (getIntent().getStringExtra("onStartFragmentName") != null) {

            onStartfragmentName = getIntent().getStringExtra("onStartFragmentName");
            switch (onStartfragmentName) {

                case "BudgetFragment": {

                    bottomNavigationView.setSelectedItemId(R.id.budget);
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new BudgetFragment());
                    break;
                }
                default: {

                    bottomNavigationView.setSelectedItemId(R.id.home);
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new HomeFragment());
                }
            }
            getIntent().removeExtra("onStartFragmentName");
        }
    }

}