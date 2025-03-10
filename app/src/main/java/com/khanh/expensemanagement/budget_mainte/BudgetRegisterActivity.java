package com.khanh.expensemanagement.budget_mainte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import com.khanh.expensemanagement.BaseActivity;
import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.util.SqliteUtil;

public class BudgetRegisterActivity extends BaseActivity {

    private final String ACTIVITY_TITLE = "Create budget";

    EditText limit_amount;
    Button create_budget_btn;
    DatabaseHelper databaseHelper;
    Integer categoryId;
    ImageView category_icon;
    TextView category_name_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget_register);
        ActivityUtil.enableActionBar(this, ACTIVITY_TITLE);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        getIntentData();
        getDataDisplay();
        initTextWatcher();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back to budget
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {

        category_icon = findViewById(R.id.category_icon);
        category_name_tv = findViewById(R.id.category_name_tv);
        limit_amount = findViewById(R.id.limit_amount);
        limit_amount.requestFocus();
        create_budget_btn = findViewById(R.id.create_budget_btn);
        create_budget_btn.setOnClickListener(view -> {

            if (categoryId != -1) {

                databaseHelper.registerBudget(Integer.valueOf(limit_amount.getText().toString()), categoryId);
                // Back to home
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("onStartFragmentName", "BudgetFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getIntentData() {

        Intent intent = getIntent();
        categoryId = intent.getIntExtra("categoryIdSelected", -1);
    }

    private void initTextWatcher() {

        create_budget_btn.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataErrorCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        limit_amount.addTextChangedListener(textWatcher);
    }

    private void getDataDisplay() {

        int imageResId = 0;

        switch (categoryId) {
            case -99: {
                // create budget total on month

                imageResId = getResources().getIdentifier("ic_bill", "drawable", getPackageName());
                if (imageResId != 0) {

                    category_icon.setImageResource(imageResId);
                }
                category_name_tv.setText("Total spending month");
                break;
            }
            default: {
                // create budget on specific category
                Cursor cursor = databaseHelper.mNameSelectByUk1(CategoryClass.NAME_IDENT_CD, categoryId.toString());

                if (cursor != null && cursor.moveToFirst()) {

                    // display category icon
                    if (cursor.getString(4) != null && !cursor.getString(4).isEmpty()) {

                        imageResId = getResources().getIdentifier(cursor.getString(4), "drawable", getPackageName());
                        if (imageResId != 0) {

                            category_icon.setImageResource(imageResId);
                        }
                    }

                    // display category name
                    if (!cursor.getString(3).isEmpty()) {

                        category_name_tv.setText(cursor.getString(3));
                    }

                    SqliteUtil.releaseCursor(cursor);
                }
            }
        }


    }

    private void dataErrorCheck() {

        String limitAmountText = limit_amount.getText().toString().trim();

        create_budget_btn.setEnabled(!limitAmountText.isEmpty());
    }
}