package com.khanh.expensemanagement.register_trans;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

import java.util.ArrayList;

public class SourceAdapter extends  RecyclerView.Adapter<SourceViewHolder> {

    private Context context;
    private Activity activity;
    private final ArrayList<Source> sourceList;
    private final OnItemListener onItemListener;

    public SourceAdapter(Context context, Activity activity, ArrayList<Source> sourceList, OnItemListener onItemListener) {
        this.context = context;
        this.activity = activity;
        this.sourceList = sourceList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.source_of_fund_cell, parent, false);
        return new SourceViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {

        holder.setId(sourceList.get(position).getId());
        if (sourceList.get(position).getImage() != null) {

            int imageResId = context.getResources().getIdentifier(sourceList.get(position).getImage(), "drawable", context.getPackageName());
            if (imageResId != 0) {

                holder.source_icon.setImageResource(imageResId);
            }
        }
        holder.source_name.setText(sourceList.get(position).getNameSs());
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, Integer id, String nameSs, View view);
    }
}
