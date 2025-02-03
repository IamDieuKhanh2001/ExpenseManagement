package com.khanh.expensemanagement.register_trans;

import android.app.Dialog;
import android.content.Intent;
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

import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.m_name.kbn.SourcePaymentClass;
import com.khanh.expensemanagement.m_name.view.MNameAdapter;
import com.khanh.expensemanagement.util.DateTimeUtil;
import com.khanh.expensemanagement.util.db.DatabaseHelper;

public class RegisterTransActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Add expense";

    EditText amount;
    EditText m_name_category;
    EditText date;
    EditText m_name_source;
    EditText note;
    Button add_expense;
    ImageView source_icon;
    DatabaseHelper databaseHelper;
    RecyclerView m_name_recycler_view;
    TextView m_name_title;

    Integer categorySelectedId;
    Integer sourceSelectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_trans);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(ACTIVITY_TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }
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
        m_name_category.setFocusable(false);
        m_name_category.setClickable(true);
        m_name_category.setOnClickListener(view -> {
            showBottomCategoryDialog(CategoryClass.NAME_IDENT_CD, false);
        });

        date = findViewById(R.id.date);
        date.setFocusable(false);
        date.setClickable(true);
        date.setOnClickListener(view -> {

            DateTimeUtil.showDatePicker(RegisterTransActivity.this, (EditText) view); // Open date picker dialog
        });

        m_name_source = findViewById(R.id.m_name_source);
        m_name_source.setFocusable(false);
        m_name_source.setClickable(true);
        m_name_source.setOnClickListener(view -> showSourceBottomDialog(SourcePaymentClass.NAME_IDENT_CD, true));

        source_icon = findViewById(R.id.source_icon);
        source_icon.setVisibility(View.GONE);

        note = findViewById(R.id.note);

        add_expense = findViewById(R.id.add_expense);
        add_expense.setOnClickListener(view -> {
            databaseHelper.registerTransaction(Integer.valueOf(amount.getText().toString()), note.getText().toString(), categorySelectedId, date.getText().toString(), sourceSelectedId);
            // Back to home
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
        String categoryText = m_name_category.getText().toString();
        String dateText = date.getText().toString().trim();
        String sourceText = m_name_source.getText().toString();

        add_expense.setEnabled(!amountText.isEmpty() && !categoryText.isEmpty() && !dateText.isEmpty() && !sourceText.isEmpty());
    }

    private void showBottomCategoryDialog(String nameIdentCd, Boolean enableCellIcon) {

        final Dialog dialog = new Dialog(RegisterTransActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.m_name_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // add data dialog
        m_name_recycler_view = dialog.findViewById(R.id.m_name_recycler_view);
        m_name_title = dialog.findViewById(R.id.m_name_title);
        m_name_title.setText("カテゴリー");

        MNameAdapter mNameAdapter = new MNameAdapter(RegisterTransActivity.this, RegisterTransActivity.this, nameIdentCd, enableCellIcon, (position, mName, view) -> {
            Toast.makeText(RegisterTransActivity.this, "Clicked: " + mName.getNameCd(), Toast.LENGTH_SHORT).show();
            m_name_category.setText(mName.getNameSs());
            categorySelectedId = Integer.valueOf(mName.getNameCd());
            dialog.dismiss(); // close dialog
        });

        m_name_recycler_view.setAdapter(mNameAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(RegisterTransActivity.this, 4);
        m_name_recycler_view.setLayoutManager(layoutManager);

        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showSourceBottomDialog(String nameIdentCd, Boolean enableCellIcon) {

        final Dialog dialog = new Dialog(RegisterTransActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.m_name_bottom);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        // add data dialog
        m_name_recycler_view = dialog.findViewById(R.id.m_name_recycler_view);
        m_name_title = dialog.findViewById(R.id.m_name_title);
        m_name_title.setText("支払い方");

        MNameAdapter mNameAdapter = new MNameAdapter(RegisterTransActivity.this, RegisterTransActivity.this, nameIdentCd, enableCellIcon, (position, mName, view) -> {
            Toast.makeText(RegisterTransActivity.this, "Clicked: " + mName.getNameCd(), Toast.LENGTH_SHORT).show();
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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(RegisterTransActivity.this, 4);
        m_name_recycler_view.setLayoutManager(layoutManager);

        // display dialog
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}