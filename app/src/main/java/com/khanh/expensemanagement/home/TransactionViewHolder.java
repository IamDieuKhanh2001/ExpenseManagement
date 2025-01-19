package com.khanh.expensemanagement.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    public TextView transaction_title_tv;
    public TextView category_tv;
    public TextView transaction_up;


    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        transaction_title_tv = itemView.findViewById(R.id.transaction_title_tv);
        category_tv = itemView.findViewById(R.id.category_tv);
        transaction_up = itemView.findViewById(R.id.transaction_up);
    }
}
