package com.khanh.expensemanagement.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private Context context;
    private Activity activity;
    private final ArrayList<TransactionHistory> transactionHistoryList;

    public TransactionAdapter(Context context, Activity activity, ArrayList<TransactionHistory> transactionHistoryList) {
        this.context = context;
        this.activity = activity;
        this.transactionHistoryList = transactionHistoryList;
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_row, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        if (!transactionHistoryList.get(position).getTransactionTitle().isEmpty()) {

            holder.transaction_title_tv.setText(transactionHistoryList.get(position).getTransactionTitle());
        } else {

            holder.transaction_title_tv.setText(String.format("%sでの出費", transactionHistoryList.get(position).getCategoryTitle()));
        }
        holder.category_tv.setText(transactionHistoryList.get(position).getCategoryTitle());
        holder.transaction_up.setText(String.valueOf(transactionHistoryList.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }
}
