package com.khanh.expensemanagement.naiji;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

public class CategoryExpenseViewHolder extends RecyclerView.ViewHolder {

    public TextView category_name;
    public TextView total_spent;

    public CategoryExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        category_name = itemView.findViewById(R.id.category_name);
        total_spent = itemView.findViewById(R.id.total_spent);
    }

}
