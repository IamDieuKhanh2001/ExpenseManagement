package com.khanh.expensemanagement.budget_mainte;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.util.SqliteUtil;

import java.util.ArrayList;
import java.util.List;

public class BudgetCategorySelectActivity extends AppCompatActivity {

    private RecyclerView budget_category_recycler_view;
    private Button continue_btn;
    DatabaseHelper databaseHelper;
    private RadioButton total_spent_month_rb;
    private LinearLayout total_spent_month_layout;
    BudgetCategorySelectAdapter budgetCategorySelectAdapter;
    private CategorySelectedViewModel categorySelectedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget_category_select);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        getDataDisplay();
        initWatcher();
    }

    private void initWidgets() {

        budget_category_recycler_view = findViewById(R.id.budget_category_recycler_view);
        total_spent_month_rb = findViewById(R.id.total_spent_month_rb);
        total_spent_month_rb.setOnClickListener(view -> {
            budgetCategorySelectAdapter.clearSelection();
            total_spent_month_rb.setChecked(true);
            categorySelectedViewModel.setCategoryIdSelected(-99);
            Log.d("categoryIdSelected", String.valueOf(categorySelectedViewModel.getCategoryIdSelected()));
        });
        total_spent_month_layout = findViewById(R.id.total_spent_month_layout);
        total_spent_month_layout.setOnClickListener(view -> {
            budgetCategorySelectAdapter.clearSelection();
            total_spent_month_rb.setChecked(true);
            categorySelectedViewModel.setCategoryIdSelected(-99);
            Log.d("categoryIdSelected", String.valueOf(categorySelectedViewModel.getCategoryIdSelected()));
        });
        continue_btn = findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(view -> {

        });
    }

    private void getDataDisplay() {

        List<Category> categoryList = new ArrayList<>();

        Cursor cursor = databaseHelper.mNameFindAll(CategoryClass.NAME_IDENT_CD);

        if (cursor != null && cursor.moveToFirst()) {

            do {
                categoryList.add(new Category(cursor.getInt(1), cursor.getString(3), cursor.getString(4)));
            } while (cursor.moveToNext());
        }

        SqliteUtil.releaseCursor(cursor);

        budgetCategorySelectAdapter = new BudgetCategorySelectAdapter(this, this, categoryList, ((position, categoryIdClick) -> {

            total_spent_month_rb.setChecked(false);
            categorySelectedViewModel.setCategoryIdSelected(categoryIdClick);
            Log.d("categoryIdSelected", String.valueOf(categorySelectedViewModel.getCategoryIdSelected()));
        }));
        budget_category_recycler_view.setAdapter(budgetCategorySelectAdapter);
        budget_category_recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initWatcher() {

        categorySelectedViewModel = new ViewModelProvider(this).get(CategorySelectedViewModel.class);

        categorySelectedViewModel.getCategoryIdSelected().observe(this, categoryId -> {
            continue_btn.setEnabled(categoryId != -1);
        });
    }

}