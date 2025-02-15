package com.khanh.expensemanagement.budget_mainte;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.budget.BudgetFragment;
import com.khanh.expensemanagement.trans_mainte.TransDetailActivity;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.util.FragmentUtil;

public class BudgetRegisterActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Create budget";

    EditText limit_amount;
    Button create_budget_btn;
    DatabaseHelper databaseHelper;
    Integer categoryId;

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back to budget
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("onStartFragmentName","BudgetFragment");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {

        limit_amount = findViewById(R.id.limit_amount);
        create_budget_btn = findViewById(R.id.create_budget_btn);
        create_budget_btn.setOnClickListener(view -> {

            categoryId = -99;
            databaseHelper.registerBudget(Integer.valueOf(limit_amount.getText().toString()), categoryId);
            // Back to home
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("onStartFragmentName","BudgetFragment");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initTextWatcher() {

        create_budget_btn.setEnabled(false);

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

        create_budget_btn.setEnabled(!limitAmountText.isEmpty());
    }
}