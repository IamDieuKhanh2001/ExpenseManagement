package com.khanh.expensemanagement.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.util.FormUtil;

import java.util.ArrayList;

class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private Context context;
    private Activity activity;
    private final ArrayList<TransactionHistory> transactionHistoryList;
    private final TransactionAdapter.OnItemListener onItemListener;

    public TransactionAdapter(Context context, Activity activity, ArrayList<TransactionHistory> transactionHistoryList, OnItemListener onItemListener) {
        this.context = context;
        this.activity = activity;
        this.transactionHistoryList = transactionHistoryList;
        this.onItemListener = onItemListener;
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_row, parent, false);
        return new TransactionViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        holder.setTransactionHistory(transactionHistoryList.get(position));
        if (!transactionHistoryList.get(position).getNote().isEmpty()) {

            holder.transaction_title_tv.setText(transactionHistoryList.get(position).getNote());
        } else {

            holder.transaction_title_tv.setText(String.format("%sでの出費", transactionHistoryList.get(position).getCategoryTitle()));
        }
        holder.category_tv.setText(transactionHistoryList.get(position).getCategoryTitle());
        String amountText = String.valueOf(transactionHistoryList.get(position).getAmount());
        amountText = FormUtil.fncDecFormat(amountText);
        holder.transaction_up.setText(context.getString(R.string.transaction_amount_currency, amountText));
    }

    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }

    public interface OnItemListener {

        void onItemClick(int position, TransactionHistory transactionHistory, View view);
    }
}
