package com.khanh.expensemanagement.budget_mainte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.MainActivity;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;
import com.khanh.expensemanagement.m_name.kbn.CategoryClass;
import com.khanh.expensemanagement.util.ActivityUtil;
import com.khanh.expensemanagement.util.SqliteUtil;

import java.util.ArrayList;
import java.util.List;

public class BudgetCategorySelectActivity extends AppCompatActivity {

    private final String ACTIVITY_TITLE = "Create budget";

    private RecyclerView budget_category_recycler_view;
    private Button continue_btn;
    DatabaseHelper databaseHelper;
    private RadioButton total_spent_month_rb;
    private LinearLayout total_spent_month_layout;
    BudgetCategorySelectAdapter budgetCategorySelectAdapter;
    private CategorySelectedViewModel categorySelectedViewModel;
    private TextView created_total_tv;
    private ImageView total_spent_icon;
    private TextView total_spent_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget_category_select);
        ActivityUtil.enableActionBar(this, ACTIVITY_TITLE);
        databaseHelper = new DatabaseHelper(this);
        initWidgets();
        getDataDisplay();
        initWatcher();
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

        total_spent_icon = findViewById(R.id.total_spent_icon);
        total_spent_tv = findViewById(R.id.total_spent_tv);
        created_total_tv = findViewById(R.id.created_total_tv);
        budget_category_recycler_view = findViewById(R.id.budget_category_recycler_view);
        total_spent_month_rb = findViewById(R.id.total_spent_month_rb);
        total_spent_month_rb.setOnClickListener(view -> {
            budgetCategorySelectAdapter.clearSelection();
            total_spent_month_rb.setChecked(true);
            categorySelectedViewModel.setCategoryIdSelected(-99);
        });
        total_spent_month_layout = findViewById(R.id.total_spent_month_layout);
        total_spent_month_layout.setOnClickListener(view -> {
            budgetCategorySelectAdapter.clearSelection();
            total_spent_month_rb.setChecked(true);
            categorySelectedViewModel.setCategoryIdSelected(-99);
        });
        continue_btn = findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), BudgetRegisterActivity.class);
            intent.putExtra("categoryIdSelected", categorySelectedViewModel.getCategoryIdSelected().getValue());
            startActivity(intent);
            finish();
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
        }));
        budget_category_recycler_view.setAdapter(budgetCategorySelectAdapter);
        budget_category_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        disableCreatedBudget();
    }

    private void disableCreatedBudget() {

        List<Integer> disablePositionList = new ArrayList<>();

        // Disable selection total budget on month if exist in database
        Cursor cursorTotal = databaseHelper.budgetTotalSpendingMonth();
        if (cursorTotal != null && cursorTotal.moveToFirst()) {

            // Show budget created, disable radio selection
            created_total_tv.setVisibility(View.VISIBLE);
            total_spent_month_rb.setVisibility(View.GONE);
            // disable click on layout
            total_spent_month_layout.setOnClickListener(null);
            // Blur icon and title of selection
            total_spent_icon.setAlpha(0.5f);
            total_spent_tv.setAlpha(0.5f);
        } else {

            created_total_tv.setVisibility(View.GONE);
            total_spent_month_rb.setVisibility(View.VISIBLE);
        }

        // Disable selection budget by category if exist in database
        for (int i = 0; i < budgetCategorySelectAdapter.getCategoryList().size(); i++) {

            Cursor cursorCategory = databaseHelper.budgetFindByCategoryId(budgetCategorySelectAdapter.getCategoryList().get(i).getCategoryId());
            if (cursorCategory != null && cursorCategory.moveToFirst()) {

                disablePositionList.add(i);
            }
        }
        if (!disablePositionList.isEmpty()) {

            budgetCategorySelectAdapter.disableSelectionAtPositions(disablePositionList);
        }
    }

    private void initWatcher() {

        categorySelectedViewModel = new ViewModelProvider(this).get(CategorySelectedViewModel.class);

        categorySelectedViewModel.getCategoryIdSelected().observe(this, categoryId -> {
            continue_btn.setEnabled(categoryId != -1);
        });
    }

}