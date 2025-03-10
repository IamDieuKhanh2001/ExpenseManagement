package com.khanh.expensemanagement.trans_mainte;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.khanh.expensemanagement.BaseActivity;
import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.component.CustomSelectBox;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.m_name.kbn.SourcePaymentClass;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.util.DateTimeUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;

public class TransRegisterActivity extends BaseActivity {

    private final String ACTIVITY_TITLE = "Add expense";

    EditText amount;
    CustomSelectBox m_name_category;
    EditText date;
    CustomSelectBox m_name_source;
    EditText note;
    Button add_expense;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trans_register);
        ActivityUtil.enableActionBar(this, ACTIVITY_TITLE);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        initTextWatcher();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back to home
            finish(); // end RegisterTransActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initWidgets() {

        amount = findViewById(R.id.amount);
        m_name_category = findViewById(R.id.m_name_category);
        m_name_category.setNameIdentCd(CategoryClass.NAME_IDENT_CD);
        date = findViewById(R.id.date);
        date.setFocusable(false);
        date.setClickable(true);
        date.setOnClickListener(view -> {

            DateTimeUtil.showDatePicker(TransRegisterActivity.this, (EditText) view); // Open date picker dialog
        });
        m_name_source = findViewById(R.id.m_name_source);
        m_name_source.setNameIdentCd(SourcePaymentClass.NAME_IDENT_CD);
        note = findViewById(R.id.note);

        add_expense = findViewById(R.id.add_expense);
        add_expense.setOnClickListener(view -> {
            databaseHelper.registerTransaction(Integer.valueOf(amount.getText().toString()), note.getText().toString(), m_name_category.getSelectedId(), date.getText().toString(), m_name_source.getSelectedId());
            // Back to home
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initTextWatcher() {

        add_expense.setEnabled(false);

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

        add_expense.setEnabled(!amountText.isEmpty() && categoryId != null && !dateText.isEmpty() && sourceId != null);
    }
}