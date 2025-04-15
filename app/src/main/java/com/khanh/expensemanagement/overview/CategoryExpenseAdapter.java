package com.khanh.expensemanagement.overview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

import java.util.ArrayList;

class CategoryExpenseAdapter extends RecyclerView.Adapter<CategoryExpenseViewHolder> {

    private Context context;
    private Activity activity;
    private final ArrayList<CategoryExpense> categoryExpenseList;

    public CategoryExpenseAdapter(Context context, Activity activity, ArrayList<CategoryExpense> categoryExpenseList) {

        this.context = context;
        this.activity = activity;
        this.categoryExpenseList = categoryExpenseList;
    }

    @NonNull
    @Override
    public CategoryExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_expense_row, parent, false);
        return new CategoryExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryExpenseViewHolder holder, int position) {

        holder.category_name.setText(categoryExpenseList.get(position).getCategoryName());
        holder.total_spent.setText(categoryExpenseList.get(position).getTotalSpent().toString());
    }

    @Override
    public int getItemCount() {
        return categoryExpenseList.size();
    }
}
