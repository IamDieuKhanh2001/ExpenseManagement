package com.khanh.expensemanagement.budget_mainte;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;

public class BudgetRegisterActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Create budget";

    EditText limit_amount;
    Button create_budget_btn;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget_register);
        ActivityUtil.enableActionBar(this, ACTIVITY_TITLE);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        initTextWatcher();
    }

    private void initWidgets() {

        limit_amount = findViewById(R.id.limit_amount);
        create_budget_btn = findViewById(R.id.create_budget_btn);
    }

    private void initTextWatcher() {


    }
}