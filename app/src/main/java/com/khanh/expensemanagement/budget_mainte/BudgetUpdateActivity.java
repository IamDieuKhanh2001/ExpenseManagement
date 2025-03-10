package com.khanh.expensemanagement.budget_mainte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import com.khanh.expensemanagement.BaseActivity;
import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.util.SqliteUtil;

public class BudgetUpdateActivity extends BaseActivity {

    private final String ACTIVITY_TITLE = "Update budget";

    EditText limit_amount;
    Button update_budget_btn;
    DatabaseHelper databaseHelper;
    Integer categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget_update);
        ActivityUtil.enableActionBar(this, ACTIVITY_TITLE);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        getFormData();
        initTextWatcher();
    }

    private void initWidgets() {

        limit_amount = findViewById(R.id.limit_amount);
        update_budget_btn = findViewById(R.id.update_budget_btn);
        update_budget_btn.setOnClickListener(view -> {

            categoryId = -99;
            databaseHelper.updateBudget(Integer.valueOf(limit_amount.getText().toString()), categoryId);
            // Back to home
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("onStartFragmentName","BudgetFragment");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void getFormData() {

        Cursor cursor = databaseHelper.budgetTotalSpendingMonth();
        Budget budget = new Budget();
        if (cursor != null && cursor.moveToFirst()) {

            budget.setLimitAmount(cursor.getInt(0));
            budget.setCategoryId(cursor.getInt(1));
        }

        limit_amount.setText(String.valueOf(budget.getLimitAmount()));

        //Release cursor
        SqliteUtil.releaseCursor(cursor);
    }

    private void initTextWatcher() {

        update_budget_btn.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataErrorCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        limit_amount.addTextChangedListener(textWatcher);
    }

    private void dataErrorCheck() {

        String limitAmountText = limit_amount.getText().toString().trim();

        update_budget_btn.setEnabled(!limitAmountText.isEmpty());
    }

}