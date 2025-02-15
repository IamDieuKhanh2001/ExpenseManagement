package com.khanh.expensemanagement.trans_mainte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.home.TransactionHistory;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.m_name.kbn.SourcePaymentClass;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.util.FormUtil;
import com.khanh.expensemanagement.util.SqliteUtil;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;

public class TransDetailActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Transaction detail";

    DatabaseHelper databaseHelper;
    TextView amount_tv;
    TextView note_tv;
    TextView source_tv;
    ImageView source_icon;
    TextView date_tv;
    TextView category_tv;
    ImageView category_icon;
    TextView transaction_id_tv;
    TextView last_upd_dttm_tv;
    Button del_btn;
    Button edit_btn;

    Integer transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trans_detail);
        ActivityUtil.enableActionBar(this, ACTIVITY_TITLE);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        getIntentData();
        getDataDetail();
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

        amount_tv = findViewById(R.id.amount_tv);
        note_tv = findViewById(R.id.note_tv);
        note_tv.setVisibility(View.GONE);
        source_tv = findViewById(R.id.source_tv);
        source_icon = findViewById(R.id.source_icon);
        date_tv = findViewById(R.id.date_tv);
        category_tv = findViewById(R.id.category_tv);
        category_icon = findViewById(R.id.category_icon);
        transaction_id_tv = findViewById(R.id.transaction_id_tv);
        last_upd_dttm_tv = findViewById(R.id.last_upd_dttm_tv);
        del_btn = findViewById(R.id.del_btn);
        del_btn.setOnClickListener(view -> {

            String dialogTitle = "Delete expense";
            String dialogMessage = "Deleted transactions can not be recovered";
            FormUtil.openConfirmDialog(this, dialogTitle, dialogMessage, () -> {

                databaseHelper.deleteTransactionById(transactionId);
                finish();
            });
        });
        edit_btn = findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), TransUpdateActivity.class);
            intent.putExtra("transactionId", transactionId);
            startActivity(intent);
            finish();
        });
    }

    private void getIntentData() {

        Intent intent = getIntent();
        transactionId = intent.getIntExtra("transactionId", -1);
    }

    private void getDataDetail() {

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

            transactionHistory.setCategoryTitle(cursorCategory.getString(3));
            categoryIconName = FormUtil.fncNS(cursorCategory.getString(4));
        }

        // Get m name source
        Cursor cursorSource = databaseHelper.mNameSelectByUk1(SourcePaymentClass.NAME_IDENT_CD, FormUtil.fncNS(transactionHistory.getSourceName()));

        if (cursorSource != null && cursorSource.moveToFirst()) {

            transactionHistory.setSourceName(cursorSource.getString(3));
            sourceIconName = FormUtil.fncNS(cursorSource.getString(4));
        }

        // Set view data
        amount_tv.setText(getString(R.string.transaction_amount_currency, transactionHistory.getAmount().toString()));
        if (!transactionHistory.getNote().isEmpty()) {

            note_tv.setText(transactionHistory.getNote());
            note_tv.setVisibility(View.VISIBLE);
        }
        imageResId = this.getResources().getIdentifier(sourceIconName, "drawable", this.getPackageName());
        if (imageResId != 0) {

            source_icon.setVisibility(View.VISIBLE);
            source_icon.setImageResource(imageResId);
        } else {

            source_icon.setVisibility(View.GONE);
        }
        source_tv.setText(transactionHistory.getSourceName());
        date_tv.setText(transactionHistory.getDate());
        imageResId = this.getResources().getIdentifier(categoryIconName, "drawable", this.getPackageName());
        if (imageResId != 0) {

            category_icon.setVisibility(View.VISIBLE);
            category_icon.setImageResource(imageResId);
        } else {

            category_icon.setVisibility(View.GONE);
        }
        category_tv.setText(transactionHistory.getCategoryTitle());
        transaction_id_tv.setText(transactionHistory.getTransactionId().toString());
        last_upd_dttm_tv.setText(transactionHistory.getUpdDttm());

        //Release cursor
        SqliteUtil.releaseCursor(cursor);
        SqliteUtil.releaseCursor(cursorCategory);
        SqliteUtil.releaseCursor(cursorSource);
    }
}