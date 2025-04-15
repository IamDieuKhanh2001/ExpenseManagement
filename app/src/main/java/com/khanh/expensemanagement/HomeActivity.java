package com.khanh.expensemanagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khanh.expensemanagement.budget.BudgetFragment;
import com.khanh.expensemanagement.home.HomeFragment;
import com.khanh.expensemanagement.overview.OverviewFragment;
import com.khanh.expensemanagement.settings.SettingsFragment;
import com.khanh.expensemanagement.trans_mainte.TransRegisterActivity;
import com.khanh.expensemanagement.util.FragmentUtil;

public class HomeActivity extends BaseActivity {

    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

        FragmentUtil.replaceFragment(getSupportFragmentManager(), new OverviewFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {

                FragmentUtil.replaceFragment(getSupportFragmentManager(), new HomeFragment());
            } else if (item.getItemId() == R.id.budget) {

                FragmentUtil.replaceFragment(getSupportFragmentManager(), new BudgetFragment());
            } else if (item.getItemId() == R.id.overview) {

                FragmentUtil.replaceFragment(getSupportFragmentManager(), new OverviewFragment());
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