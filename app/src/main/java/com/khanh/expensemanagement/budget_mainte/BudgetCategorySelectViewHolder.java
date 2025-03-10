package com.khanh.expensemanagement.budget_mainte;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

class BudgetCategorySelectViewHolder extends RecyclerView.ViewHolder {

    public ImageView category_icon;
    public TextView category_name;
    public RadioButton category_rb;
    public TextView created_category_tv;

    public BudgetCategorySelectViewHolder(@NonNull View itemView) {
        super(itemView);
        category_icon = itemView.findViewById(R.id.category_icon);
        category_name = itemView.findViewById(R.id.category_name);
        category_rb = itemView.findViewById(R.id.category_rb);
        created_category_tv = itemView.findViewById(R.id.created_category_tv);
    }
}
