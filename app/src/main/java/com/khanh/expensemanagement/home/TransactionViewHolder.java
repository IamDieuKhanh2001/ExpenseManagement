package com.khanh.expensemanagement.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView transaction_title_tv;
    public TextView category_tv;
    public TextView transaction_up;
    private TransactionHistory transactionHistory;

    private final TransactionAdapter.OnItemListener onItemListener;

    public TransactionViewHolder(@NonNull View itemView, TransactionAdapter.OnItemListener onItemListener) {
        super(itemView);
        transaction_title_tv = itemView.findViewById(R.id.transaction_title_tv);
        category_tv = itemView.findViewById(R.id.category_tv);
        transaction_up = itemView.findViewById(R.id.transaction_up);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(TransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @Override
    public void onClick(View view) {

        onItemListener.onItemClick(getAdapterPosition(), this.getTransactionHistory(), view);
    }
}
