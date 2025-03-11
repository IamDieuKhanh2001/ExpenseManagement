package com.khanh.expensemanagement.trans_mainte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.khanh.expensemanagement.BaseActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.component.CustomSelectBox;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.m_name.kbn.SourcePaymentClass;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.util.DateTimeUtil;
import com.khanh.expensemanagement.util.SqliteUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;

public class TransUpdateActivity extends BaseActivity {

    private final String ACTIVITY_TITLE = "Edit expense";

    EditText amount;
    CustomSelectBox m_name_category;
    EditText date;
    CustomSelectBox m_name_source;
    EditText note;
    Button upd_expense;
    DatabaseHelper databaseHelper;

    Integer transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trans_update);
        ActivityUtil.enableActionBar(this, ACTIVITY_TITLE);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        getIntentData();
        getFormData();
        initTextWatcher();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back to detail
            Intent intent = new Intent(this, TransDetailActivity.class);
            intent.putExtra("transactionId", transactionId);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWidgets() {

        amount = findViewById(R.id.amount);
        amount.requestFocus();
        m_name_category = findViewById(R.id.m_name_category);
        m_name_category.setNameIdentCd(CategoryClass.NAME_IDENT_CD);
        date = findViewById(R.id.date);
        date.setFocusable(false);
        date.setClickable(true);
        date.setOnClickListener(view -> {

            DateTimeUtil.showDatePicker(TransUpdateActivity.this, (EditText) view); // Open date picker dialog
        });

        m_name_source = findViewById(R.id.m_name_source);
        m_name_source.setNameIdentCd(SourcePaymentClass.NAME_IDENT_CD);
        note = findViewById(R.id.note);

        upd_expense = findViewById(R.id.upd_expense);
        upd_expense.setOnClickListener(view -> {
            databaseHelper.updateTransaction(transactionId, Integer.valueOf(amount.getText().toString()), note.getText().toString(), m_name_category.getSelectedId(), date.getText().toString(), m_name_source.getSelectedId());
            // Back to detail
            Intent intent = new Intent(this, TransDetailActivity.class);
            intent.putExtra("transactionId", transactionId);
            startActivity(intent);
            finish();
        });
    }

    private void getIntentData() {

        Intent intent = getIntent();
        transactionId = intent.getIntExtra("transactionId", -1);
    }

    private void getFormData() {

        // Get by id
        Cursor cursor = databaseHelper.transactionFindById(transactionId);
        if (cursor != null && cursor.moveToFirst()) {

            // Set view data
            amount.setText(cursor.getString(1));
            m_name_category.setSelectedId(cursor.getInt(3));
            date.setText(cursor.getString(4));
            m_name_source.setSelectedId(cursor.getInt(5));
            note.setText(cursor.getString(2));
        }

        //Release cursor
        SqliteUtil.releaseCursor(cursor);
    }

    private void initTextWatcher() {

        String amountText = amount.getText().toString().trim();
        Integer categoryId = m_name_category.getSelectedId();
        String dateText = date.getText().toString().trim();
        Integer sourceId = m_name_source.getSelectedId();
        upd_expense.setEnabled(!amountText.isEmpty() && categoryId != null && !dateText.isEmpty() && sourceId != null);

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

        amount.addTextChangedListener(textWatcher);
        m_name_category.addTextChangedListener(textWatcher);
        date.addTextChangedListener(textWatcher);
        m_name_source.addTextChangedListener(textWatcher);
    }

    private void dataErrorCheck() {

        String amountText = amount.getText().toString().trim();
        Integer categoryId = m_name_category.getSelectedId();
        String dateText = date.getText().toString().trim();
        Integer sourceId = m_name_source.getSelectedId();

        upd_expense.setEnabled(!amountText.isEmpty() && categoryId != null && !dateText.isEmpty() && sourceId != null);
    }
}