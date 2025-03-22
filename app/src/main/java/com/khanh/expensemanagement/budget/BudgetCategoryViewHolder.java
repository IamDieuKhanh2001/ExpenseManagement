package com.khanh.expensemanagement.budget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.khanh.expensemanagement.R;

class BudgetCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public Integer categoryId;
    public PieChart budget_category_chart;
    public TextView category_name_tv;
    public TextView remaining_title;
    public TextView remaining_amount;
    public TextView total_spent_amount;
    public TextView budget_amount;
    public TextView over_spent_icon;
    public Button more_button;
    private final BudgetCategoryAdapter.OnMoreOptionListener onMoreOptionListener;

    public BudgetCategoryViewHolder(@NonNull View itemView, Context context, BudgetCategoryAdapter.OnMoreOptionListener onMoreOptionListener) {

        super(itemView);

        budget_category_chart = itemView.findViewById(R.id.budget_category_chart);
        category_name_tv = itemView.findViewById(R.id.category_name_tv);
        remaining_title = itemView.findViewById(R.id.remaining_title);
        remaining_amount = itemView.findViewById(R.id.remaining_amount);
        total_spent_amount = itemView.findViewById(R.id.total_spent_amount);
        budget_amount = itemView.findViewById(R.id.budget_amount);
        over_spent_icon = itemView.findViewById(R.id.over_spent_icon);
        more_button = itemView.findViewById(R.id.more_button);
        this.onMoreOptionListener = onMoreOptionListener;
        more_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onMoreOptionListener.onClick(getAdapterPosition(), categoryId);
    }
}
