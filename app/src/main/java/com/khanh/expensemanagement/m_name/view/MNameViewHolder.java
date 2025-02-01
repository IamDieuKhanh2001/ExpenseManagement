package com.khanh.expensemanagement.m_name.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;

public class MNameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView m_name_icon;
    TextView m_name_ss;
    MName mName;

    private final MNameAdapter.OnItemListener onItemListener;

    public MNameViewHolder(@NonNull View itemView, MNameAdapter.OnItemListener onItemListener) {
        super(itemView);
        m_name_icon = itemView.findViewById(R.id.m_name_icon);
        m_name_ss = itemView.findViewById(R.id.m_name_ss);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    public MName getmName() {
        return mName;
    }

    public void setmName(MName mName) {
        this.mName = mName;
    }

    @Override
    public void onClick(View view) {

        onItemListener.onItemClick(getAdapterPosition(), this.getmName(), view);
    }
}
