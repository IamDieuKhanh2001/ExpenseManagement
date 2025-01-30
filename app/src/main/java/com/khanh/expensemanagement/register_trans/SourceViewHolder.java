package com.khanh.expensemanagement.register_trans;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khanh.expensemanagement.R;

public class SourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Integer id;
    ImageView source_icon;
    TextView source_name;
    private final SourceAdapter.OnItemListener onItemListener;

    public SourceViewHolder(@NonNull View itemView, SourceAdapter.OnItemListener onItemListener) {
        super(itemView);
        source_icon = itemView.findViewById(R.id.source_icon);
        source_name = itemView.findViewById(R.id.source_name);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void onClick(View view) {

        onItemListener.onItemClick(getAdapterPosition(), id, (String) source_name.getText(), view);
    }
}
