package com.khanh.expensemanagement.trans_mainte;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.home.TransactionHistory;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.m_name.kbn.SourcePaymentClass;
import com.khanh.expensemanagement.m_name.view.MNameAdapter;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.util.DateTimeUtil;
import com.khanh.expensemanagement.util.FormUtil;
import com.khanh.expensemanagement.util.SqliteUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;

public class TransUpdateActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Edit expense";

    EditText amount;
    EditText m_name_category;
    EditText date;
    EditText m_name_source;
    EditText note;
    Button upd_expense;
    ImageView category_icon;
    ImageView source_icon;
    DatabaseHelper databaseHelper;
    RecyclerView m_name_recycler_view;
    TextView m_name_title;

    Integer categorySelectedId;
    Integer sourceSelectedId;

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

        m_name_category = findViewById(R.id.m_name_category);
        m_name_category.setFocusable(false);
        m_name_category.setClickable(true);
        m_name_category.setOnClickListener(view -> {
            showBottomCategoryDialog(CategoryClass.NAME_IDENT_CD, true);
        });

        category_icon = findViewById(R.id.category_icon);
        category_icon.setVisibility(View.GONE);

        date = findViewById(R.id.date);
        date.setFocusable(false);
        date.setClickable(true);
        date.setOnClickListener(view -> {

            DateTimeUtil.showDatePicker(TransUpdateActivity.this, (EditText) view); // Open date picker dialog
        });

        m_name_source = findViewById(R.id.m_name_source);
        m_name_source.setFocusable(false);
        m_name_source.setClickable(true);
        m_name_source.setOnClickListener(view -> showSourceBottomDialog(SourcePaymentClass.NAME_IDENT_CD, true));

        source_icon = findViewById(R.id.source_icon);
        source_icon.setVisibility(View.GONE);

        note = findViewById(R.id.note);

        upd_expense = findViewById(R.id.upd_expense);
        upd_expense.setOnClickListener(view -> {
            databaseHelper.updateTransaction(transactionId, Integer.valueOf(amount.getText().toString()), note.getText().toString(), categorySelectedId, date.getText().toString(), sourceSelectedId);
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

        String categoryIconName = "";
        String sourceIconName = "";
        int imageResId;

        // Get by id
        Cursor cursor = databaseHelper.transactionFindById(transactionId);
        TransactionHistory transactionHistory = new TransactionHistory();
        if (cursor != null && cursor.moveToFirst()) {

            transactionHistory.setAmount(cursor.getInt(1));
            transactionHistory.setNote(cursor.getString(2));
            transactionHistory.setSourceName(cursor.getString(5));
            transactionHistory.setDate(cursor.getString(4));
            transactionHistory.setCategoryTitle(cursor.getString(3));
            transactionHistory.setTransactionId(cursor.getInt(0));
            transactionHistory.setUpdDttm(cursor.getString(7));
        }

        // get m name category
        Cursor cursorCategory = databaseHelper.mNameSelectByUk1(CategoryClass.NAME_IDENT_CD, FormUtil.fncNS(transactionHistory.getCategoryTitle()));
        if (cursorCategory != null && cursorCategory.moveToFirst()) {

            categorySelectedId = cursorCategory.getInt(1);
            transactionHistory.setCategoryTitle(cursorCategory.getString(3));
            categoryIconName = FormUtil.fncNS(cursorCategory.getString(4));
        }

        // Get m name source
        Cursor cursorSource = databaseHelper.mNameSelectByUk1(SourcePaymentClass.NAME_IDENT_CD, FormUtil.fncNS(transactionHistory.getSourceName()));

        if (cursorSource != null && cursorSource.moveToFirst()) {

            sourceSelectedId = cursorSource.getInt(1);
            transactionHistory.setSourceName(cursorSource.getString(3));
            sourceIconName = FormUtil.fncNS(cursorSource.getString(4));
        }

        // Set view data
        amount.setText(String.valueOf(transactionHistory.getAmount()));
        m_name_category.setText(transactionHistory.getCategoryTitle());
        imageResId = this.getResources().getIdentifier(categoryIconName, "drawable", this.getPackageName());
        if (imageResId != 0) {

            category_icon.setVisibility(View.VISIBLE);
            category_icon.setImageResource(imageResId);
        } else {

            category_icon.setVisibility(View.GONE);
        }
        date.setText(transactionHistory.getDate());
        m_name_source.setText(transactionHistory.getSourceName());
        imageResId = this.getResources().getIdentifier(sourceIconName, "drawable", this.getPackageName());
        if (imageResId != 0) {

            source_icon.setVisibility(View.VISIBLE);
            source_icon.setImageResource(imageResId);
        } else {

            source_icon.setVisibility(View.GONE);
        }
        note.setText(transactionHistory.getNote());

        //Release cursor
        SqliteUtil.releaseCursor(cursor);
        SqliteUtil.releaseCursor(cursorCategory);
        SqliteUtil.releaseCursor(cursorSource);
    }

    private void initTextWatcher() {

        String amountText = amount.getText().toString().trim();
        String categoryText = m_name_category.getText().toString();
        String dateText = date.getText().toString().trim();
        String sourceText = m_name_source.getText().toString();
        upd_expense.setEnabled(!amountText.isEmpty() && !categoryText.isEmpty() && !dateText.isEmpty() && !sourceText.isEmpty());

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
        String categoryText = m_name_category.getText().toString();
        String dateText = date.getText().toString().trim();
        String sourceText = m_name_source.getText().toString();

        upd_expense.setEnabled(!amountText.isEmpty() && !categoryText.isEmpty() && !dateText.isEmpty() && !sourceText.isEmpty());
    }

    private void showBottomCategoryDialog(String nameIdentCd, Boolean enableCellIcon) {

        final Dialog dialog = new Dialog(TransUpdateActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.m_name_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // add data dialog
        m_name_recycler_view = dialog.findViewById(R.id.m_name_recycler_view);
        m_name_title = dialog.findViewById(R.id.m_name_title);
        m_name_title.setText("カテゴリー");

        MNameAdapter mNameAdapter = new MNameAdapter(TransUpdateActivity.this, TransUpdateActivity.this, nameIdentCd, enableCellIcon, (position, mName, view) -> {
            Toast.makeText(TransUpdateActivity.this, "Clicked: " + mName.getNameCd(), Toast.LENGTH_SHORT).show();
            m_name_category.setText(mName.getNameSs());
            categorySelectedId = Integer.valueOf(mName.getNameCd());
            // Enable icon
            category_icon.setVisibility(View.VISIBLE);
            if (mName.getDrawableIconUrl() != null) {

                int imageResId = this.getResources().getIdentifier(mName.getDrawableIconUrl(), "drawable", this.getPackageName());
                if (imageResId != 0) {

                    category_icon.setImageResource(imageResId);
                }
            } else {

                category_icon.setImageResource(R.drawable.ic_no_image);
            }
            dialog.dismiss(); // close dialog
        });

        m_name_recycler_view.setAdapter(mNameAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TransUpdateActivity.this, 4);
        m_name_recycler_view.setLayoutManager(layoutManager);

        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showSourceBottomDialog(String nameIdentCd, Boolean enableCellIcon) {

        final Dialog dialog = new Dialog(TransUpdateActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.m_name_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // add data dialog
        m_name_recycler_view = dialog.findViewById(R.id.m_name_recycler_view);
        m_name_title = dialog.findViewById(R.id.m_name_title);
        m_name_title.setText("支払い方");

        MNameAdapter mNameAdapter = new MNameAdapter(TransUpdateActivity.this, TransUpdateActivity.this, nameIdentCd, enableCellIcon, (position, mName, view) -> {
            Toast.makeText(TransUpdateActivity.this, "Clicked: " + mName.getNameCd(), Toast.LENGTH_SHORT).show();
            m_name_source.setText(mName.getNameSs());
            sourceSelectedId = Integer.valueOf(mName.getNameCd());
            // Enable icon
            source_icon.setVisibility(View.VISIBLE);
            if (mName.getDrawableIconUrl() != null) {

                int imageResId = this.getResources().getIdentifier(mName.getDrawableIconUrl(), "drawable", this.getPackageName());
                if (imageResId != 0) {

                    source_icon.setImageResource(imageResId);
                }
            } else {

                source_icon.setImageResource(R.drawable.ic_no_image);
            }
            dialog.dismiss(); // close dialog
        });

        m_name_recycler_view.setAdapter(mNameAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TransUpdateActivity.this, 4);
        m_name_recycler_view.setLayoutManager(layoutManager);

        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}