package com.khanh.expensemanagement.m_name.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.domain.db.DatabaseHelper;

import java.util.ArrayList;

public class MNameAdapter extends RecyclerView.Adapter<MNameViewHolder> {

    private Context context;
    private Boolean enableIcon;
    private ArrayList<MName> mNameList;
    private final MNameAdapter.OnItemListener onItemListener;

    private DatabaseHelper databaseHelper;

    public MNameAdapter(Context context, ArrayList<MName> mNameList, Boolean enableIcon, OnItemListener onItemListener) {
        this.context = context;
        this.mNameList = mNameList;
        this.enableIcon = enableIcon;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.m_name_cell, parent, false);

        return new MNameViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MNameViewHolder holder, int position) {

        holder.setmName(mNameList.get(position));
        holder.m_name_ss.setText(mNameList.get(position).getNameSs());
        if (enableIcon) {

            // Enable icon
            holder.m_name_icon.setVisibility(View.VISIBLE);
            if (mNameList.get(position).getDrawableIconUrl() != null) {

                int imageResId = context.getResources().getIdentifier(mNameList.get(position).getDrawableIconUrl(), "drawable", context.getPackageName());
                if (imageResId != 0) {

                    holder.m_name_icon.setImageResource(imageResId);
                }
            }
        } else {

            // Hide icon
            holder.m_name_icon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    public interface OnItemListener {

        void onItemClick(int position, MName mName, View view);
    }
}
