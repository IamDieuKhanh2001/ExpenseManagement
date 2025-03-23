package com.khanh.expensemanagement.budget_mainte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import com.khanh.expensemanagement.BaseActivity;
import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.util.SqliteUtil;

public class BudgetUpdateActivity extends BaseActivity {

    private final String ACTIVITY_TITLE = "Update budget";

    EditText limit_amount;
    Button update_budget_btn;
    ImageView category_icon;
    TextView category_name_tv;
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
        getIntentData();
        getDataDisplay();
        initTextWatcher();
    }

    private void initWidgets() {

        category_icon = findViewById(R.id.category_icon);
        category_name_tv = findViewById(R.id.category_name_tv);
        limit_amount = findViewById(R.id.limit_amount);
        update_budget_btn = findViewById(R.id.update_budget_btn);
        update_budget_btn.setOnClickListener(view -> {

            if (categoryId != -1) {

                databaseHelper.updateBudget(Integer.valueOf(limit_amount.getText().toString()), categoryId);
                // Back to home
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("onStartFragmentName","BudgetFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getIntentData() {

        Intent intent = getIntent();
        categoryId = intent.getIntExtra("categoryUpdateId", -1);
    }

    private void getDataDisplay() {

        int imageResId = 0;

        switch (categoryId) {
            case -99: {
                // update budget total on month

                imageResId = getResources().getIdentifier("ic_bill", "drawable", getPackageName());
                if (imageResId != 0) {

                    category_icon.setImageResource(imageResId);
                }
                category_name_tv.setText("Total spending month");
                break;
            }
            default: {
                // update budget on specific category
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

        Cursor cursor = databaseHelper.budgetFindByCategoryId(categoryId);
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