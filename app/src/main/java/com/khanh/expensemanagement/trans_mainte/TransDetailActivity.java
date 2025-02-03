package com.khanh.expensemanagement.trans_mainte;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.db.DatabaseHelper;

public class TransDetailActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Transaction detail";

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trans_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(ACTIVITY_TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back to home
            finish(); // end TransDetailActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {


    }
}