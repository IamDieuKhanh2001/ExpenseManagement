package com.khanh.expensemanagement.budget_mainte;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

import java.util.List;

class BudgetCategorySelectAdapter extends RecyclerView.Adapter<BudgetCategorySelectViewHolder> {

    private int selectedPosition = -1;
    private Context context;
    private Activity activity;
    private List<Category> categoryList;
    private final OnCategorySelectedListener onCategorySelectedListener;

    public BudgetCategorySelectAdapter(Context context, Activity activity, List<Category> categoryList, OnCategorySelectedListener onCategorySelectedListener) {
        this.context = context;
        this.activity = activity;
        this.categoryList = categoryList;
        this.onCategorySelectedListener = onCategorySelectedListener;
    }

    @NonNull
    @Override
    public BudgetCategorySelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.radio_budget_category_select_row, parent, false);
        return new BudgetCategorySelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetCategorySelectViewHolder holder, int position) {

        holder.categoryId = categoryList.get(position).getCategoryId();
        holder.category_name.setText(categoryList.get(position).getCategoryName());
        if (categoryList.get(position).getDrawableIconUrl() != null) {

            int imageResId = context.getResources().getIdentifier(categoryList.get(position).getDrawableIconUrl(), "drawable", context.getPackageName());
            if (imageResId != 0) {

                holder.category_icon.setImageResource(imageResId);
            }
        }
        holder.category_rb.setChecked(position == selectedPosition);
        holder.category_rb.setOnClickListener(v -> {
            selectedPosition = position;
            onCategorySelectedListener.onCategorySelected(position, categoryList.get(position).getCategoryId()); // Gọi callback để thông báo Activity
            notifyDataSetChanged();
        });
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            onCategorySelectedListener.onCategorySelected(position, categoryList.get(position).getCategoryId()); // Gọi callback để thông báo Activity
            notifyDataSetChanged();
        });
    }

    public void clearSelection() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(int position, Integer categoryIdClick);
    }
}
